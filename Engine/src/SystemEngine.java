import exceptions.TargetNotExistException;

import java.util.Collection;
import java.util.List;

public class SystemEngine implements Engine{

    private Graph graph;
    private String path;

    @Override
    public boolean readFile() {
        return false;
    }

    @Override
    public GraphDTO getGraphDTO() {
        return null;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public TargetDTO getTarget(String name) throws TargetNotExistException {
        return null;
    }

    @Override
    public Collection<List<TargetDTO>> getPaths(String firstTargetName, String secondTargetName, String relation) {
        return null;
    }

    @Override
    public GraphDTO activateTask() {
        return null;
    }

    @Override
    public String toString() {
        return "SystemEngine{" +
                "graph=" + graph +
                '}';
    }
}
