package dto;

import general_enums.RunResults;
import general_enums.RunStatus;

import java.time.LocalTime;
import java.util.Locale;

public class ExecutionTargetDTO {
    private RunResults runResults;
    private RunStatus runStatus;
    private String taskLog;
    private String taskName;
    private String targetName;
    private LocalTime startProcessTime;



    public ExecutionTargetDTO(RunResults runResults, RunStatus runStatus, String taskLog, String taskName, String targetName, LocalTime startProcessTime) {
        this.runResults = runResults;
        this.runStatus = runStatus;
        this.taskLog = taskLog;
        this.taskName = taskName;
        this.targetName = targetName;
        this.startProcessTime =startProcessTime;
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

    public LocalTime getStartProcessTime() {
        return startProcessTime;
    }
}
