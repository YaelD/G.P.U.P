package tables;

//import dto.GraphDTO;
//import dto.TargetDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TargetsTableController  {

    @FXML
    private TableView<TableTargetProperties> Targets_TableView;

    @FXML
    private TableColumn<TableTargetProperties, String> Name_column;

    @FXML
    private TableColumn<TableTargetProperties, String> Place_Column;

    @FXML
    private TableColumn<TableTargetProperties, Integer> DependsOn_Column;

    @FXML
    private TableColumn<TableTargetProperties, Integer> RequiredFor_Column;

    @FXML
    private TableColumn<TableTargetProperties, String> Info_Column;

    @FXML
    private TableColumn<TableTargetProperties, Integer> SerialSet_Column;


    public void initialize(){
        Name_column.setCellValueFactory(new PropertyValueFactory<TableTargetProperties, String>("name"));
        Place_Column.setCellValueFactory(new PropertyValueFactory<TableTargetProperties, String>("placeInGraph"));
        Info_Column.setCellValueFactory(new PropertyValueFactory<TableTargetProperties, String>("info"));
        TableColumn dependsOnDirect = this.DependsOn_Column.getColumns().get(0);
        dependsOnDirect.setCellValueFactory(new PropertyValueFactory<TableTargetProperties, Integer>("numOfDependsOn"));
        TableColumn dependsOnIndirect = this.DependsOn_Column.getColumns().get(1);
        dependsOnIndirect.setCellValueFactory(new PropertyValueFactory<TableTargetProperties, Integer>("totalNumOfDependsOn"));
        TableColumn requiredForDirect = this.RequiredFor_Column.getColumns().get(0);
        requiredForDirect.setCellValueFactory(new PropertyValueFactory<TableTargetProperties, Integer>("numOfRequiredFor"));
        TableColumn requiredForIndirect = this.RequiredFor_Column.getColumns().get(1);
        requiredForIndirect.setCellValueFactory(new PropertyValueFactory<TableTargetProperties, Integer>("totalNumOfRequiredFor"));
        SerialSet_Column.setCellValueFactory(new PropertyValueFactory<TableTargetProperties, Integer>("numOfSerialSet"));
    }

//    public void setTargets(GraphDTO graphDTO){
//        final ObservableList<TableTargetProperties> data = FXCollections.observableArrayList();
//        for(TargetDTO currTarget: graphDTO.getTargets().values()){
//            data.add(new TableTargetProperties(currTarget));
//        }
//        Targets_TableView.setItems(data);
//    }


}
