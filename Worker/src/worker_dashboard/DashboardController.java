package worker_dashboard;


import dto.TaskDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;


public class DashboardController {


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

    }

    public void setTaskInfo(TaskDTO taskDTO){
        Platform.runLater(()->{
            //TODO: show the task info


        });
    }


    public void setTopTabPane(TabPane tabs) {
        this.topTabPane = tabs;
    }
}

