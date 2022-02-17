package dto;

import general_enums.TaskStatus;
import general_enums.TaskType;

public class TaskDTO {

    private String taskName;
    private String creatorName;
    private int taskTotalPrice;
    private int numOfRegisteredWorkers;
    private TaskStatus taskStatus;
    private GraphDTO graphDTO;
    private TaskType taskType;


    public TaskDTO(String taskName, String creatorName, int taskTotalPrice, int numOfRegisteredWorkers,
                   TaskStatus taskStatus, GraphDTO graphDTO, TaskType taskType) {
        this.taskName = taskName;
        this.creatorName = creatorName;
        this.taskTotalPrice = taskTotalPrice;
        this.numOfRegisteredWorkers = numOfRegisteredWorkers;
        this.taskStatus = taskStatus;
        this.graphDTO = graphDTO;
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public int getTaskTotalPrice() {
        return taskTotalPrice;
    }

    public int getNumOfRegisteredWorkers() {
        return numOfRegisteredWorkers;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public GraphDTO getGraphDTO() {
        return graphDTO;
    }

    public TaskType getTaskType() {
        return taskType;
    }
}
