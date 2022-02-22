package dto;

import general_enums.RunResults;
import general_enums.RunStatus;

public class ExecutionTargetDTO {
    private RunResults runResults;
    private RunStatus runStatus;
    private String taskLog;
    private String taskName;

    public ExecutionTargetDTO(RunResults runResults, RunStatus runStatus, String taskLog, String taskName) {
        this.runResults = runResults;
        this.runStatus = runStatus;
        this.taskLog = taskLog;
        this.taskName = taskName;
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
}
