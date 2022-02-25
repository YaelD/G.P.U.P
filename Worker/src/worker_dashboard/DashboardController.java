package worker_dashboard;


import dto.TaskDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import worker_engine.WorkerEngine;


public class DashboardController {

    private String userName;

    @FXML
    private GridPane registerToTaskPane;

    @FXML
    private RegisterToTaskController registerToTaskPaneController;

    @FXML
    private GridPane usersList;

    @FXML
    private UsersListController usersListController;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private TableView<TaskDTO> taskInfoTable;

    @FXML
    private TaskTableController taskInfoTableController;
    private TabPane topTabPane;



    @FXML
    private void initialize(){
        registerToTaskPaneController.taskNameProperty().bind(taskInfoTableController.selectedTaskNameProperty());
        taskInfoTableController.setDashboardController(this);
        totalIncomeLabel.textProperty().bind(Bindings.convert(WorkerEngine.getInstance().totalCreditsProperty()));

    }

    public void setTaskInfo(TaskDTO taskDTO){
        Platform.runLater(()->{
            //TODO: show the task info


        });
    }


    public void setTopTabPane(TabPane tabs) {
        this.topTabPane = tabs;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

