import java.util.Set;

public class Target {

    private String name;
    private PlaceInGraph place;
    private Set<Target> requiredFor;
    private Set<Target> dependsOn;
    private String info;


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
}
