package task;

import dto.ExecutionTargetDTO;
import dto.TargetDTO;
import dto.TaskDTO;
import dto.TaskParamsDTO;
import engine.ExceptionMessages;
import engine.TasksManager;
import exceptions.CycleException;
import general_enums.*;
import graph.Graph;
import target.Target;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;


public abstract class Task {

    protected Graph graph;
    protected String taskName;
    protected String creatorName;
    protected int totalTaskPrice;
    protected Set<String> registeredWorkers = new HashSet<>();
    protected TaskStatus status;
    private CountDownLatch latch;
    protected List<Target> sortedTargets = new ArrayList<>();
    protected List<Target> finishedTargets = new ArrayList<>();
    private boolean isTaskFinished = false;
    private String path;

    public List<Target> getSkippedTargets() {
        return skippedTargets;
    }

    private List<Target> skippedTargets = new ArrayList<>();

    public void setPath(String path) {
        this.path = path;
    }

    //public static Object taskDummyLock = new Object();


    public Task(Graph graph, String creatorName, String taskName, int totalPriceTask, RunType runType) {
        this.graph = graph;
        this.creatorName = creatorName;
        this.latch = null;
        this.status = TaskStatus.NEW;
        this.taskName = taskName;
        this.totalTaskPrice = totalPriceTask;
    }

    public boolean isTaskFinished() {
        return isTaskFinished;
    }

