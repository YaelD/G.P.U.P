package dto;

import general_enums.TaskStatus;

public class TaskDTO {

    private String taskName;
    private String creatorName;
    private String graphName;
    private int totalNumOfTargets;
    private int numOfLeaves;
    private int numOfMiddles;
    private int numOfRoots;
    private int numOfIndependents;
    private int taskPricePerTarget;
    private int numOfRegisteredWorkers;
    private TaskStatus taskStatus;
    

    public TaskDTO(String taskName, String creatorName, String graphName, int totalNumOfTargets,
                   int numOfLeaves, int numOfMiddles, int numOfRoots, int numOfIndependents,
                   int taskPricePerTarget, int numOfRegisteredWorkers, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.creatorName = creatorName;
        this.graphName = graphName;
        this.totalNumOfTargets = totalNumOfTargets;
        this.numOfLeaves = numOfLeaves;
        this.numOfMiddles = numOfMiddles;
        this.numOfRoots = numOfRoots;
        this.numOfIndependents = numOfIndependents;
        this.taskPricePerTarget = taskPricePerTarget;
        this.numOfRegisteredWorkers = numOfRegisteredWorkers;
        this.taskStatus = taskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getGraphName() {
        return graphName;
    }

    public int getTotalNumOfTargets() {
        return totalNumOfTargets;
    }

    public int getNumOfLeaves() {
        return numOfLeaves;
    }

    public int getNumOfMiddles() {
        return numOfMiddles;
    }

    public int getNumOfRoots() {
        return numOfRoots;
    }

    public int getNumOfIndependents() {
        return numOfIndependents;
    }

    public int getTaskPricePerTarget() {
        return taskPricePerTarget;
    }

    public int getNumOfRegisteredWorkers() {
        return numOfRegisteredWorkers;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
}
