package findpath;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.Collection;
import java.util.List;

public class PathsTableController {

    @FXML
    private TableView<String> pathsTable;

    @FXML
    private TableColumn<String, String> pathsColumn;


    public void setTableValues(String[][] paths){
        final ObservableList<String> data = FXCollections.observableArrayList();
        for(String[] currPath: paths){
            String pathStr = getPath(currPath);
            data.add(pathStr);
        }
        pathsTable.setItems(data);
    }

    public void initialize(){
        pathsColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String, String> param) {
                return new SimpleStringProperty(param.getValue());
            }
        });
    }

    private String getPath(String[] currPath){
        String path = "";
        int size = currPath.length-1;
        for(int i =0; i < size; ++i){
            path += currPath[i] + "->";
        }
        path += currPath[size];
        return path;
    }






}
