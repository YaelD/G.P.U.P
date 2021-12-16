package graph;

import exceptions.SerialSetException;
import exceptions.TargetNotExistException;
import target.RunStatus;
import target.Target;

import java.util.List;
import java.util.Set;

public class SerialSet {

    private String name;
    private Set<Target> targetList;

    public SerialSet(String name, Set<Target> targetList) {
        this.name = name;
        this.targetList = targetList;
    }

    public String getName() {
        return name;
    }

    public Set<Target> getTargetsSet() {
        return targetList;
    }

    public static void checkTargetsInSet(List<String> targetsName, Graph graph, Set<Target> serialSet, String serialSetName) throws SerialSetException {
        for(String currTarget : targetsName){
            if(!graph.getTargetGraph().containsKey(currTarget)){
                throw new SerialSetException(currTarget, serialSetName);
            }
            else{
                serialSet.add(graph.getTargetGraph().get(currTarget));
            }
        }
    }

    public synchronized void updateTargetsRunStatus(Target target) {
        for(Target currTarget : this.getTargetsSet()){
            if(!currTarget.getName().equals(target.getName())){
                if(currTarget.getRunStatus().equals(RunStatus.WAITING)) {
                    currTarget.setRunStatus(RunStatus.FROZEN);
                }
            }
        }
    }
}
