package tables;

//import dto.TargetDTO;
import javafx.beans.property.SimpleStringProperty;

public class TableTargetProperties {
        private SimpleStringProperty name;
        private SimpleStringProperty info;
        private Integer numOfDependsOn;
        private Integer totalNumOfDependsOn;
        private Integer numOfRequiredFor;
        private Integer totalNumOfRequiredFor;
        private Integer numOfSerialSet;
        private SimpleStringProperty placeInGraph;



//    public TableTargetProperties(TargetDTO dto) {
//        this.name = new SimpleStringProperty(dto.getName());
//        this.info = new SimpleStringProperty(dto.getInfo());
//        this.numOfDependsOn = dto.getDependsOn().size();
//        this.numOfRequiredFor = dto.getRequiredFor().size();
//        this.totalNumOfDependsOn = dto.getTotalDependsOn().size();
//        this.totalNumOfRequiredFor = dto.getTotalRequiredFor().size();
//        this.numOfSerialSet = dto.getTotalNumOfSerialSets();
//        this.placeInGraph = new SimpleStringProperty(dto.getPlace().toString());
//    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getInfo() {
        return info.get();
    }

    public SimpleStringProperty infoProperty() {
        return info;
    }

    public Integer getNumOfDependsOn() {
        return numOfDependsOn;
    }

    public Integer getTotalNumOfDependsOn() {
        return totalNumOfDependsOn;
    }

    public Integer getNumOfRequiredFor() {
        return numOfRequiredFor;
    }

    public Integer getTotalNumOfRequiredFor() {
        return totalNumOfRequiredFor;
    }

    public Integer getNumOfSerialSet() {
        return numOfSerialSet;
    }

    public String getPlaceInGraph() {
        return placeInGraph.get();
    }

    public SimpleStringProperty placeInGraphProperty() {
        return placeInGraph;
    }
}
