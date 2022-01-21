package graph;

import target.Target;

import java.util.ArrayList;
import java.util.List;

public class SerialSetsContainer {

    private List<SerialSet> serialSetList = new ArrayList<>();

    public SerialSetsContainer(List<SerialSet> serialSetList) {
        this.serialSetList = serialSetList;
    }

    public SerialSetsContainer() {
    }

    public void setSerialSetList(List<SerialSet> serialSetList) {
        this.serialSetList = serialSetList;
    }

    public List<SerialSet> getSerialSetList() {
        return serialSetList;
    }

    public synchronized boolean isTargetInSerialSet (Target target){
        for(SerialSet currSerialSet: this.serialSetList){
            if(currSerialSet.getTargetsList().contains(target)){
                //TODO: function in serial-set that get the monitor
                //currSerialSet.updateTargetsRunStatus(target);
                return true;
            }
        }
        return false;
    }


}
