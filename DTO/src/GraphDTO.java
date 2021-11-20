import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GraphDTO {

    private int numOfTargets;
    private String name;
    private Map<String, TargetDTO> targets = new HashMap<>();
    private SimpleDateFormat runTime;


    public GraphDTO(Graph graph) {
        this.name = graph.getName();
        this.numOfTargets = graph.getTargets().size();
        for(Target target: graph.getTargets()) {
            this.targets.put(target.getName(), new TargetDTO(target));
        }
        /*
        for(TargetDTO targetDTO: this.targets.values()) {
            targetDTO.dependencyTargetDTO(graph.getTarget(targetDTO.getName()), this.targets);
        }
         */
    }

    public String getName() {
        return name;
    }

    public int getNumOfTargets() {
        return numOfTargets;
    }

    public Map<String, TargetDTO> getTargets() {
        return targets;
    }

    public SimpleDateFormat getRunTime() {
        return runTime;
    }

    public int getNumOfTargetsInPlace(PlaceInGraph place){
        int counter = 0;
        for(TargetDTO target: targets.values()){
            if(target.getPlace() == place)
                counter++;
        }
        return counter;
    }

    public int getNumOfTargetsRunResult(RunResults runResult){
        int counter = 0;
        for(TargetDTO target: targets.values()){
            if(target.getRunResult() == runResult)
                counter++;
        }
        return counter;
    }

}
