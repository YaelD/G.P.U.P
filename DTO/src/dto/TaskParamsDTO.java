package dto;

import general_enums.RunType;
import general_enums.TaskType;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskParamsDTO {

    protected String creatorName;
    protected TaskType taskType;
    protected RunType runType;
    protected List<String> targets = new ArrayList<>();
    protected String taskName;
    protected String graphName;
    protected int totalTaskPrice;

    public TaskParamsDTO(String creatorName, TaskType taskType, RunType runType, List<String> targets, String taskName,
                         String graphName, int totalTaskPrice) {
        this.creatorName = creatorName;
        this.taskType = taskType;
        this.runType = runType;
        this.targets = targets;
        this.taskName = taskName;
        this.graphName = graphName;
        this.totalTaskPrice = totalTaskPrice;
    }
}
