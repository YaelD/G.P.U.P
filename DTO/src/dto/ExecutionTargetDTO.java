package dto;

import general_enums.RunResults;
import general_enums.RunStatus;

public class ExecutionTargetDTO {
    private RunResults runResults;
    private RunStatus runStatus;
    private String taskLog;
    private String taskName;
    private String targetName;



    public ExecutionTargetDTO(RunResults runResults, RunStatus runStatus, String taskLog, String taskName, String targetName) {
        this.runResults = runResults;
        this.runStatus = runStatus;
        this.taskLog = taskLog;
        this.taskName = taskName;
        this.targetName = targetName;
    }

    public RunResults getRunResults() {
        return runResults;
    }

    public RunStatus getRunStatus() {
        return runStatus;
    }

    public String getTaskLog() {
        return taskLog;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTargetName() {
        return targetName;
    }
}
