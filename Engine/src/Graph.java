import schema.generated.GPUPTargets;

import java.util.Collection;
import java.util.Map;

public class Graph {

    private Map<String, Target> targetGraph;
    private String name;

    public String getName() {
        return name;
    }

    public Graph() {

    }

    public Collection<Target> getTargets(){
        return targetGraph.values();
    }

    public Target getTarget(String name){
        return targetGraph.get(name);
    }
}
