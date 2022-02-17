package dto;

import general_enums.RunType;
import general_enums.TaskType;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskParamsDTO {
    TaskType taskType;
    RunType runType;
    List<String> targets = new ArrayList<>();

    public TaskParamsDTO(TaskType taskType, RunType runType, List<String> targets) {
        this.taskType = taskType;
        this.runType = runType;
        this.targets = targets;
    }
}
