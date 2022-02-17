package dashboard;

import RefreshingItems.GraphListRefresher;
import RefreshingItems.TaskListRefresher;
import dto.GraphDTO;
import dto.TaskDTO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TaskTableController {

    private Timer timer;
    private TimerTask listRefresher;


    @FXML
    private TableView<TaskDTO> tasksInSystemTableView;

    @FXML
    private TableColumn<TaskDTO, String> taskname_column;

    @FXML
    private TableColumn<TaskDTO, String> creator_name_column;

    @FXML
    private TableColumn<TaskDTO, String> graphName_column;

    @FXML
    private TableColumn<TaskDTO, String> totalNumOfTargets_column;

    @FXML
    private TableColumn<TaskDTO, String> numOfLeaves_column;

    @FXML
    private TableColumn<TaskDTO, String> numOfMiddles_column;

    @FXML
    private TableColumn<TaskDTO, String> numOfRoots_column;

    @FXML
    private TableColumn<TaskDTO, String> numOfIndepepndens_column;

    @FXML
    private TableColumn<TaskDTO, String> totalTaskPrice_column;

    @FXML
    private TableColumn<TaskDTO, String> numberOfWorkers_column;

    @FXML
    private void initialize(){
        loadTaskTableColumns();
        startTaskListRefresher();
    }

    private void loadTaskTableColumns() {
        tasksInSystemTableView.setRowFactory(tv-> {
            TableRow<TaskDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    TaskDTO rowData = row.getItem();
                    System.out.println("Clickedddd");
                    //TODO: what we want to show when we clicking on a task
                }
            });
            return row;
        });
        //I wrote as string the members of the TaskDTO and GraphDTO
        taskname_column.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        creator_name_column.setCellValueFactory(new PropertyValueFactory<>("creatorName"));
        graphName_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        totalNumOfTargets_column.setCellValueFactory(new PropertyValueFactory<>("totalNumOfTargets"));
        numOfLeaves_column.setCellValueFactory(new PropertyValueFactory<>("numOfLeaves"));
        numOfMiddles_column.setCellValueFactory(new PropertyValueFactory<>("numOfMiddles"));
        numOfRoots_column.setCellValueFactory(new PropertyValueFactory<>("numOfRoots"));
        numOfIndepepndens_column.setCellValueFactory(new PropertyValueFactory<>("numOfIndependents"));
        totalTaskPrice_column.setCellValueFactory(new PropertyValueFactory<>("taskTotalPrice"));
        numberOfWorkers_column.setCellValueFactory(new PropertyValueFactory<>("numOfRegisteredWorkers"));
    }


    private void updateTasksList(List<TaskDTO> tasks) {
        Platform.runLater(() -> {
            ObservableList<TaskDTO> taskDTOS = tasksInSystemTableView.getItems();
            taskDTOS.clear();
            taskDTOS.addAll(tasks);
        });
    }

    public void startTaskListRefresher() {
        listRefresher = new TaskListRefresher(
                this::updateTasksList);
        timer = new Timer(true);
        timer.schedule(listRefresher, 15000, 15000);
    }

}
