import java.text.SimpleDateFormat;
import java.util.*;

public class TargetDTO {

    private String name;
    private PlaceInGraph place;
    //private Set<TargetDTO> requiredFor = new HashSet<>();
    //private Set<TargetDTO> dependsOn = new HashSet<>();
    private Set<String> requiredFor = new HashSet<>();
    private Set<String> dependsOn = new HashSet<>();
    private String info;
    private RunResults runResult;
    private SimpleDateFormat runTime;

    public TargetDTO(Target target) {
        this.name = target.getName();
        this.place = target.getPlace();
        this.info = target.getInfo();
        for(Target currTarget: target.getRequiredFor())
        {
            this.requiredFor.add(currTarget.getName());
        }
        for (Target currTarget: target.getDependsOn())
        {
            this.dependsOn.add(currTarget.getName());
        }
    }
    /*

    public void dependencyTargetDTO(Target target, Map<String, TargetDTO> targets){
        for(Target currTarget: target.getRequiredFor()){
            this.requiredFor.add(targets.get(currTarget.getName()));
        }
        for(Target currTarget: target.getDependsOn()){
            this.dependsOn.add(targets.get(currTarget.getName()));
        }
    }

 */

    public String getName() {
        return name;
    }

    public PlaceInGraph getPlace() {
        return place;
    }

    public Set<String> getRequiredFor() {
        return requiredFor;
    }

    public Set<String> getDependsOn() {
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
