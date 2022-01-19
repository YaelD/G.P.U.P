package runtask;

import engine.Engine;
import javafx.beans.property.*;
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

    @FXML private GridPane chooseTargetsToggles;
    @FXML private ChooseTargetsController chooseTargetsTogglesController;

    @FXML private GridPane chooseThreadsAndTaskToggles;
    @FXML private ChooseTaskController chooseThreadsAndTaskTogglesController;

    @FXML private GridPane simulationTaskToggles;
    @FXML private SimulationParamsController simulationTaskTogglesController;

    @FXML private GridPane compilationTaskToggles;
    @FXML private CompilationParamsController compilationTaskTogglesController;

    private SimpleListProperty<String> targetsList;
    private ObjectProperty<TaskType> taskType;
    private ObjectProperty<RunType> runType;
    private SimpleIntegerProperty numOfThreads;

    Engine engine;

    public RunTaskMenuController(){
        this.taskType = new SimpleObjectProperty<>();
        this.runType = new SimpleObjectProperty<>();
        this.targetsList = new SimpleListProperty<>();
        this.numOfThreads = new SimpleIntegerProperty();
    }


    @FXML
    private void initialize(){
        this.chooseTargetsTogglesController.setTargetsList(targetsList);
    }

    public void setEngine(Engine engine){
        this.engine = engine;
        this.chooseTargetsTogglesController.setEngine(engine);
    }


}
