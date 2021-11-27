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
        TargetDTO targetResult;
        List<Target> sortedTargets = topologicalSort(this.graph);
        LocalTime startTime = LocalTime.now();
        for(Target currTarget : sortedTargets){
            if(currTarget.getRunStatus().equals(RunStatus.WAITING)){
                targetResult = executeTaskOnTarget(currTarget);
            }
            else{
                targetResult = new TargetDTO(currTarget);
            }
            if(targetResult.getRunResult().equals(RunResults.FAILURE)){
                updateParentsStatus(currTarget, targetResult.getSkippedFathers());
            }
            getOpenedTargetsToRun(targetResult, currTarget);
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
                if(!currTarget.getRunStatus().equals(RunStatus.SKIPPED)){
                    currTarget.setRunStatus(RunStatus.WAITING);
                }
                targetResult.getTargetsThatCanBeRun().add(currTarget.getName());
            }
            else{
                isOpenedToRun = true;
            }
        }
    }

    private void updateParentsStatus(Target target, Set<String> skippedFathers) {

        if(target.getRequiredFor().isEmpty()){
            return;
        }
        else{
            for(Target currTarget : target.getRequiredFor()){
                currTarget.setRunStatus(RunStatus.SKIPPED);
                currTarget.setRunResult(RunResults.SKIPPED);
                skippedFathers.add(currTarget.getName());
                updateParentsStatus(currTarget, skippedFathers);
            }
        }
    }

    protected abstract TargetDTO executeTaskOnTarget(Target currTarget);

    private void createGraphOfFailedTargets() {
        String graphName = this.graph.getName();
        Graph newGraph = new Graph(new HashMap<>(), graphName);
        for(Target currTarget : this.graph.getTargets()){
            if(currTarget.getRunResult().equals(RunResults.FAILURE) ||
                    currTarget.getRunResult().equals(RunResults.SKIPPED)){
                currTarget.setRunResult(null);
                currTarget.setRunStatus(RunStatus.FROZEN);
                newGraph.getTargetGraph().put(currTarget.getName(), currTarget.clone());
            }
        }
        Graph.updateGraphTargets(newGraph);
        this.graph = newGraph;
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
