package dto;

import general_enums.RunResults;
import general_enums.RunStatus;

public class ExecutionTargetDTO {
    private RunResults runResults;
    private RunStatus runStatus;
    private String taskLog;

    public ExecutionTargetDTO(RunResults runResults, RunStatus runStatus, String taskLog) {
        this.runResults = runResults;
        this.runStatus = runStatus;
        this.taskLog = taskLog;
    }
}
