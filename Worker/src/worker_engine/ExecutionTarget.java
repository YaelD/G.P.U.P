package worker_engine;

import dto.ExecutionTargetDTO;
import dto.TargetDTO;
import general_enums.RunResults;
import general_enums.RunStatus;

import java.time.Duration;
import java.time.LocalTime;

public class ExecutionTarget {

    private String taskName;
    private RunResults runResult;
    private RunStatus runStatus;
    private String taskLog;
    private String info;

    public ExecutionTarget(TargetDTO targetDTO){
        this.taskName = targetDTO.getTaskName();
        this.runResult = targetDTO.getRunResult();
        this.runStatus = targetDTO.getRunStatus();
        this.taskLog = "";
        this.info = targetDTO.getInfo();

    }

    public synchronized ExecutionTargetDTO makeDTO(){
        return new ExecutionTargetDTO(this.runResult, this.runStatus, this.taskLog, this.taskName);
    }

    public synchronized void setSpecificTaskLog(String str){
        taskLog += str + "\n";
    }

    public synchronized void setRunResult(RunResults runResult) {
        this.runResult = runResult;
    }

    public synchronized void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }

    public String getInfo() {
        return info;
    }
}
