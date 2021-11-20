import java.text.SimpleDateFormat;
import java.util.*;

public class TargetDTO {

    private String name;
    private PlaceInGraph place;
    private Set<TargetDTO> requiredFor = new HashSet<>();
    private Set<TargetDTO> dependsOn = new HashSet<>();
    private String info;
    private RunResults runResult;
    private SimpleDateFormat runTime;

    public TargetDTO(Target target) {
        this.name = target.getName();
        this.place = target.getPlace();
        this.info = target.getInfo();
        //TODO: complete sets

    }

    public void dependencyTargetDTO(Target target, Map<String, TargetDTO> targets){
        for(Target currTarget: target.getRequiredFor()){
            this.requiredFor.add(targets.get(currTarget.getName()));
        }
        for(Target currTarget: target.getDependsOn()){
            this.dependsOn.add(targets.get(currTarget.getName()));
        }
    }

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
