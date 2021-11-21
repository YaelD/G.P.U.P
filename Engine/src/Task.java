import java.util.*;

public abstract class Task {

    protected Graph graph;

    protected Task(Graph graph) {
        this.graph = graph;
    }

    protected List<Target> topologicalSort(){
        List<Target> sortedTargets = new ArrayList<>();
        Map<String, Integer> targetsInDegree = getTargetsInDegree();
        Queue<Target> sourceTargets = new LinkedList<>();

        for(Target target : this.graph.getTargets()){
            if(target.getDependsOn().isEmpty()){
                sourceTargets.add(target);
            }
        }
        while(!sourceTargets.isEmpty()){
            Target currTarget = sourceTargets.remove();
            //executeTask(currTarget);
            sortedTargets.add(currTarget);
            for(Target neighborTarget : currTarget.getDependsOn()){
                targetsInDegree.put(neighborTarget.getName(), (targetsInDegree.get(neighborTarget.getName())-1));
                if((targetsInDegree.get(neighborTarget.getName())) == 0){
                    sourceTargets.add(neighborTarget);
                }
            }
        }
        //checkingForCycle(targetsInDegree);
        return sortedTargets;
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

    protected abstract void executeTaskOnTarget(Target target);

    public void executeTaskOnGraph(){
        List<Target> sortedTargets = topologicalSort();
        for(Target target : sortedTargets){
            executeTaskOnTarget(target);
        }
    }
}
