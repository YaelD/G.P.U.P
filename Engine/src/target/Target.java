package target;

import graph.Dependency;
import graph.SerialSet;
import graph.SerialSetsContainer;
import schema.generated.GPUPTarget;

import java.util.*;

public class Target implements Cloneable {

    private String name;
    private PlaceInGraph place;
    private Set<Target> requiredFor = new HashSet<>(); //This target.Target requiredFor the Set's Targets
    private Set<Target> dependsOn = new HashSet<>();// This target.Target is dependsOn the Set's targets
    private String info;
    private RunResults runResult;
    private long runningTime;
    private RunStatus runStatus;
    private SerialSetsContainer serialSetsContainer;


    public Target(GPUPTarget target) {
        this.name = target.getName();
        this.info = target.getGPUPUserData();
        this.runStatus = RunStatus.FROZEN;
        this.serialSetsContainer = new SerialSetsContainer(new ArrayList<>());
    }

    public SerialSetsContainer getSerialSetsContainer() {
        return serialSetsContainer;
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

    public Set<Target> getDependencies(Dependency dependency){
        if(dependency.equals(Dependency.REQUIRED_FOR)){
            return this.getRequiredFor();
        }
        else{
            return this.getDependsOn();
        }
    }

    public RunResults getRunResult() {
        return runResult;
    }

    public synchronized void setRunResult(RunResults runResult) {
        this.runResult = runResult;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public synchronized void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public RunStatus getRunStatus() {
        return runStatus;
    }

    public synchronized void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
        notifyAll();
    }

    public void getSerialSetsMonitors(){
        for(SerialSet currSerialSet : this.getSerialSetsContainer().getSerialSetList()){
            currSerialSet.getSerialSetMonitor();
        }
    }

    public void freeSerialSetsMonitors(){
        for(SerialSet currSerialSet : this.getSerialSetsContainer().getSerialSetList()){
            currSerialSet.freeMonitor();
        }
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

    public void updateParentsStatus(Set<String> skippedFathers) {
        if(this.getRequiredFor().isEmpty()){
            return;
        }
        else{
            for(Target currTarget : this.getRequiredFor()){
                currTarget.setRunStatus(RunStatus.SKIPPED);
                currTarget.setRunResult(RunResults.SKIPPED);
                skippedFathers.add(currTarget.getName());
                currTarget.updateParentsStatus(skippedFathers);
            }
        }
    }

    public void getRequiredForAncestors(Set<String> targetsSet){
        for(Target target : this.requiredFor){
            targetsSet.add(target.name);
            target.getRequiredForAncestors(targetsSet);
        }
    }

    public void getDependsOnAncestors(Set<String> targetsSet){
        for(Target target : this.dependsOn){
            targetsSet.add(target.name);
            target.getDependsOnAncestors(targetsSet);
        }
    }

    @Override
    public Target clone()  {
        try {
            Target newTarget = (Target) super.clone();
            return newTarget;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
