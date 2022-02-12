package findpath;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Collection;
import java.util.List;

public class PathsTableController {

    @FXML
    private TableView<SimpleTableStringProperty> pathsTable;

    @FXML
    private TableColumn<SimpleTableStringProperty, String> pathsColumn;


    public void setTableValues(Collection<List<String>> paths){
        final ObservableList<SimpleTableStringProperty> data = FXCollections.observableArrayList();
        for(List<String> currPath: paths){
            String pathStr = getPath(currPath);
            data.add(new SimpleTableStringProperty(pathStr));
        }
        pathsTable.setItems(data);
    }

    public void initialize(){
        pathsColumn.setCellValueFactory(new PropertyValueFactory<SimpleTableStringProperty, String>("path"));
    }

    private String getPath(List<String> currPath){
        String path = "";
        int size = currPath.size()-1;
        for(int i =0; i < size; ++i){
            path += currPath.get(i) + "->";
        }
        path += currPath.get(size);
        return path;
    }






}
