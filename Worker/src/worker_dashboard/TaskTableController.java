package worker_dashboard;

import RefreshingItems.TaskListRefresherTimer;
import dto.TaskDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TaskTableController {

    private Timer timer;
    private TimerTask listRefresher;

    private SimpleStringProperty selectedTaskName;

    @FXML
    private TableView<TaskDTO> tasksInSystemTableView;

    @FXML
    private TableColumn<TaskDTO, String> name_column;

    @FXML
    private TableColumn<TaskDTO, String> creator_name_column;

    @FXML
    private TableColumn<TaskDTO, String> task_type_column;

    @FXML
    private TableColumn<TaskDTO, String> target_price_column;

    @FXML
    private TableColumn<TaskDTO, String> task_status_column;

    @FXML
    private TableColumn<TaskDTO, String> number_of_workers_column;

    @FXML
    private TableColumn<TaskDTO, String> totalNumOfTargets_column;

    @FXML
    private TableColumn<TaskDTO, String> numOfLeaves_column;

    @FXML
    private TableColumn<TaskDTO, String> numOfMiddles_column;

    @FXML
    private TableColumn<TaskDTO, String> numOfRoots_column;

    @FXML
    private TableColumn<TaskDTO, String> numOfIndependents_column;


    public TaskTableController() {
        this.selectedTaskName = new SimpleStringProperty();
    }

    @FXML
    private void initialize(){
        tasksInSystemTableView.setRowFactory(tv-> {
            TableRow<TaskDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    TaskDTO rowData = row.getItem();
                    this.selectedTaskName.set(rowData.getTaskName());
                    showTaskDetails(rowData);
                    //TODO: what we want to show when we clicking on a task
                }
            });
            return row;
        });
        name_column.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        creator_name_column.setCellValueFactory(new PropertyValueFactory<>("creatorName"));
        number_of_workers_column.setCellValueFactory(new PropertyValueFactory<>("numOfRegisteredWorkers"));
        task_type_column.setCellValueFactory(new PropertyValueFactory<>("taskType"));
        target_price_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TaskDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TaskDTO, String> param) {
                int pricePerTarget = param.getValue().getTaskTotalPrice()/param.getValue().getGraphDTO().getTotalNumOfTargets();
                return new SimpleStringProperty(String.valueOf(pricePerTarget));
            }
        });
        task_status_column.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));
        initGraphProperties();
        startTaskListRefresher();
    }

    private void initGraphProperties() {
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
        numOfIndependents_column.setCellValueFactory((callback)->{
            return new SimpleStringProperty(String.valueOf(callback.getValue().getGraphDTO().getNumOfIndependents()));
        });;

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

    public String getSelectedTaskName() {
        return selectedTaskName.get();
    }

    public SimpleStringProperty selectedTaskNameProperty() {
        return selectedTaskName;
    }


    //taksNameTosend = selecteTaskName
    private void showTaskDetails(TaskDTO taskDTO){
        try {
            URL resource = TaskDetailsPopupController.class.getResource("task_details_popup.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(resource);
            Parent root = fxmlLoader.load(resource.openStream());
            Scene scene = new Scene(root, 1000, 600);
            TaskDetailsPopupController taskDetailsPopupController = fxmlLoader.getController();
            taskDetailsPopupController.setTaskDTO(taskDTO);
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(scene);
            secondaryStage.setTitle(taskDTO.getTaskName());
            secondaryStage.setMinHeight(400);
            secondaryStage.setMinWidth(400);
            secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
