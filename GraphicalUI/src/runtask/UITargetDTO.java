package runtask;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import target.PlaceInGraph;

import java.time.LocalTime;
import java.util.List;

public class UITargetDTO {

    private PlaceInGraph placeInGraph;
    private List<String> serialSetsName;

    private SimpleListProperty<String> runningFathers;
    private SimpleListProperty<String> runningTargetsInSerialSet;

    private SimpleObjectProperty<LocalTime> waitTime;

    private List<String> failedFathers;

    private SimpleObjectProperty<LocalTime> processTime;

    private String finishResult;

    

    public void setRunningFathers(ObservableList<String> runningFathers) {
        this.runningFathers.set(runningFathers);
    }

    public void setRunningTargetsInSerialSet(ObservableList<String> runningTargetsInSerialSet) {
        this.runningTargetsInSerialSet.set(runningTargetsInSerialSet);
    }

    public void setWaitTime(LocalTime waitTime) {
        this.waitTime.set(waitTime);
    }

    public void setProcessTime(LocalTime processTime) {
        this.processTime.set(processTime);
    }
}
