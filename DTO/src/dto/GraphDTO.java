package dto;



import general_enums.RunResults;
import general_enums.RunStatus;

import java.util.HashMap;
import java.util.Map;

public class GraphDTO {

//    private int numOfTargets;
    private String name;
    private Map<String, TargetDTO> targets = new HashMap<>();
    private long runTime;
    private String creatorName;
    private int totalNumOfTargets;
    private int numOfLeaves;
    private int numOfRoots;
    private int numOfIndependents;
    private int numOfMiddles;
    private int priceOfSimulationTask;
    private int priceOfCompilationTask;

    public String getCreatorName() {
        return creatorName;
    }

    public int getPriceOfSimulationTask() {
        return priceOfSimulationTask;
    }

    public int getPriceOfCompilationTask() {
        return priceOfCompilationTask;
    }


    public GraphDTO(String name, Map<String, TargetDTO> targets, String creatorName, int totalNumOfTargets,
                    int numOfLeaves, int numOfRoots, int numOfIndependents,
                    int numOfMiddles, int priceOfSimulationTask,
                    int priceOfCompilationTask) {
        this.name = name;
        this.targets = targets;
        this.creatorName = creatorName;
        this.totalNumOfTargets = totalNumOfTargets;
        this.numOfLeaves = numOfLeaves;
        this.numOfRoots = numOfRoots;
        this.numOfIndependents = numOfIndependents;
        this.numOfMiddles = numOfMiddles;
        this.priceOfSimulationTask = priceOfSimulationTask;
        this.priceOfCompilationTask = priceOfCompilationTask;
    }


    public int getTotalNumOfTargets() {
        return totalNumOfTargets;
    }

    public int getNumOfLeaves() {
        return numOfLeaves;
    }

    public int getNumOfRoots() {
        return numOfRoots;
    }

    public int getNumOfIndependents() {
        return numOfIndependents;
    }

    public int getNumOfMiddles() {
        return numOfMiddles;
    }


    public String getName() {
        return name;
    }


    public Map<String, TargetDTO> getTargets() {
        return targets;
    }

    public int getNumOfTargetsRunResult(RunResults runResult){
        int counter = 0;
        for(TargetDTO target: targets.values()){
            if(target.getRunResult() == runResult)
                counter++;
        }
        return counter;
    }

    public int getNumOfTargetsByRunStatus(RunStatus runStatus){
        int counter = 0;
        for(TargetDTO targetDTO: targets.values()){
            if(targetDTO.getRunStatus().equals(runStatus)){
                counter++;
            }
        }
        return counter;
    }

}
