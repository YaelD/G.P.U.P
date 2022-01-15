package dto;

import graph.SerialSet;
import target.Target;
import java.util.ArrayList;
import java.util.List;


public class SerialSetDTO {
    private List<String> targetsName = new ArrayList<>();
    private String name;

    public SerialSetDTO(SerialSet serialSet) {
        this.name = serialSet.getName();
        for(Target currTarget: serialSet.getTargetsList()){
            this.targetsName.add(currTarget.getName());
        }
    }

    public List<String> getTargetsName() {
        return targetsName;
    }

    public String getName() {
        return name;
    }
}
