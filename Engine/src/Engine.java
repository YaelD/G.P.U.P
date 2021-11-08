import java.util.Collection;
import java.util.List;

public interface Engine {

    public boolean readFile();

    public GraphDTO getGraph();

    public TargetDTO getTarget(String name);

    public Collection<List<TargetDTO>> getPaths(String firstTargetName, String secondTargetName, String relation);

    public GraphDTO activateTask();
}
