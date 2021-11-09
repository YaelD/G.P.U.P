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
        for(GPUPTargetDependencies.GPUGDependency currDependency: target.getGPUPTargetDependencies().getGPUGDependency())
        {
            if(currDependency.getType().equals("requiredFor"))
            {
                //TODO: talk with Yael about this
                this.requiredFor.add(new Target(currDependency.getValue()));
            }
            if(currDependency.equals("dependsOn"))
            {
                this.dependsOn.add(new Target(currDependency.getValue()));
            }
            if(this.requiredFor.isEmpty() && this.dependsOn.isEmpty())
            {
                this.place = PlaceInGraph.INDEPENDENT;
            }
            else if(this.requiredFor.isEmpty())
            {
                this.place = PlaceInGraph.ROOT;
            }
            else if(this.dependsOn.isEmpty())
            {
                this.place = PlaceInGraph.LEAF;
            }
            else
            {
                this.place = PlaceInGraph.MIDDLE;
            }
        }
    }

    public Target(String name)
    {
        this.name = name;
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
