package dto;

//import target.*;

import general_enums.PlaceInGraph;
import general_enums.RunResults;
import general_enums.RunStatus;

import java.util.*;

public class TargetDTO {


    private String name;
    private String taskName;
    private PlaceInGraph place;
    private Set<String> requiredFor = new HashSet<>();
    private Set<String> dependsOn = new HashSet<>();
    private String info;
    private RunResults runResult;
    private RunStatus runStatus;
    private Set<String> totalRequiredFor = new HashSet<>();
    private Set<String> totalDependsOn = new HashSet<>();
    private Set<String> targetsThatCanBeRun = new HashSet<>();
    private Set<String> skippedFathers = new HashSet<>();
    private String taskLog;



    public TargetDTO(String name, PlaceInGraph place, Set<String> requiredFor,
                     Set<String> dependsOn, String info, Set<String> totalRequiredFor,
                     Set<String> totalDependsOn, String taskRunResult,
                     String taskName, RunStatus runStatus, RunResults runResult) {
        this.name = name;
        this.place = place;
        this.requiredFor = requiredFor;
        this.dependsOn = dependsOn;
        this.info = info;
        this.totalRequiredFor = totalRequiredFor;
        this.totalDependsOn = totalDependsOn;
        this.taskLog = taskRunResult;
        this.runStatus = runStatus;
        this.runResult = runResult;
        this.taskName = taskName;
    }


    public String getTaskName() {
        return taskName;
    }

    public String getTaskLog() {
        return taskLog;
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

    public RunStatus getRunStatus() {
        return runStatus;
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


}
