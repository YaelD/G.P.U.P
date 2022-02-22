package worker_engine;

import dto.ExecutionTargetDTO;
import dto.TargetDTO;
import general_enums.RunResults;
import general_enums.RunStatus;

import java.time.Duration;
import java.time.LocalTime;

public class ExecutionTarget {

    private RunResults runResult;
    private RunStatus runStatus;
    private String taskLog;
    private String info;

    public ExecutionTarget(TargetDTO targetDTO){
        this.runResult = targetDTO.getRunResult();
        this.runStatus = targetDTO.getRunStatus();
        this.taskLog = "";
        this.info = targetDTO.getInfo();
    }



    public ExecutionTargetDTO makeDTO(){
        return new ExecutionTargetDTO(this.runResult, this.runStatus, this.taskLog);
    }

    public void setSpecificTaskLog(String str){
        taskLog += str + "\n";
    }

    public void setRunResult(RunResults runResult) {
        this.runResult = runResult;
    }

    public void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }

    public String getInfo() {
        return info;
    }
}
