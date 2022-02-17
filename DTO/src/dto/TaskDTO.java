package dto;

import general_enums.TaskStatus;

public class TaskDTO {

    private String taskName;
    private String creatorName;
    private int taskTotalPrice;
    private int numOfRegisteredWorkers;
    private TaskStatus taskStatus;
    private GraphDTO graphDTO;


    public TaskDTO(String taskName, String creatorName, int taskTotalPrice,
                   int numOfRegisteredWorkers, TaskStatus taskStatus, GraphDTO graphDTO) {
        this.taskName = taskName;
        this.creatorName = creatorName;
        this.taskTotalPrice = taskTotalPrice;
        this.numOfRegisteredWorkers = numOfRegisteredWorkers;
        this.taskStatus = taskStatus;
        this.graphDTO = graphDTO;
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
}
