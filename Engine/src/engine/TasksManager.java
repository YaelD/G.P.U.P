package engine;

import dto.TargetDTO;
import general_enums.TaskStatus;
import general_enums.TaskType;
import task.Task;

import java.util.*;

public class TasksManager {

    private Map<String, Task> tasksInSystem = new HashMap<>();

    public Map<String, Task> getTasksInSystem() {
        return tasksInSystem;
    }

    public synchronized void addTaskToSystem(Task task) throws Exception {
        if (isTaskExistInSystem(task.getTaskName())) {
            throw new Exception(ExceptionMessages.TASK + task.getTaskName() + ExceptionMessages.ALREADY_EXIST);
        }
        tasksInSystem.put(task.getTaskName(), task);
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

    public Set<TargetDTO> getTaskTargetForExecution(Collection<Task> workerTasks, int requiredNumOfTargets){
        Set<TargetDTO> targetsForWorker = new HashSet<>();
        boolean isFinished = false;
        int numOfReadyForRunningTargets = 0;

        while (!isFinished){

            for(Task currTask : workerTasks){
                if(targetsForWorker.size() < requiredNumOfTargets){
                    TargetDTO targetDTO = currTask.getTargetReadyForRunning();
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
}
