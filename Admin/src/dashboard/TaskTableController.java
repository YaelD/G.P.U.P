package dashboard;

import RefreshingItems.GraphListRefresher;
import RefreshingItems.TaskListRefresher;
import dto.GraphDTO;
import dto.TaskDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
    private DashboardController dashboardController;

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
            TaskDTO rowData = row.getItem();
            if(rowData.getCreatorName().equals(dashboardController.getUserName())){
                row.setStyle("-fx-background-color:#44f144");
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                        addNewTab(rowData);
                    }
                });
            }

            return row;
        });
        taskname_column.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        creator_name_column.setCellValueFactory(new PropertyValueFactory<>("creatorName"));
        totalTaskPrice_column.setCellValueFactory(new PropertyValueFactory<>("taskTotalPrice"));
        numberOfWorkers_column.setCellValueFactory(new PropertyValueFactory<>("numOfRegisteredWorkers"));
        initGraphInfo();
    }


    private void initGraphInfo(){
        graphName_column.setCellValueFactory( (callback)->{
            return new SimpleStringProperty(callback.getValue().getGraphDTO().getName());
        });
        totalNumOfTargets_column.setCellValueFactory((callback)->{
            return new SimpleStringProperty(String.valueOf(callback.getValue().getGraphDTO().getTotalNumOfTargets()));
        });
        numOfLeaves_column.setCellValueFactory((callback)->{
            return new SimpleStringProperty(String.valueOf(callback.getValue().getGraphDTO().getNumOfLeaves()));
        });;
        numOfRoots_column.setCellValueFactory((callback)->{
            return new SimpleStringProperty(String.valueOf(callback.getValue().getGraphDTO().getNumOfRoots()));
        });;
        numOfMiddles_column.setCellValueFactory((callback)->{
            return new SimpleStringProperty(String.valueOf(callback.getValue().getGraphDTO().getNumOfMiddles()));
        });;
        numOfIndepepndens_column.setCellValueFactory((callback)->{
            return new SimpleStringProperty(String.valueOf(callback.getValue().getGraphDTO().getNumOfIndependents()));
        });;

    }

    private void addNewTab(TaskDTO task){
        //TODO: open a new tab with the new task details




        //dashboardController.addTab(task.getTaskName(), );

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

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}
