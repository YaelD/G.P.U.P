import schema.generated.GPUPTarget;
import schema.generated.GPUPTargetDependencies;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Target {

    private String name;
    private PlaceInGraph place;
    private Set<Target> requiredFor = new HashSet<>();
    private Set<Target> dependsOn = new HashSet<>();
    private String info;


    public Target(GPUPTarget target) {
        this.name = target.getName();
        this.info = target.getGPUPUserData();
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

    @Override
    public int hashCode() {
        return Objects.hash(name, place, requiredFor, dependsOn, info);
    }
}
