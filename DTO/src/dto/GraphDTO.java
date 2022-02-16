package dto;


import graph.Graph;
import target.PlaceInGraph;
import target.Target;
import task.TaskType;

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

    //    public GraphDTO(int numOfTargets, Map<String, TargetDTO> targets, String name){
////        this.numOfTargets = numOfTargets;
//        this.targets = targets;
//        this.name = name;
//    }


    public GraphDTO(Graph graph) {
        this.creatorName = graph.getCreatorName();
        Map<TaskType, Integer> prices = graph.getTaskPricePerTarget();
        this.priceOfCompilationTask= prices.containsKey(TaskType.COMPILATION_TASK) ?
                prices.get(TaskType.COMPILATION_TASK): 0;
        this.priceOfSimulationTask= prices.containsKey(TaskType.SIMULATION_TASK) ?
                prices.get(TaskType.SIMULATION_TASK): 0;
        this.name = graph.getName();
        this.totalNumOfTargets = graph.getTargets().size();
        this.numOfLeaves = 0;
        this.numOfIndependents = 0;
        this.numOfMiddles = 0;
        this.numOfRoots = 0;
        for(Target target: graph.getTargets()) {
            switch(target.getPlace()){
                case INDEPENDENT:
                    numOfIndependents++;
                  break;
                case ROOT:
                    numOfRoots++;
                    break;
                case MIDDLE:
                    numOfMiddles++;
                    break;
                case LEAF:
                    numOfLeaves++;
                    break;
            }
            this.targets.put(target.getName(), new TargetDTO(target));
        }
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

    //    public GraphDTO(Graph graph, long runTime) {
//        this(graph);
//        this.runTime = runTime;
//    }

    public String getName() {
        return name;
    }


    public Map<String, TargetDTO> getTargets() {
        return targets;
    }

    public long getRunTime() {
        return runTime;
    }

    public int getNumOfTargetsInPlace(PlaceInGraph place){
        int counter = 0;
        for(TargetDTO target: targets.values()){
            if(target.getPlace() == place)
                counter++;
        }
        return counter;
    }

    public int getNumOfTargetsRunResult(RunResults runResult){
        int counter = 0;
        for(TargetDTO target: targets.values()){
            if(target.getRunResult() == runResult)
                counter++;
        }
        return counter;
    }

}
