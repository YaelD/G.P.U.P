package worker_engine;

import dto.ExecutionTargetDTO;
import dto.TargetDTO;
import general_enums.RunResults;
import general_enums.RunStatus;
import general_enums.TaskType;

import java.time.Duration;
import java.time.LocalTime;

public class ExecutionTarget {

    private String taskName;
    private RunResults runResult;
    private RunStatus runStatus;
    private String taskLog;
    private String info;
    private String targetName;
    private LocalTime startProcessTime;
    private int targetPrice;
    private TaskType taskType;

    public ExecutionTarget(TargetDTO targetDTO){
        this.taskName = targetDTO.getTaskName();
        this.runResult = targetDTO.getRunResult();
        this.runStatus = targetDTO.getRunStatus();
        this.taskLog = "";
        this.info = targetDTO.getInfo();
        this.targetName = targetDTO.getName();
        this.targetPrice = 0;
        this.taskType = WorkerEngine.getInstance().getRegisteredTasksParams().get(taskName).getTaskType();
    }

    public synchronized ExecutionTargetDTO makeDTO(){
        return new ExecutionTargetDTO(this.runResult, this.runStatus, this.taskLog, this.taskName, this.targetName, this.startProcessTime);
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

    public void setStartProcessTime(LocalTime startProcessTime) {
        this.startProcessTime = startProcessTime;
    }

    public int getTargetPrice() {
        return targetPrice;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTargetPrice(int targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getTaskName() {
        return taskName;
    }

    public RunResults getRunResult() {
        return runResult;
    }

    public RunStatus getRunStatus() {
        return runStatus;
    }

    public String getTaskLog() {
        return taskLog;
    }

    public String getTargetName() {
        return targetName;
    }

    public LocalTime getStartProcessTime() {
        return startProcessTime;
    }

    public String getInfo() {
        return info;
    }
}
