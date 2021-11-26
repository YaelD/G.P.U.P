package target;

import schema.generated.GPUPTarget;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Target implements Cloneable {

    private String name;
    private PlaceInGraph place;
    private Set<Target> requiredFor = new HashSet<>(); //This target.Target requiredFor the Set's Targets
    private Set<Target> dependsOn = new HashSet<>();// This target.Target is dependsOn the Set's targets
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
        return "target.Target{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return Objects.equals(name, target.name) && place == target.place && Objects.equals(requiredFor, target.requiredFor) && Objects.equals(dependsOn, target.dependsOn) && Objects.equals(info, target.info);
    }

    public void setRequiredFor(Set<Target> requiredFor) {
        this.requiredFor = requiredFor;
    }

    public void setDependsOn(Set<Target> dependsOn) {
        this.dependsOn = dependsOn;
    }

    //    @Override
//    public int hashCode() {
//        return Objects.hash(name, place, requiredFor, dependsOn, info);
//    }




    @Override
    public Target clone()  {
        try {
            Target newTarget = (Target) super.clone();
            //newTarget.requiredFor = new HashSet<>(this.requiredFor);
            //newTarget.dependsOn = new HashSet<>(this.dependsOn);
            return newTarget;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
