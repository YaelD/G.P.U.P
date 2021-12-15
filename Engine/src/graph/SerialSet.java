package graph;

import exceptions.TargetNotExistException;

import java.util.List;

public class SerialSet {

    private String name;
    private List<String> targetsName;

    public SerialSet(String name, List<String> targetsName) {
        this.name = name;
        this.targetsName = targetsName;
    }

    public String getName() {
        return name;
    }

    public List<String> getTargetsName() {
        return targetsName;
    }

    public static void checkTargetsInSet(List<String> targetsName, Graph graph) throws TargetNotExistException {
        for(String currTarget : targetsName){
            if(!graph.getTargetGraph().containsKey(currTarget)){
                throw new TargetNotExistException(currTarget);
            }
        }
    }

}
