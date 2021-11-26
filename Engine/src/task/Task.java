package task;

import dto.GraphDTO;
import dto.TargetDTO;
import dto.TaskParamsDTO;
import exceptions.CycleException;
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

    public static List<Target> topologicalSort(Graph graph) throws CycleException {
        List<Target> sortedTargets = new ArrayList<>();
        Map<String, Integer> targetsInDegree = getTargetsInDegree(graph.getTargetGraph());
        List<Target> sourceTargets = initSourceQueue(graph.getTargets());
        while (!sourceTargets.isEmpty()) {
            Target currTarget = sourceTargets.remove(0);
            sortedTargets.add(currTarget);
            for (Target neighborTarget : currTarget.getRequiredFor()) {
                targetsInDegree.put(neighborTarget.getName(), (targetsInDegree.get(neighborTarget.getName()) - 1));
                if ((targetsInDegree.get(neighborTarget.getName())) == 0) {
                    sourceTargets.add(graph.getTarget(neighborTarget.getName()));
                }
            }
        }
        checkingForCycle(targetsInDegree);
        return sortedTargets;
    }

    public GraphDTO executeTaskOnGraph(List<Consumer<TargetDTO>> outputConsumers) throws CycleException {
        List<Target> sortedTargets = topologicalSort(this.graph);
        LocalTime startTime = LocalTime.now();
        for(Target currTarget : sortedTargets){
            TargetDTO targetResult = executeTaskOnTarget(currTarget);
            getOpenedTargetsToRun(targetResult, currTarget);
            if(targetResult.getRunResult().equals(RunResults.FAILURE)){
                updateParentsStatus(currTarget.getRequiredFor(), targetResult.getSkippedFathers());
            }
            outputTargetResult(outputConsumers, targetResult);
        }
        LocalTime endTime = LocalTime.now();
        GraphDTO graphRunResult = new GraphDTO(this.graph, Duration.between(startTime, endTime).toMillis());
        createGraphOfFailedTargets();
        return graphRunResult;
    }

    private void getOpenedTargetsToRun(TargetDTO targetResult, Target target) {
        boolean isOpenedToRun = true;
        for(Target currTarget : target.getRequiredFor()){
            for(Target currTargetFather : currTarget.getDependsOn()){
                if(currTargetFather.getRunStatus().equals(RunStatus.FROZEN)){
                    isOpenedToRun = false;
                }
            }
            if (isOpenedToRun) {
                targetResult.getTargetsThatCanBeRun().add(currTarget.getName());
            }
            else{
                isOpenedToRun = true;
            }
        }
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

    protected abstract TargetDTO executeTaskOnTarget(Target currTarget);

    private void createGraphOfFailedTargets() {
        String graphName = this.graph.getName();
        Set<Target> newDependsOn = new HashSet<>();
        Set<Target> newRequiredFor = new HashSet<>();
        Map<String, Target> newGraph = new HashMap<>();
        for(Target currTarget : this.graph.getTargets()){
            if(currTarget.getRunResult().equals(RunResults.FAILURE) ||
                    currTarget.getRunResult().equals(RunResults.SKIPPED)){
                newGraph.put(currTarget.getName(), currTarget.clone());
            }
        }
        for(Target currTarget: newGraph.values()){
            for(Target dependentTarget: currTarget.getDependsOn()){
                if(newGraph.containsKey(dependentTarget.getName())){
                    newDependsOn.add(newGraph.get(dependentTarget.getName()));
                }
            }
            currTarget.setDependsOn(newDependsOn);
            newDependsOn = new HashSet<>();
            for(Target requiredTarget: currTarget.getRequiredFor()){
                if(newGraph.containsKey(requiredTarget.getName())){
                    newRequiredFor.add(newGraph.get(requiredTarget.getName()));
                }
            }
            currTarget.setRequiredFor(newRequiredFor);
            newRequiredFor = new HashSet<>();
        }
        this.graph = new Graph(newGraph, graphName);
    }

    private void outputTargetResult(List<Consumer<TargetDTO>> outputConsumers, TargetDTO targetResult) {
        for(Consumer<TargetDTO> currConsumer: outputConsumers){
            currConsumer.accept(targetResult);
        }
    }

    private static List<Target> initSourceQueue(Collection<Target> targets) {
        List<Target> sourceTargets = new LinkedList<>();
        for(Target target : targets){
            if(target.getDependsOn().isEmpty()){
                target.setRunStatus(RunStatus.WAITING);
                sourceTargets.add(target);
            }
        }
        return sourceTargets;
    }

    private static void checkingForCycle(Map<String, Integer> targetsInDegree) throws CycleException {
        for(Map.Entry<String,Integer> targetEntry : targetsInDegree.entrySet()){
            if((targetEntry.getValue()) != 0){
                throw new CycleException(targetEntry.getKey());
            }
        }
    }

    private static Map<String, Integer> getTargetsInDegree(Map<String, Target> graphMap){
        Map<String, Integer> targetsInDegree = new HashMap<>();
        for(Map.Entry<String, Target> targetEntry : graphMap.entrySet()){
            targetsInDegree.put(targetEntry.getKey(), targetEntry.getValue().getDependsOn().size());
        }
        return targetsInDegree;
    }
    public abstract void updateParameters(TaskParamsDTO taskParamsDTO);



}
