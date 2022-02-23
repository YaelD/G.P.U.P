package dashboard;

import RefreshingItems.TaskListRefresherTimer;
import dto.TaskDTO;
import execution_details.RunWindowController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
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
//        tasksInSystemTableView.setRowFactory(tv-> {
//            TableRow<TaskDTO> row = new TableRow<>();
//            TaskDTO rowData = row.getItem();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
//                    addNewTab();
//                }
//            });
//            return row;
//        });
        tasksInSystemTableView.setRowFactory(new Callback<TableView<TaskDTO>, TableRow<TaskDTO>>() {
            @Override
            public TableRow<TaskDTO> call(TableView<TaskDTO> param) {
                TableRow<TaskDTO> row = new TableRow<TaskDTO>(){
                    @Override
                    protected void updateItem(TaskDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null && item.getCreatorName().equals(dashboardController.getUserName())){
                            System.out.println("IN CREATOR NAME: " + item.getCreatorName() + " Equals" + dashboardController.getUserName());
                            setStyle("-fx-background-color: #1fff22;");
                        }
                        else {
                            System.out.println("They are not equals!!");
                            this.setStyle(null);

                        }
                    }
                };
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                        addNewTab();
                    }
                });
                return row;
            }
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

    private void addNewTab(){
        //TODO: open a new tab with the new task details

        if(tasksInSystemTableView.getSelectionModel().getSelectedItem() != null){
            TaskDTO task = tasksInSystemTableView.getSelectionModel().getSelectedItem();
            if(task.getCreatorName().equals(this.dashboardController.getUserName()) ){
                Parent scene = loadTaskRunScene(task);

                dashboardController.addTab(task.getTaskName(),scene);
            }
        }

    }

    private Parent loadTaskRunScene(TaskDTO task) {
        URL resource = RunWindowController.class.getResource("task_window_details.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        Parent root = null;
        try {
            root = fxmlLoader.load(resource.openStream());
            RunWindowController runWindowController = fxmlLoader.getController();
            runWindowController.setTaskDTOProperty(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }


    private void updateTasksList(List<TaskDTO> tasks) {
        Platform.runLater(() -> {
            ObservableList<TaskDTO> taskDTOS = tasksInSystemTableView.getItems();
            taskDTOS.clear();
            taskDTOS.addAll(tasks);

        });
    }

    public void startTaskListRefresher() {
        TaskListRefresherTimer.getInstance().addConsumer(this::updateTasksList);



//        listRefresher = new TaskListRefresher(
//                this::updateTasksList);
//        timer = new Timer(true);
//        timer.schedule(listRefresher, 15000, 15000);
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}
