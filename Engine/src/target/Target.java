package target;

import general_enums.PlaceInGraph;
import dto.TargetDTO;
import general_enums.Dependency;
import general_enums.RunResults;
import general_enums.RunStatus;
import schema.generated.GPUPTarget;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class Target implements Cloneable {

    private String name;
    private PlaceInGraph place;
    private Set<Target> requiredFor = new HashSet<>(); //This target.Target requiredFor the Set's Targets
    private Set<Target> dependsOn = new HashSet<>();// This target.Target is dependsOn the Set's targets
    private String info;
    private RunResults runResult;

    private RunStatus runStatus;
    private LocalTime startingProcessTime = null;
    private LocalTime endingProcessTime = null;
    private LocalTime startWaitingTime = null;
    private Set<String> failedChildTargets = new HashSet<>();
    private Set<String> waitForThisTargetsToBeFinished = new HashSet<>();
    private Set<String> targetsThatCanBeRun = new HashSet<>();
    private Set<String> skippedFathers = new HashSet<>();

    public void setStartingProcessTime(LocalTime startingProcessTime) {
        this.startingProcessTime = startingProcessTime;
    }

    private String taskSpecificLogs;


    public Target(GPUPTarget target) {
        this.name = target.getName();
        this.info = target.getGPUPUserData();
        this.taskSpecificLogs = "";
    }



    public String getName() {
        return name;
    }

    public PlaceInGraph getPlace() {
        return place;
    }

    public Set<Target> getRequiredFor() {
        return requiredFor;
    }

    public Set<Target> getDependsOn() {
        return dependsOn;
    }

    public String getInfo() {
        return info;
    }

    public void setPlace(PlaceInGraph place) {
        this.place = place;
    }

    public Set<Target> getDependencies(Dependency dependency){
        if(dependency.equals(Dependency.REQUIRED_FOR)){
            return this.getRequiredFor();
        }
        else{
            return this.getDependsOn();
        }
    }

    public RunResults getRunResult() {
        return runResult;
    }

    public synchronized void setRunResult(RunResults runResult) {
        this.runResult = runResult;
    }


    public RunStatus getRunStatus() {
        return runStatus;
    }

    public synchronized void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
        notifyAll();
    }

    public synchronized void setStartWaitingTime(LocalTime startWaitingTime) {
        this.startWaitingTime = startWaitingTime;
    }

    public Set<String> getFailedChildTargets() {
        return failedChildTargets;
    }

    public Set<String> getWaitForThisTargetsToBeFinished() {
        return waitForThisTargetsToBeFinished;
    }

    public void setRequiredFor(Set<Target> requiredFor) {
        this.requiredFor = requiredFor;
    }

    public void setDependsOn(Set<Target> dependsOn) {
        this.dependsOn = dependsOn;
    }


    public void updateParentsStatus(Set<String> skippedFathers, String sourceTargetName) {
        if(this.getRequiredFor().isEmpty()){
            return;
        }
        else{
            for(Target currTarget : this.getRequiredFor()){
                currTarget.setRunStatus(RunStatus.SKIPPED);
                currTarget.setRunResult(RunResults.SKIPPED);
                currTarget.getFailedChildTargets().add(sourceTargetName);
                skippedFathers.add(currTarget.getName());
                currTarget.updateParentsStatus(skippedFathers, sourceTargetName);
            }
        }
    }

    public void getRequiredForAncestors(Set<String> targetsSet){
        for(Target target : this.requiredFor){
            if(!targetsSet.contains(target.name)){
                targetsSet.add(target.name);
                target.getRequiredForAncestors(targetsSet);
            }
        }
    }

    public void getDependsOnAncestors(Set<String> targetsSet){
        for(Target target : this.dependsOn){
            if(!targetsSet.contains(target.name)){
                targetsSet.add(target.name);
                target.getDependsOnAncestors(targetsSet);
            }
        }
    }

    public void updateWaitForTheseTargetsToBeFinished(){
        for(Target target : this.dependsOn){
            this.waitForThisTargetsToBeFinished.add(target.getName());
        }
    }

    @Override
    public String toString() {
        return "target.Target{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return Objects.equals(name, target.name) && place == target.place && Objects.equals(requiredFor, target.requiredFor) && Objects.equals(dependsOn, target.dependsOn) && Objects.equals(info, target.info);
    }

    @Override
    public Target clone()  {
        try {
            Target newTarget = (Target) super.clone();
            return newTarget;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TargetDTO makeDTO(){
        Set<String> requiredForNames = new HashSet<>();
        for(Target target: this.requiredFor){
            requiredForNames.add(target.name);
        }
        Set<String> dependsOnName = new HashSet<>();
        for(Target target: this.dependsOn){
            dependsOnName.add(target.name);
        }

        Set<String> totalRequiredForNames = new HashSet<>();
        Set<String> totalDependsOnNames = new HashSet<>();
        this.getRequiredForAncestors(totalRequiredForNames);
        this.getDependsOnAncestors(totalDependsOnNames);
        return new TargetDTO(this.name, this.place, requiredForNames, dependsOnName,this.info, totalRequiredForNames, totalDependsOnNames,
                this.getRunTaskLog(), this.runStatus, this.runResult);
    }

    public static Target createTargetFromTargetDTO(TargetDTO targetDTO){
        //TODO: create the function
        return null;
    }

    public synchronized void updateTarget(TargetDTO targetDTO){
        this.runStatus = targetDTO.getRunStatus();
        this.runResult = targetDTO.getRunResult();

    }



    public synchronized void setTaskSpecificLogs(String taskSpecificLogs){
        this.taskSpecificLogs += taskSpecificLogs + "\n";
    }

    public synchronized String getRunTaskLog() {
        if(this.runStatus == null){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("Target: " + this.name);
        stringBuilder.append(this.info!= null ? "\nInfo: " + this.info : "");
        stringBuilder.append("\nPlace: " + this.place.name());
        stringBuilder.append("\nRun status: " + this.runStatus);
        stringBuilder.append("\n");
        switch (this.runStatus){
            case FROZEN:
                stringBuilder.append("Waiting for targets: " + this.waitForThisTargetsToBeFinished.toString() + " to finish their running.") ;
                break;
            case WAITING:
                stringBuilder.append("Waiting time: " + Duration.between(this.startWaitingTime, LocalTime.now()).toMillis() + " ms");
                break;
            case SKIPPED:
                stringBuilder.append("Skipped because of: " + this.failedChildTargets.toString());
                break;
            case IN_PROCESS:
                stringBuilder.append("Process time: " + Duration.between(this.startingProcessTime, LocalTime.now()).toMillis() + " millisecond");
                break;
            case FINISHED:
                stringBuilder.append("Run result: " + this.runResult.name());
                stringBuilder.append("Opened Targets to run: " + targetsThatCanBeRun.toString());
                if(this.runResult.equals(RunResults.FAILURE)){
                    stringBuilder.append("Skipped fathers: " + this.skippedFathers.toString());
                }
        }
        stringBuilder.append(this.taskSpecificLogs);
        return stringBuilder.toString();


    }


}
