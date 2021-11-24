import schema.generated.GPUPTarget;
import schema.generated.GPUPTargetDependencies;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Target implements Cloneable {

    private String name;
    private PlaceInGraph place;
    private Set<Target> requiredFor = new HashSet<>(); //This Target requiredFor the Set's Targets
    private Set<Target> dependsOn = new HashSet<>();// This Target is dependsOn the Set's targets
    private String info;
    private RunResults runResult;
    private long runningTime;
    private RunStatus runStatus;


    public Target(GPUPTarget target) {
        this.name = target.getName();
        this.info = target.getGPUPUserData();
        this.runStatus = RunStatus.FROZEN;
    }

    public String getName() {
        return name;
    }

    public PlaceInGraph getPlace() {
        return place;
    }

    public Set<Target> getRequiredFor() {
        return requiredFor;
    }

    public Set<Target> getDependsOn() {
        return dependsOn;
    }

    public String getInfo() {
        return info;
    }

    public void setPlace(PlaceInGraph place) {
        this.place = place;
    }

    public Set<Target> getDependencies(String dependency){
        if(dependency.equals("requiredFor")){
            return this.getRequiredFor();
        }
        else{
            return this.getDependsOn();
        }
    }

    public RunResults getRunResult() {
        return runResult;
    }

    public void setRunResult(RunResults runResult) {
        this.runResult = runResult;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public RunStatus getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }

    @Override
    public String toString() {
        return "Target{" +
                "name='" + name + '\'' +
                ", place=" + place +
                ", requiredFor=" + requiredFor +
                ", dependsOn=" + dependsOn +
                ", info='" + info + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return Objects.equals(name, target.name) && place == target.place && Objects.equals(requiredFor, target.requiredFor) && Objects.equals(dependsOn, target.dependsOn) && Objects.equals(info, target.info);
    }


//    @Override
//    public int hashCode() {
//        return Objects.hash(name, place, requiredFor, dependsOn, info);
//    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
