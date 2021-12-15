package graph;

import target.Target;

import java.util.ArrayList;
import java.util.List;

public class SerialSets {

    private List<SerialSet> serialSetList = new ArrayList<>();

    public SerialSets(List<SerialSet> serialSetList) {
        this.serialSetList = serialSetList;
    }

    public List<SerialSet> getSerialSetList() {
        return serialSetList;
    }

    public boolean isTargetInSerialSet (Target target, SerialSet serialSet){
        for(SerialSet currSerialSet: this.serialSetList){
            if(currSerialSet.getTargetsName().contains(target.getName())){
                serialSet = currSerialSet;
                return true;
            }
        }
        return false;
    }
}
