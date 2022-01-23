package task;

import dto.GraphDTO;
import dto.TargetDTO;
import dto.TaskParamsDTO;
import exceptions.CycleException;
import graph.Graph;
import graph.SerialSetsContainer;
import target.RunResults;
import target.RunStatus;
import target.Target;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;



public abstract class Task{

    protected Graph graph;
    public static Object taskDummyLock = new Object();
    protected SerialSetsContainer serialSetsContainer;
    private CountDownLatch latch;


    public Task(Graph graph, SerialSetsContainer serialSetsContainer) {
        this.graph = graph;
        this.serialSetsContainer = serialSetsContainer;
        this.latch = null;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public static List<Target> topologicalSort(Graph graph) throws CycleException {
        List<Target> sortedTargets = new ArrayList<>();
        Map<String, Integer> targetsInDegree = getTargetsInDegree(graph.getTargetGraph());
        List<Target> sourceTargets = initSourceQueue(graph.getTargets());
        while (!sourceTargets.isEmpty()) {
            Target currTarget = sourceTargets.remove(0);
            sortedTargets.add(currTarget);
            for (Target neighborTarget : currTarget.getRequiredFor()) {
                targetsInDegree.put(neighborTarget.getName(), (targetsInDegree.get(neighborTarget.getName()) - 1));
                if ((targetsInDegree.get(neighborTarget.getName())) == 0) {
                    sourceTargets.add(graph.getTarget(neighborTarget.getName()));
                }
            }
        }
        checkingForCycle(targetsInDegree);
        return sortedTargets;
    }
    //main Thread
    public GraphDTO executeTaskOnGraph(List<Consumer<TargetDTO>> outputConsumers,
                                       Consumer<PausableThreadPoolExecutor> threadPoolConsumer,
                                       int threadNumber) throws CycleException {


        List<Target> sortedTargets = topologicalSort(this.graph);

        BlockingQueue<Runnable> workingQueue = new LinkedBlockingQueue<>();
        PausableThreadPoolExecutor threadPool = new PausableThreadPoolExecutor(threadNumber, workingQueue);
        threadPoolConsumer.accept(threadPool);
        this.latch = new CountDownLatch(sortedTargets.size());
        LocalTime startTime = LocalTime.now();
        for(Target currTarget : sortedTargets){
            addTargetToThreadPool(threadPool, currTarget, outputConsumers);
        }
        threadPool.shutdown();
        try {
            this.latch.await();
            //System.out.println("RunTask==>Woke from latch!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LocalTime endTime = LocalTime.now();
        //System.out.println("Thread Pool===> Finished!!! " +  threadPool.isTerminated());
        GraphDTO graphRunResult = new GraphDTO(this.graph, Duration.between(startTime, endTime).toMillis());
        createGraphOfFailedTargets();
        return graphRunResult;
    }

    private void addTargetToThreadPool(PausableThreadPoolExecutor threadPool, Target currTarget, List<Consumer<TargetDTO>> outputConsumers) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                TargetDTO targetResult;
                synchronized (currTarget){
                    while(currTarget.getRunStatus().equals(RunStatus.FROZEN)){
                        try {
                            currTarget.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(currTarget.getRunStatus().equals(RunStatus.WAITING)){
                    currTarget.getSerialSetsMonitors();
                    targetResult = executeTaskOnTarget(currTarget);
                    if(targetResult.getRunResult().equals(RunResults.FAILURE)){
                        currTarget.updateParentsStatus(targetResult.getSkippedFathers(), currTarget.getName()); //כל מי שסגרתי לריצה בגללי
                    }
                    getOpenedTargetsToRun(targetResult, currTarget);
                }
                else {
                    targetResult = new TargetDTO(currTarget);
                }
                currTarget.freeSerialSetsMonitors();
                outputTargetResult(outputConsumers, targetResult);
                latch.countDown();

                //System.out.println("The latch value is=" + latch.toString());
            }
        });
    }


    private void getOpenedTargetsToRun(TargetDTO targetResult, Target target) {
        boolean isOpenedToRun = true;
        for(Target currParent : target.getRequiredFor()){
            for(Target childOfTheCurrParent : currParent.getDependsOn()){
                if (childOfTheCurrParent.getRunStatus().equals(RunStatus.FROZEN)
                || childOfTheCurrParent.getRunStatus().equals(RunStatus.WAITING)) {

                    //currParent.getWaitForThisTargetsToBeFinished().add(childOfTheCurrParent.getName());
                    isOpenedToRun = false;
                }
                else{
                    currParent.getWaitForThisTargetsToBeFinished().remove(childOfTheCurrParent.getName());
                    isOpenedToRun = true;
                }
            }
            if (isOpenedToRun) {
                if(!currParent.getRunStatus().equals(RunStatus.SKIPPED)){
                    currParent.setRunStatus(RunStatus.WAITING);
                    currParent.setStartWaitingTime(LocalTime.now());
                }
                targetResult.getTargetsThatCanBeRun().add(currParent.getName());
            }
            else{
                isOpenedToRun = true;
            }

        }
    }


    protected abstract TargetDTO executeTaskOnTarget(Target currTarget);

    private void createGraphOfFailedTargets() {
        String graphName = this.graph.getName();

        Graph newGraph = new Graph(new HashMap<>(), graphName);
        for(Target currTarget : this.graph.getTargets()){
            if(currTarget.getRunResult().equals(RunResults.FAILURE) ||
                    currTarget.getRunResult().equals(RunResults.SKIPPED)){
                Target clonedTarget = currTarget.clone();
                clonedTarget.setRunResult(null);
                clonedTarget.setRunStatus(RunStatus.FROZEN);
                clonedTarget.updateWaitForTheseTargetsToBeFinished();
                newGraph.getTargetGraph().put(clonedTarget.getName(), clonedTarget);
            }
        }
        Graph.updateGraphTargets(newGraph);
        this.graph = newGraph;
    }

    private synchronized void outputTargetResult(List<Consumer<TargetDTO>> outputConsumers, TargetDTO targetResult) {
        for(Consumer<TargetDTO> currConsumer: outputConsumers){
            currConsumer.accept(targetResult);
        }
    }

    private static List<Target> initSourceQueue(Collection<Target> targets) {
        List<Target> sourceTargets = new LinkedList<>();
        for(Target target : targets){
            target.setRunStatus(RunStatus.FROZEN);
            if(target.getDependsOn().isEmpty()){
                target.setRunStatus(RunStatus.WAITING);
                target.setStartWaitingTime(LocalTime.now());
                sourceTargets.add(target);
            }
        }
        return sourceTargets;
    }

    private static void checkingForCycle(Map<String, Integer> targetsInDegree) throws CycleException {
        for(Map.Entry<String,Integer> targetEntry : targetsInDegree.entrySet()){
            if((targetEntry.getValue()) != 0){
                throw new CycleException(targetEntry.getKey());
            }
        }
    }

    private static Map<String, Integer> getTargetsInDegree(Map<String, Target> graphMap){
        Map<String, Integer> targetsInDegree = new HashMap<>();
        for(Map.Entry<String, Target> targetEntry : graphMap.entrySet()){
            targetsInDegree.put(targetEntry.getKey(), targetEntry.getValue().getDependsOn().size());
        }
        return targetsInDegree;
    }

    public abstract void updateParameters(TaskParamsDTO taskParamsDTO);

    //----------------------------------------------------------------------------------------------
//    class TaskRunner implements Runnable{
//        List<Consumer<TargetDTO>> outputConsumers;
//        Target currTarget;
//
//        public TaskRunner(List<Consumer<TargetDTO>> outputConsumers) {
//            this.outputConsumers = outputConsumers;
//        }
//
//        public void setCurrTarget(Target currTarget) {
//            this.currTarget = currTarget;
//        }
//
//        @Override
//        public void run() {
//            TargetDTO targetResult;
//            synchronized (currTarget){
//                while(currTarget.getRunStatus().equals(RunStatus.FROZEN)){
//                    try {
//                        currTarget.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//                if(currTarget.getRunStatus().equals(RunStatus.WAITING)){
//                    currTarget.getSerialSetsMonitors();
//                    targetResult = executeTaskOnTarget(currTarget);
//                    if(targetResult.getRunResult().equals(RunResults.FAILURE)){
//                        currTarget.updateParentsStatus(targetResult.getSkippedFathers());
//                    }
//                    getOpenedTargetsToRun(targetResult, currTarget);
//                }
//                else {
//                    targetResult = new TargetDTO(currTarget);
//                }
//                currTarget.freeSerialSetsMonitors();
//                outputTargetResult(outputConsumers, targetResult);
//                latch.countDown();
//
//                System.out.println("The latch value is=" + latch.toString());
//
//        }
//    }

}
