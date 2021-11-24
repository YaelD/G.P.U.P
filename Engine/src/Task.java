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

    protected GraphDTO executeTaskOnGraph(List<Consumer<TargetDTO>> outputConsumers){
        TargetDTO targetResult;
        LocalTime startTime, endTime;
        Map<String, Integer> targetsInDegree = getTargetsInDegree();
        Queue<Target> sourceTargets = initSourceQueue(this.graph.getTargets());
        startTime = LocalTime.now();
        while(!sourceTargets.isEmpty()){
            Target currTarget = sourceTargets.remove();
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

    private void updateNeighborTargets(Target currTarget, Queue<Target> sourceTargets, Map<String, Integer> targetsInDegree, TargetDTO targetResult) {
        for(Target neighborTarget : currTarget.getDependsOn()){
            targetsInDegree.put(neighborTarget.getName(), (targetsInDegree.get(neighborTarget.getName())-1));
            if((targetsInDegree.get(neighborTarget.getName())) == 0){
                neighborTarget.setRunStatus(RunStatus.WAITING);
                sourceTargets.add(neighborTarget);
                if(!currTarget.getRunStatus().equals(RunStatus.SKIPPED)){
                    targetResult.getTargetsThatCanBeRun().add(neighborTarget.getName());
                }
            }
        }
    }

    private Queue<Target> initSourceQueue(Collection<Target> targets) {
        Queue<Target> sourceTargets = new LinkedList<>();
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
        for(Map.Entry<String,Target> targetEntry : this.graph.getTargetGraph().entrySet()){
            targetsInDegree.put(targetEntry.getKey(), targetEntry.getValue().getDependsOn().size());
        }
        return targetsInDegree;
    }

    protected abstract TargetDTO executeTaskOnTarget(Target target);



}
