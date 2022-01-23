package dto;

import graph.SerialSet;
import target.PlaceInGraph;
import target.RunResults;
import target.RunStatus;
import target.Target;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class TargetDTO {

    private String name;
    private PlaceInGraph place;
    private Set<String> requiredFor = new HashSet<>();
    private Set<String> dependsOn = new HashSet<>();
    private String info;
    private RunResults runResult;
    private RunStatus runStatus;
    private long runTime;
    private Set<String> totalRequiredFor = new HashSet<>();
    private Set<String> totalDependsOn = new HashSet<>();
    private Set<String> targetsThatCanBeRun = new HashSet<>();
    private Set<String> skippedFathers = new HashSet<>();
    private int totalNumOfSerialSets;
    private String taskRunResult = "";
    private Set<String> serialSetNames = new HashSet<>();; //all serial set names that the current target belongs to them - needed always during the running of the task.
    private LocalTime startingProcessTime = null; //the time that the target began the process- needed when the run status is "in process"
    private LocalTime endingProcessTime = null; //the time that the target ended the process
    private LocalTime startWaitingTime = null; //the time that the target begins waiting to start the process- needed when the run status is "waiting", for calculating the duration of the waiting and showing it to the user
    private Set<String> failedChildTargets = new HashSet<>(); //all the names of the target that failed and causes to this curr target to be skipped- needed when the run status is "skipped"
    private Set<String> waitForThisTargetsToBeFinished = new HashSet<>(); //all the names of the targets that this target waiting for them to be finished- needed when the run status is "frozen"
    private String runningTargetStatus = null;

    //CompilationParams
    private String compilationRunResult;
    private String compilationFileName;
    private String compilerOperatingLine;
    private LocalTime startingCompileTime;
    private LocalTime endingCompileTime;



    public TargetDTO(Target target) {
        this.startingCompileTime = target.getStartingCompileTime();
        this.endingCompileTime = target.getEndingCompileTime();
        this.compilationRunResult = target.getCompilationRunResult();
        this.compilationFileName = target.getCompilationFileName();
        this.compilerOperatingLine = target.getCompilerOperatingLine();
        this.runStatus = RunStatus.FROZEN;
        this.name = target.getName();
        this.place = target.getPlace();
        this.info = target.getInfo();
        this.runResult = target.getRunResult();
        this.runStatus = target.getRunStatus();
        this.runTime = target.getRunningTime();
        for(Target currTarget: target.getRequiredFor())
        {
            this.requiredFor.add(currTarget.getName());
        }
        for (Target currTarget: target.getDependsOn())
        {
            this.dependsOn.add(currTarget.getName());
        }

        target.getDependsOnAncestors(this.totalDependsOn);
        target.getRequiredForAncestors(this.totalRequiredFor);
        this.totalNumOfSerialSets = target.getSerialSetsContainer().getSerialSetList().size();
        if(this.totalNumOfSerialSets != 0){
            for(SerialSet serialSet : target.getSerialSetsContainer().getSerialSetList()){
                this.serialSetNames.add(serialSet.getName());
            }
        }
        this.startingProcessTime = target.getStartingProcessTime();
        this.startWaitingTime = target.getStartWaitingTime();
        this.failedChildTargets = target.getFailedChildTargets();
        this.waitForThisTargetsToBeFinished = target.getWaitForThisTargetsToBeFinished();
        this.endingProcessTime = target.getEndingProcessTime();
    }

    public TargetDTO(Target target, String taskRunResult) {
        this(target);
        this.taskRunResult = taskRunResult;
    }

    public Set<String> getSkippedFathers() {
        return skippedFathers;
    }

    public String getName() {
        return name;
    }

    public PlaceInGraph getPlace() {
        return place;
    }

    public Set<String> getRequiredFor() {
        return requiredFor;
    }

    public Set<String> getDependsOn() {
        return dependsOn;
    }

    public String getInfo() {
        return info;
    }

    public RunResults getRunResult() {
        return runResult;
    }

    public long getRunTime() {
        return runTime;
    }

    public RunStatus getRunStatus() {
        return runStatus;
    }

    public Set<String> getSerialSetNames() {
        return serialSetNames;
    }

    public LocalTime getStartingProcessTime() {
        return startingProcessTime;
    }

    public LocalTime getEndingProcessTime() {
        return endingProcessTime;
    }

    public LocalTime getStartWaitingTime() {
        return startWaitingTime;
    }

    public Set<String> getFailedChildTargets() {
        return failedChildTargets;
    }

    public Set<String> getWaitForThisTargetsToBeFinished() {
        return waitForThisTargetsToBeFinished;
    }

//    public LocalTime getStartingTime() {
//        return startingTime;
//    }
//
//    public void setStartingTime(LocalTime startingTime) {
//        this.startingTime = startingTime;
//    }
//
//    public LocalTime getEndingTime() {
//        return endingTime;
//    }
//
//    public void setEndingTime(LocalTime endingTime) {
//        this.endingTime = endingTime;
//    }

    public Set<String> getTargetsThatCanBeRun() {
        return targetsThatCanBeRun;
    }

    public Set<String> getTotalRequiredFor() {
        return totalRequiredFor;
    }

    public Set<String> getTotalDependsOn() {
        return totalDependsOn;
    }

    public int getTotalNumOfSerialSets() {
        return totalNumOfSerialSets;
    }

//    public void setTaskRunResult(String taskRunResult) {
//        this.taskRunResult = taskRunResult;
//    }

    public String getTaskRunResult() {
        return taskRunResult;
    }

    public String getRunningTargetStatus() {
        return runningTargetStatus;
    }

    public void updateRunningTargetStatus(RunStatus runStatus){
        this.runningTargetStatus = "";
        this.runningTargetStatus += "Target name: "+ this.name;
        this.runningTargetStatus += "\nPlace in graph: " +this.place;
        this.runningTargetStatus += "\nSerialSets: ";
        if(!this.serialSetNames.isEmpty()){
            this.runningTargetStatus += this.serialSetNames.toString()+"\n";
        }
        else{
            this.runningTargetStatus += "this target does not belong to any serial set";
        }
        this.runningTargetStatus += "\nRun status: ";
        switch (runStatus){
            case WAITING:
                if(this.startWaitingTime != null){
                    this.runningTargetStatus += "waiting \nWaiting time: " +
                            Duration.between(this.startWaitingTime, LocalTime.now()).toMillis() + " ms";
                }
                break;
            case FINISHED:
                this.runningTargetStatus = "";
                break;
            case FROZEN:
                this.runningTargetStatus += "frozen \nWaiting to the targets: "+
                        this.waitForThisTargetsToBeFinished.toString()+ " to finish their running.";
                break;
            case IN_PROCESS:
                this.runningTargetStatus += "in process \nProcessing time: "+
                        Duration.between(this.startingProcessTime, LocalTime.now()).toMillis() + " ms";
                break;
            case SKIPPED:
                this.runningTargetStatus += "skipped \n Skipped because of the failure of targets: "+
                        this.failedChildTargets.toString();
                break;
        }
        if(!this.compilationFileName.isEmpty()){
            runningTargetStatus += "\nCompilation file name: " + this.compilationFileName  + "\n";
        }
        if(!this.compilerOperatingLine.isEmpty()){
            runningTargetStatus += this.compilerOperatingLine + "\n";
        }
        if(this.startingCompileTime != null && this.endingCompileTime != null){
            runningTargetStatus += "Compiler Running time: " + Duration.between(this.startingCompileTime, this.endingCompileTime).toMillis() + "ms \n";
        }
        if(!this.compilationRunResult.isEmpty()){
            runningTargetStatus += "Compilation run result: " + this.compilationRunResult + "\n";
        }

    }

}
