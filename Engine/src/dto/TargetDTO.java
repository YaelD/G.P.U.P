package dto;

import target.PlaceInGraph;
import target.RunResults;
import target.Target;

import java.time.LocalTime;
import java.util.*;

public class TargetDTO {

    private String name;
    private PlaceInGraph place;
    private Set<String> requiredFor = new HashSet<>();
    private Set<String> dependsOn = new HashSet<>();
    private String info = null;
    private RunResults runResult;
    private long runTime;
    private LocalTime startingTime = null;
    private LocalTime endingTime = null;
    private Set<String> totalRequiredFor = new HashSet<>();
    private Set<String> totalDependsOn = new HashSet<>();
    private Set<String> targetsThatCanBeRun = new HashSet<>();
    private Set<String> skippedFathers = new HashSet<>();
    private int totalNumOfSerialSets;


    public TargetDTO(Target target) {
        this.name = target.getName();
        this.place = target.getPlace();
        this.info = target.getInfo();
        this.runResult = target.getRunResult();
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

    }

    public TargetDTO(Target target, LocalTime startingTime, LocalTime endingTime) {
        this(target);
        this.startingTime = startingTime;
        this.endingTime = endingTime;
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

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
    }

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
}
