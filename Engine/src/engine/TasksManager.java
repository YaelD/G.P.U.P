package engine;

import dto.*;
import general_enums.RunStatus;
import general_enums.TaskStatus;
import general_enums.TaskType;
import graph.Graph;
import target.Target;
import task.CompilationTask;
import task.SimulationTask;
import task.Task;

import java.io.*;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class TasksManager {

    private final String workingDirectory = "c:\\gpup-working-dir";

    private Map<String, Task> tasksInSystem = new HashMap<>();

    public TasksManager() {
        File directory = new File(this.workingDirectory);
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public Map<String, Task> getTasksInSystem() {
        return tasksInSystem;
    }

    public synchronized void addTaskToSystem(Task task) throws Exception {
        if (isTaskExistInSystem(task.getTaskName())) {
            throw new Exception(ExceptionMessages.TASK + task.getTaskName() + ExceptionMessages.ALREADY_EXIST);
        }
        tasksInSystem.put(task.getTaskName(), task);
    }

    public TaskType getTaskTypeByName(String taskName){
        Task task = this.tasksInSystem.get(taskName);
        if(task instanceof SimulationTask){
            return TaskType.SIMULATION_TASK;
        }
        else{
            return TaskType.COMPILATION_TASK;
        }
    }

    public boolean isTaskExistInSystem(String taskName) {
        return this.tasksInSystem.containsKey(taskName);
    }

    public boolean isRunInIncrementalMode(TaskType taskType, Set<String> selectedTargets) {
        if(!this.tasksInSystem.containsKey(taskType)){
            return false;
        }
        if(this.tasksInSystem.get(taskType).getGraph().getTargets().isEmpty()){
            return false;
        }
        for(String target : selectedTargets){
            if(this.tasksInSystem.get(taskType).getGraph().getTarget(target) == null){
                return false;
            }
        }
        return true;
    }


    public Task updateTaskStatus(String taskName, TaskStatus newStatus) throws Exception {
        Task task = null;
        if(!isTaskExistInSystem(taskName)){
            throw new Exception(ExceptionMessages.TASK + taskName + ExceptionMessages.NOT_EXIST);
        }
        if(this.tasksInSystem.get(taskName).updateTaskStatus(newStatus)){
            task = getTasksInSystem().get(taskName);
        }
        return task;
    }

    public Task workerRegistrationToTask(String taskName, String workerName) throws Exception {
        if(!isTaskExistInSystem(taskName)){
            throw new Exception(ExceptionMessages.TASK + taskName + ExceptionMessages.NOT_EXIST);
        }
        this.tasksInSystem.get(taskName).addWorkerToTask(workerName);
        return this.tasksInSystem.get(taskName);
    }

    public synchronized Set<TargetDTO> getTaskTargetForExecution(Collection<Task> workerTasks, int requiredNumOfTargets) throws Exception {
        //TODO: check if all the targets in the collection are exist in the system

        Set<TargetDTO> targetsForWorker = new HashSet<>();
        boolean isFinished = false;
        int numOfReadyForRunningTargets = 0;

        while (!isFinished){
            for(Task currTask : workerTasks){
                //writeTargetRunResultToFile(getTaskTypeByName(currTask.getTaskName()), currTask);
                if(targetsForWorker.size() < requiredNumOfTargets && !currTask.getSortedTargets().isEmpty()){
                    TargetDTO targetDTO = currTask.getTargetReadyForRunning(this.workingDirectory);
                    if(targetDTO != null){
                        targetsForWorker.add(targetDTO);
                    }
                }
            }
            if(targetsForWorker.size() == requiredNumOfTargets ||
                    numOfReadyForRunningTargets == targetsForWorker.size()){
                isFinished = true;
            }
            else if(numOfReadyForRunningTargets < targetsForWorker.size()){
                numOfReadyForRunningTargets = targetsForWorker.size();
            }
        }
        return targetsForWorker;
    }

    public TaskParamsDTO toTaskParamsDTO(Task task){
        TaskParamsDTO taskParamsDTO = null;
        if(task instanceof SimulationTask){
            SimulationTask simulationTask = (SimulationTask) task;
            taskParamsDTO = simulationTask.createSimulationTaskParamsDTO();
        }
        else if(task instanceof CompilationTask){
            CompilationTask compilationTask = (CompilationTask) task;
            taskParamsDTO = compilationTask.createCompilationTaskParamsDTO();
        }
        return taskParamsDTO;
    }

    //TODO: check if target is finished- if so, write the results to a file
    public int updateTargetRunResult(ExecutionTargetDTO executionTargetDTO) throws Exception {
        int priceForTarget = 0;
        if(this.tasksInSystem.containsKey(executionTargetDTO.getTaskName())){
            Task task = this.tasksInSystem.get(executionTargetDTO.getTaskName());
            Graph taskGraph = task.getGraph();
            Target taskTarget = taskGraph.getTarget(executionTargetDTO.getTargetName());
            taskTarget.updateTarget(executionTargetDTO);
            priceForTarget = task.updateTargetsRunResult(taskTarget);
            writeTargetRunResultToFile(this.getTaskTypeByName(task.getTaskName()),
                    taskTarget, task, this.workingDirectory);
        }
        else{
            throw new Exception(ExceptionMessages.TASK + executionTargetDTO.getTaskName() +
                    ExceptionMessages.NOT_EXIST);
        }
        return priceForTarget;
    }

    public static void writeTargetRunResultToFile(TaskType taskTypeByName,Target target,
                                                  Task task, String workingDirectory) {
        if(target.getRunStatus().equals(RunStatus.FINISHED) || target.getRunStatus().equals(RunStatus.SKIPPED)){
            new Thread(()->openDirectoryAndFiles(taskTypeByName, target, task.getTaskName(), workingDirectory)).start();
        }
    }


    public static void openDirectoryAndFiles(TaskType taskType, Target target, String taskName, String workingDirectory) {
        StringBuffer stringBuffer = new StringBuffer();
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        simpleDateFormat.format(now, stringBuffer, new FieldPosition(0));
        String path = workingDirectory + "\\" + taskType.getTaskType() + "-" +
                simpleDateFormat.format(now);
        File directory = new File(path);
        if(!directory.exists()){
            directory.mkdirs();
        }
        writeToFile(path, target, taskName);
    }


    public static void writeToFile(String path, Target target, String taskName) {
        Writer out = null;
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(path + "\\" + target.getName() + ".log")));
            out.write(target.getRunTaskLog(taskName));
        }
        catch (IOException e) {
        e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
