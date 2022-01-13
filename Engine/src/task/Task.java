package task;

import dto.GraphDTO;
import dto.TargetDTO;
import dto.TaskParamsDTO;
import exceptions.CycleException;
import graph.Graph;
import graph.SerialSet;
import graph.SerialSetsContainer;
import target.RunResults;
import target.RunStatus;
import target.Target;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;



public abstract class Task{

    protected Graph graph;
    public static Object printDummy = new Object();
    protected SerialSetsContainer serialSetsContainer;

    public Task(Graph graph, SerialSetsContainer serialSetsContainer) {
        this.graph = graph;
        this.serialSetsContainer = serialSetsContainer;
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
    public GraphDTO executeTaskOnGraph(List<Consumer<TargetDTO>> outputConsumers) throws CycleException {


        List<Target> sortedTargets = topologicalSort(this.graph);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        LocalTime startTime = LocalTime.now();
        for(Target currTarget : sortedTargets){
            TaskRunner taskRunner = new TaskRunner(outputConsumers);
            taskRunner.setCurrTarget(currTarget);
            threadPool.execute(taskRunner);
        }
        threadPool.shutdown();
        synchronized (printDummy) {
            while (!threadPool.isTerminated()) {
                try {
                    printDummy.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        LocalTime endTime = LocalTime.now();

        GraphDTO graphRunResult = new GraphDTO(this.graph, Duration.between(startTime, endTime).toMillis());
        createGraphOfFailedTargets();
        return graphRunResult;
    }




    private void getOpenedTargetsToRun(TargetDTO targetResult, Target target) {
        boolean isOpenedToRun = true;
        for(Target currParent : target.getRequiredFor()){
            for(Target childOfTheCurrParent : currParent.getDependsOn()){
                if (childOfTheCurrParent.getRunStatus().equals(RunStatus.FROZEN)) {
                    isOpenedToRun = false;
                }
            }
            if (isOpenedToRun) {
                if(!currParent.getRunStatus().equals(RunStatus.SKIPPED)){
                    currParent.setRunStatus(RunStatus.WAITING);
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
                currTarget.setRunResult(null);
                currTarget.setRunStatus(RunStatus.FROZEN);
                newGraph.getTargetGraph().put(currTarget.getName(), currTarget.clone());
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
            if(target.getDependsOn().isEmpty()){
                target.setRunStatus(RunStatus.WAITING);
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

    private class TaskRunner implements Runnable{
        List<Consumer<TargetDTO>> outputConsumers;
        Target currTarget;

        public TaskRunner(List<Consumer<TargetDTO>> outputConsumers) {
            this.outputConsumers = outputConsumers;
        }

        public void setCurrTarget(Target currTarget) {
            this.currTarget = currTarget;
        }

        @Override
        public void run() {
            //System.out.println("In Thread "+ Thread.currentThread().getName());

            TargetDTO targetResult;
            synchronized (currTarget){
                while(currTarget.getRunStatus().equals(RunStatus.FROZEN)){
                    try {
//                        synchronized (Task.printDummy){
//                            System.out.println(Thread.currentThread().getName() + ": Going to wait on Target: " + currTarget.getName());
//                        }
                        currTarget.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
                if(currTarget.getRunStatus().equals(RunStatus.WAITING)){
                    currTarget.getSerialSetsMonitors();
//                    synchronized (Task.printDummy){
//                        System.out.println(Thread.currentThread().getName() + ": working on Target: " + currTarget.getName());
//                    }
                    targetResult = executeTaskOnTarget(currTarget);
//                    synchronized (Task.printDummy){
//                        System.out.println(Thread.currentThread().getName() + ": finished on Target: " + currTarget.getName());
//                    }
                    if(targetResult.getRunResult().equals(RunResults.FAILURE)){
                        currTarget.updateParentsStatus(targetResult.getSkippedFathers());
                    }
                    getOpenedTargetsToRun(targetResult, currTarget);
                }
                else {  //currTarget.RunStatus = Skipped
//                    synchronized (Task.printDummy){
//                        System.out.println(Thread.currentThread().getName() + ": working on Target: " + currTarget.getName());
//                    }
                    targetResult = new TargetDTO(currTarget);
//                    synchronized (Task.printDummy){
//                        System.out.println(Thread.currentThread().getName() + ": finished on Target: " + currTarget.getName());
//                    }
                }
                currTarget.freeSerialSetsMonitors();
                outputTargetResult(outputConsumers, targetResult);
                printDummy.notifyAll();
        }
    }

}