    public Graph getGraph() {
        return graph;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public synchronized void setStatus(TaskStatus status) {
        this.status = status;
    }

    public synchronized void setSortedTargets(List<Target> sortedTargets) {
        this.sortedTargets = sortedTargets;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getPath() {
        return path;
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


    public void StartTask() throws CycleException {
        List<Target> sortedTargets = topologicalSort(this.graph);
        this.status = TaskStatus.ACTIVE;
        this.sortedTargets = sortedTargets;
        //executeTaskOnGraph(sortedTargets);
    }

    public void pauseTask() {

    }

    public void resumeTask() {

    }

    public void stopTask() {

    }

    //this function will be called when a worker wants to get a target to work on.
    public Target getTargetForRunning() {
        Target target = null;
        if (!this.sortedTargets.isEmpty()) {
            target = this.sortedTargets.get(0);
            if (target.getRunStatus().equals(RunStatus.WAITING)) {
                this.sortedTargets.remove(0);
                return target;
            }
        } else {
            this.isTaskFinished = true;
        }
        return null;
    }

    //this function will be called when a worker sends the run result of a target.
    public synchronized int updateTargetsRunResult(Target target, ExecutionTargetDTO executionTargetDTO){
        if(!target.updateTarget(executionTargetDTO)){
            return 0;
        }
//        System.out.println(LocalTime.now() + "-IN (Task)updateTargetRunResult- start updating: " + target.getName());
        int priceForTarget = 0;
        if(target.getRunResult().equals(RunResults.FAILURE)){
            target.updateParentsStatus(target.getSkippedFathers(), target.getName()); //כל מי שסגרתי לריצה בגללי
        }
        if(target.getRunStatus().equals(RunStatus.FINISHED)){
            priceForTarget = this.totalTaskPrice/this.graph.getTargets().size();
            getOpenedTargetsToRun(target);
        }
        printTargets();
//        System.out.println(LocalTime.now() + "-IN (Task)updateTargetRunResult- stop updating: " + target.getName());
        checkIfTaskIsFinished();

        return priceForTarget;
    }

    private void printTargets(){
        for(Target t: this.graph.getTargets()){
            String rr = t.getRunResult() != null? t.getRunResult().name() : "";
            String rs = t.getRunStatus() != null? t.getRunStatus().name() : "";
//            System.out.println("Target: " + t.getName() + "RunStatus: " + rs + "RunResult: " +  rr);
        }
    }

    //The function over on all the targets in the task and check if they finished-
    //if so, the function changes the task status to finished.
    private void checkIfTaskIsFinished() {
        isTaskFinished = true;
        for(Target target : this.graph.getTargets()){
            if(!target.getRunStatus().equals(RunStatus.FINISHED) && !target.getRunStatus().equals(RunStatus.SKIPPED)){
                isTaskFinished = false;
                break;
            }
        }
        if(isTaskFinished){
            this.setStatus(TaskStatus.FINISHED);
//            System.out.println("Num of skipped target===> " + this.skippedTargets.size());
//            System.out.println("The skipped targets: " + this.skippedTargets.toString());
//            for(Target target : this.skippedTargets){
//                TasksManager.writeTargetRunResultToFile(target,this);
//            }
        }
    }


    //main Thread
//    public void executeTaskOnGraph(List<Consumer<TargetDTO>> outputConsumers,
//                                       Consumer<PausableThreadPoolExecutor> threadPoolConsumer,
//                                       int threadNumber) throws CycleException {
//
//
////        List<Target> sortedTargets = topologicalSort(this.graph);
////        this.status = TaskStatus.ACTIVE;
//
       // BlockingQueue<Runnable> workingQueue = new LinkedBlockingQueue<>();
//        PausableThreadPoolExecutor threadPool = new PausableThreadPoolExecutor(threadNumber, workingQueue);
////        threadPoolConsumer.accept(threadPool);
////        this.latch = new CountDownLatch(sortedTargets.size());
//        LocalTime startTime = LocalTime.now();
//
////        for(Target currTarget : sortedTargets){
////            addTargetToThreadPool(threadPool, currTarget, outputConsumers);
////        }
////        threadPool.shutdown();
//        try {
//            this.latch.await();
//            //System.out.println("RunTask==>Woke from latch!");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        LocalTime endTime = LocalTime.now();
//        //System.out.println("Thread Pool===> Finished!!! " +  threadPool.isTerminated());
////        GraphDTO graphRunResult = new GraphDTO(this.graph, Duration.between(startTime, endTime).toMillis());
////        createGraphOfFailedTargets();
//        return graphRunResult;
//    }

//    private void addTargetToThreadPool(PausableThreadPoolExecutor threadPool, Target currTarget, List<Consumer<TargetDTO>> outputConsumers) {
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                TargetDTO targetResult;
//                synchronized (currTarget){
//                    while(currTarget.getRunStatus().equals(RunStatus.FROZEN)){
//                        try {
//                            currTarget.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                if(currTarget.getRunStatus().equals(RunStatus.WAITING)){
//                    //currTarget.getSerialSetsMonitors();
//                    targetResult = executeTaskOnTarget(currTarget);
//                    if(targetResult.getRunResult().equals(RunResults.FAILURE)){
//                        currTarget.updateParentsStatus(targetResult.getSkippedFathers(), currTarget.getName()); //כל מי שסגרתי לריצה בגללי
//                    }
//                    getOpenedTargetsToRun(targetResult, currTarget);
//                }
//                else {
//                    targetResult = new TargetDTO(currTarget);
//                }
//                outputTargetResult(outputConsumers, targetResult);
//                latch.countDown();
//
//                //System.out.println("The latch value is=" + latch.toString());
//            }
//        });
//    }


    private void getOpenedTargetsToRun(Target target) {
        boolean isOpenedToRun = true;
        for(Target currParent : target.getRequiredFor()){
            for(Target childOfTheCurrParent : currParent.getDependsOn()){
                if (childOfTheCurrParent.getRunStatus().equals(RunStatus.FROZEN)
                || childOfTheCurrParent.getRunStatus().equals(RunStatus.WAITING)) {
                    synchronized (currParent){
                        currParent.getWaitForThisTargetsToBeFinished().add(childOfTheCurrParent.getName());
                    }
                    isOpenedToRun = false;
                }
                else{
                    synchronized (currParent){
                        currParent.getWaitForThisTargetsToBeFinished().remove(childOfTheCurrParent.getName());
                    }
                    isOpenedToRun = true;
                }
            }
            if (isOpenedToRun) {
//                if(!currParent.getRunStatus().equals(RunStatus.SKIPPED)){
               if(currParent.getRunStatus().equals(RunStatus.FROZEN)){
                    currParent.setRunStatus(RunStatus.WAITING);
                    currParent.setStartWaitingTime(LocalTime.now());
                }
                synchronized (target){
                    target.getTargetsThatCanBeRun().add(currParent.getName());
                }
            }
            else{
                isOpenedToRun = true;
            }
        }
    }


    protected abstract void executeTaskOnTarget(Target currTarget);

    private void createGraphOfFailedTargets() {
        String graphName = this.graph.getName();

        Graph newGraph = new Graph(new HashMap<>(), graphName, this.graph.getTaskPricePerTarget(), creatorName);
        for (Target currTarget : this.graph.getTargets()) {
            if (currTarget.getRunResult().equals(RunResults.FAILURE) ||
                    currTarget.getRunResult().equals(RunResults.SKIPPED)) {
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

//    private synchronized void outputTargetResult(List<Consumer<TargetDTO>> outputConsumers, TargetDTO targetResult) {
//        for(Consumer<TargetDTO> currConsumer: outputConsumers){
//            currConsumer.accept(targetResult);
//        }
//    }

    private static List<Target> initSourceQueue(Collection<Target> targets) {
        List<Target> sourceTargets = new LinkedList<>();
        for (Target target : targets) {
            target.setRunStatus(RunStatus.FROZEN);
            target.updateWaitForTheseTargetsToBeFinished();
            if (target.getDependsOn().isEmpty()) {
                target.setRunStatus(RunStatus.WAITING);
                target.setStartWaitingTime(LocalTime.now());
                sourceTargets.add(target);
            }
        }
        return sourceTargets;
    }

    private static void checkingForCycle(Map<String, Integer> targetsInDegree) throws CycleException {
        for (Map.Entry<String, Integer> targetEntry : targetsInDegree.entrySet()) {
            if ((targetEntry.getValue()) != 0) {
                throw new CycleException(targetEntry.getKey());
            }
        }
    }

    private static Map<String, Integer> getTargetsInDegree(Map<String, Target> graphMap) {
        Map<String, Integer> targetsInDegree = new HashMap<>();
        for (Map.Entry<String, Target> targetEntry : graphMap.entrySet()) {
            targetsInDegree.put(targetEntry.getKey(), targetEntry.getValue().getDependsOn().size());
        }
        return targetsInDegree;
    }

    public abstract void updateParameters(TaskParamsDTO taskParamsDTO);

    public abstract TaskDTO createTaskDTO();


    //if stopped-> can't change the status
    //if suspended-> can be changed to stopped or active
    //if active-> can be changed to stopped or suspended
    //if new-> can be changed to active
    public boolean updateTaskStatus(TaskStatus newStatus, String workingDirectory) throws CycleException {
        boolean isStatusChanged = false;
        switch (this.status) {
            case NEW:
                if (newStatus.equals(TaskStatus.ACTIVE)) {
                    this.setStatus(newStatus);
                    setSortedTargets(topologicalSort(this.graph));
                    TasksManager.openDirectoryAndFiles(this.getTaskType(), this, workingDirectory);
                    isStatusChanged = true;
                }
                break;
            case ACTIVE:
                if (newStatus.equals(TaskStatus.STOPPED) || (newStatus.equals(TaskStatus.SUSPENDED))) {
                    if(newStatus.equals(TaskStatus.STOPPED)){
                        for(Target target : this.graph.getTargets()){
                            if(target.getRunResult() == null){
                                target.setRunResult(RunResults.SKIPPED);
                            }
                        }
                    }
                    this.setStatus(newStatus);
                    isStatusChanged = true;
                }
                break;
            case SUSPENDED:
                if (newStatus.equals(TaskStatus.STOPPED) || (newStatus.equals(TaskStatus.ACTIVE))) {
                    if(newStatus.equals(TaskStatus.STOPPED)){
                        for(Target target : this.graph.getTargets()){
                            if(target.getRunResult() == null){
                                target.setRunResult(RunResults.SKIPPED);
                                target.setRunStatus(RunStatus.SKIPPED);
                            }
                        }
                    }
                    this.setStatus(newStatus);
                    isStatusChanged = true;
                }
                break;
            case STOPPED:
            case FINISHED:
                isStatusChanged = false;
                break;
        }
        return isStatusChanged;
    }

    public synchronized void addWorkerToTask(String workerName) throws Exception {
        if(this.status.equals(TaskStatus.STOPPED) || this.status.equals(TaskStatus.FINISHED)){
            throw new Exception(ExceptionMessages.INVALID_TASK_STATUS);
        }
        this.registeredWorkers.add(workerName);
    }

    public synchronized void removeWorkerFromTask(String workerName){
        if(this.status.equals(TaskStatus.STOPPED) || this.status.equals(TaskStatus.FINISHED)){
            this.registeredWorkers.remove(workerName);
        }
    }

    public TaskType getTaskType(){
        if(this instanceof SimulationTask){
            return TaskType.SIMULATION_TASK;
        }
        else{
            return TaskType.COMPILATION_TASK;
        }
    }

    public List<Target> getSortedTargets() {
        return sortedTargets;
    }

    public synchronized TargetDTO getTargetReadyForRunning() throws Exception {
        TargetDTO targetDTO = null;
        if(sortedTargets.get(0).getName().equals("E")){
//            System.out.println("STOP");
        }
        if(this.status.equals(TaskStatus.ACTIVE)){
            if(this.sortedTargets.get(0).getRunStatus().equals(RunStatus.SKIPPED)){
//                System.out.println("Hellllo");
            }
            switch (this.sortedTargets.get(0).getRunStatus()){
                case WAITING:
                    Target targetToSend = this.sortedTargets.remove(0);
                    targetDTO = targetToSend.makeDTO(this.taskName);
                    break;
                case SKIPPED:
                    this.skippedTargets.add(this.sortedTargets.remove(0));
                    break;
                case FINISHED:
                    this.finishedTargets.add(this.sortedTargets.remove(0));
                    //TasksManager.writeTargetRunResultToFile(currTarget,this);
                    break;
                default:
                    break;
            }
        }
//        else{
//            checkTaskStatus();
//        }
        return targetDTO;
    }

//    private void checkTaskStatus() throws Exception {
//        switch (this.status){
//            case SUSPENDED:
//                throw new Exception(ExceptionMessages.TASK + this.taskName + ExceptionMessages.TASK_STATUS_SUSPENDED
//                        + "\n" + ExceptionMessages.CAN_NOT_GET_TARGETS + "\n" + ExceptionMessages.TRY_LATER);
//            case STOPPED:
//                throw new Exception(ExceptionMessages.TASK + this.taskName + ExceptionMessages.TASK_STATUS_STOPPED
//                        + "\n" + ExceptionMessages.CAN_NOT_GET_TARGETS);
//            case FINISHED:
//                throw new Exception(ExceptionMessages.TASK + this.taskName + ExceptionMessages.TASK_STATUS_FINISHED
//                        + "\n" + ExceptionMessages.CAN_NOT_GET_TARGETS);
//            case NEW:
//                throw new Exception(ExceptionMessages.TASK + this.taskName + ExceptionMessages.TASK_NOT_ACTIVE);
//        }
//    }


}
