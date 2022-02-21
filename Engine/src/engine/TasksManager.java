package engine;

import general_enums.TaskStatus;
import general_enums.TaskType;
import task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
}
