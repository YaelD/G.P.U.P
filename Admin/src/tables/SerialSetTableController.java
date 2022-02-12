package tables;

import dto.SerialSetDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class SerialSetTableController {



    @FXML
    private TableView<SerialSetDTO> serialSetTable;

    @FXML
    private TableColumn<SerialSetDTO, String> nameColumn;
    @FXML
    private TableColumn<SerialSetDTO, List<String>> targetsColumn;


    public void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<SerialSetDTO, String>("name"));
        targetsColumn.setCellValueFactory(new PropertyValueFactory<SerialSetDTO, List<String>>("targetsName"));
    }

    public void fillSerialSetsInfo(List<SerialSetDTO> serialSetDTOList){
        final ObservableList<SerialSetDTO> data = FXCollections.observableArrayList();
        for(SerialSetDTO serialSetDTO: serialSetDTOList){
            data.add(serialSetDTO);
        }
        serialSetTable.setItems(data);
    }


}
