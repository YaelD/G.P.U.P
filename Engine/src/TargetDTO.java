import java.text.SimpleDateFormat;
import java.util.Set;

public class TargetDTO {

    private String name;
    private PlaceInGraph place;
    private Set<TargetDTO> requiredFor;
    private Set<TargetDTO> dependsOn;
    private String info;
    private RunResults runResult;
    private SimpleDateFormat runTime;

    public String getName() {
        return name;
    }

    public PlaceInGraph getPlace() {
        return place;
    }

    public Set<TargetDTO> getRequiredFor() {
        return requiredFor;
    }

    public Set<TargetDTO> getDependsOn() {
        return dependsOn;
    }

    public String getInfo() {
        return info;
    }

    public RunResults getRunResult() {
        return runResult;
    }

    public SimpleDateFormat getRunTime() {
        return runTime;
    }
}
