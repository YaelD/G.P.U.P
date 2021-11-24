package task;

import dto.GraphDTO;
import dto.TargetDTO;
import graph.Graph;
import target.RunResults;
import target.RunStatus;
import target.Target;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;



public abstract class Task {

    protected Graph graph;

    protected Task(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public GraphDTO executeTaskOnGraph(List<Consumer<TargetDTO>> outputConsumers){
        TargetDTO targetResult;
        LocalTime startTime, endTime;
        Map<String, Integer> targetsInDegree = getTargetsInDegree();
        List<Target> sourceTargets = initSourceQueue(this.graph.getTargets());
        startTime = LocalTime.now();
        while(!sourceTargets.isEmpty()){
            Target currTarget = sourceTargets.remove(0);
            if(!currTarget.getRunStatus().equals(RunStatus.SKIPPED)) {
                targetResult = executeTaskOnTarget(currTarget);
            }
            else{
                targetResult = new TargetDTO(currTarget);
            }
            if(currTarget.getRunStatus().equals(RunStatus.FINISHED)){
                if(currTarget.getRunResult().equals(RunResults.FAILURE)){
                    updateParentsStatus(currTarget.getRequiredFor(), targetResult.getSkippedFathers());
                }
            }
            updateNeighborTargets(currTarget,sourceTargets, targetsInDegree,targetResult);
            outputTargetResult(outputConsumers, targetResult);
        }
        //checkingForCycle(targetsInDegree);
        endTime = LocalTime.now();
        Duration.between(startTime, endTime).toMillis();
        GraphDTO graphRunResult = new GraphDTO(this.graph,Duration.between(startTime, endTime).toMillis());
        createGraphOfFailedTargets();
        return graphRunResult;
    }

    private void createGraphOfFailedTargets() {
        Graph failedTargetsGraph = new Graph(new HashMap<>(), this.graph.getName());
        Map<String, Target> newGraph = failedTargetsGraph.getTargetGraph();
        for(Target currTarget : this.graph.getTargets()){
            if(currTarget.getRunResult().equals(RunResults.FAILURE) ||
                    currTarget.getRunResult().equals(RunResults.SKIPPED)){
                newGraph.put(currTarget.getName(), currTarget);
            }
        }
        this.graph = failedTargetsGraph;
    }

    private void updateParentsStatus(Set<Target> requiredFor, Set<String> skippedFathers) {
        if(requiredFor.isEmpty())
            return;
        else{
            for(Target currTarget : requiredFor){
                currTarget.setRunStatus(RunStatus.SKIPPED);
                currTarget.setRunResult(RunResults.SKIPPED);
                skippedFathers.add(currTarget.getName());
                updateParentsStatus(currTarget.getRequiredFor(), skippedFathers);
            }
        }
    }

    private void outputTargetResult(List<Consumer<TargetDTO>> outputConsumers, TargetDTO targetResult) {
        for(Consumer<TargetDTO> currConsumer: outputConsumers){
            currConsumer.accept(targetResult);
        }
    }

    private void updateNeighborTargets(Target currTarget, List<Target> sourceTargets, Map<String, Integer> targetsInDegree, TargetDTO targetResult) {
        for(Target neighborTarget : currTarget.getRequiredFor()){
            targetsInDegree.put(neighborTarget.getName(), (targetsInDegree.get(neighborTarget.getName())-1));
            if((targetsInDegree.get(neighborTarget.getName())) == 0){
                if(currTarget.getRunResult() != RunResults.SKIPPED && currTarget.getRunResult() != RunResults.FAILURE){
                    if(neighborTarget.getRunStatus() != RunStatus.SKIPPED){
                        neighborTarget.setRunStatus(RunStatus.WAITING);
                    }
                }
                sourceTargets.add(neighborTarget);
                if(!currTarget.getRunStatus().equals(RunStatus.SKIPPED)){
                    targetResult.getTargetsThatCanBeRun().add(neighborTarget.getName());
                }
            }
        }
    }

    private List<Target> initSourceQueue(Collection<Target> targets) {
        List<Target> sourceTargets = new LinkedList<>();
        for(Target target : targets){
            if(target.getDependsOn().isEmpty()){
                target.setRunStatus(RunStatus.WAITING);
                sourceTargets.add(target);
            }
        }
        return sourceTargets;
    }

    private void checkingForCycle(Map<String, Integer> targetsInDegree) {
        for(Map.Entry<String,Integer> targetEntry : targetsInDegree.entrySet()){
            if((targetEntry.getValue()) != 0){
                //TODO: throw an exception for cycle
            }
        }
    }

    private Map<String, Integer> getTargetsInDegree(){
        Map<String, Integer> targetsInDegree = new HashMap<>();
        for(Map.Entry<String, Target> targetEntry : this.graph.getTargetGraph().entrySet()){
            targetsInDegree.put(targetEntry.getKey(), targetEntry.getValue().getDependsOn().size());
        }
        return targetsInDegree;
    }

    protected abstract TargetDTO executeTaskOnTarget(Target target);



}
