package runtask;

import engine.Engine;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import task.RunType;
import task.TaskType;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RunTaskMenuController {

    SimpleIntegerProperty numOfThreads;
    ObjectProperty<TaskType> selectedTaskType;
    ObjectProperty<RunType> selectedRunType;



    @FXML
    private SplitPane menuSplitPane;

    @FXML
    private GridPane taskToggles;

    @FXML
    private RunTaskTogglesController taskTogglesController;


    private Engine engine;


    public RunTaskMenuController() {
        this.selectedRunType = new SimpleObjectProperty<>(RunType.FROM_SCRATCH);
        this.selectedTaskType = new SimpleObjectProperty<>(TaskType.SIMULATION_TASK);
        this.numOfThreads = new SimpleIntegerProperty();
    }

    public void initialize() {
        this.taskTogglesController.setCurrSplitPane(this.menuSplitPane);
    }



    public void setEngine(Engine engine) {
        this.engine = engine;
        this.taskTogglesController.setEngine(engine);
        this.taskTogglesController.setSelectedTaskType(this.selectedTaskType);
        this.taskTogglesController.setSelectedRunType(this.selectedRunType);
        this.taskTogglesController.setNumOfOfThreads(this.numOfThreads);






    }
}