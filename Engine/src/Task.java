import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Task {

    protected Graph graph;

    protected Task(Graph graph) {
        this.graph = graph;
    }

    protected List<Target> TopologicalSort(){
        List<Target> sortedTargets = new ArrayList<>();
        Collection<Target> targets = this.graph.getTargets();
        for(Target target : targets){

        }
        return sortedTargets;
    }

}
