package tasks_menu;

import dto.TargetDTO;
import dto.TaskDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import worker_engine.WorkerEngine;

import javax.naming.Binding;

public class TaskMenuController {

    private SimpleIntegerProperty totalNumOfThreads;

    private SimpleStringProperty userName;

    @FXML
    private Label numOfThreadsLabel;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private TableView<TaskDTO> taskInfoTable;

    @FXML
    private TasksInfoTableController taskInfoTableController;

    @FXML
    private TableView<TargetDTO> targetInfoTable;

    @FXML
    private TargetsInfoTableController targetInfoTableController;



    @FXML
    private TextArea targetLogConsole;

    @FXML
    private void initialize(){
        totalIncomeLabel.textProperty().bind(Bindings.convert(WorkerEngine.getInstance().totalCreditsProperty()));
        targetInfoTableController.setTaskMenuController(this);
    }

    public TaskMenuController() {
        this.userName = new SimpleStringProperty();
        this.totalNumOfThreads = new SimpleIntegerProperty();
    }

    public void setTotalNumOfThreads(SimpleIntegerProperty totalNumOfThreads) {
        this.totalNumOfThreads.bind(totalNumOfThreads);
        numOfThreadsLabel.textProperty().bind(Bindings.format("%d/%d", WorkerEngine.getInstance().numOfFreeThreadsProperty(),totalNumOfThreads));

    }

    public void showInConsole(String str){
        Platform.runLater(()->{
            this.targetLogConsole.setText(str);
        });
    }


    public void setUserName(SimpleStringProperty userName) {
        this.userName.bind(userName);
    }
}
