package runtask;

import dto.CompilationTaskParamsDTO;
import dto.SimulationTaskParamsDTO;
import dto.TaskParamsDTO;
import engine.Engine;
import engine.SystemEngine;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import task.RunType;
import task.TaskType;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class RunTaskMenuController {

    @FXML private HBox baseHBox;

    @FXML private GridPane chooseTargetsToggles;
    @FXML private ChooseTargetsController chooseTargetsTogglesController;

    @FXML private GridPane chooseThreadsAndTaskToggles;
    @FXML private ChooseTaskController chooseThreadsAndTaskTogglesController;

    @FXML private GridPane simulationTaskToggles;
    @FXML private SimulationParamsController simulationTaskTogglesController;

    @FXML private GridPane compilationTaskToggles;
    @FXML private CompilationParamsController compilationTaskTogglesController;

    private SimpleListProperty<String> targetsList;
    private SimpleObjectProperty<TaskType> taskType;
    private SimpleObjectProperty<RunType> runType;
    private SimpleIntegerProperty numOfThreads;

    private SimpleObjectProperty<SimulationTaskParamsDTO> simulationTaskParamsDTO;
    private SimpleObjectProperty<CompilationTaskParamsDTO> compilationTaskParamsDTO;



    private Engine engine;

    public RunTaskMenuController(){
        this.taskType = new SimpleObjectProperty<>();
        this.runType = new SimpleObjectProperty<>();
        this.targetsList = new SimpleListProperty<>();
        this.numOfThreads = new SimpleIntegerProperty();
        this.compilationTaskParamsDTO = new SimpleObjectProperty<>();
        this.simulationTaskParamsDTO = new SimpleObjectProperty<>();
    }


    @FXML
    private void initialize(){
        this.chooseTargetsTogglesController.setTargetsList(targetsList);
        this.chooseThreadsAndTaskTogglesController.setCompilationLayout(this.compilationTaskToggles);
        this.chooseThreadsAndTaskTogglesController.setSimulationLayout(this.simulationTaskToggles);
        baseHBox.getChildren().remove(simulationTaskToggles);
        baseHBox.getChildren().remove(compilationTaskToggles);
        this.chooseThreadsAndTaskTogglesController.setMenuPane(baseHBox);
        this.chooseThreadsAndTaskTogglesController.setTaskTypeAndRunTypeListeners(taskType, runType);
        this.chooseThreadsAndTaskTogglesController.setNumOfThreads(this.numOfThreads);
        ActiveTaskCallback callback = new ActiveTaskCallback() {
            @Override
            public void activeTask(TaskParamsDTO taskParams) {
                URL resource = getClass().getResource("run_task_popup.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(resource);
                BorderPane root = null;
                try {
                    root = fxmlLoader.load(resource.openStream());
                    Scene scene = new Scene(root, 1000, 600);
                    RunWindowController runWindowController = fxmlLoader.getController();
                    runWindowController.setEngine(engine);
                    Set<String> targetSet = new HashSet<>();
                    targetSet.addAll(targetsList.getValue());
                    runWindowController.runTask(taskParams, numOfThreads.getValue(),
                            taskType.getValue(), runType.getValue(), targetSet);
                    Stage secondaryStage = new Stage();
                    secondaryStage.setScene(scene);
                    secondaryStage.setTitle("Run task");
                    secondaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        this.simulationTaskTogglesController.setActiveTaskCallback(callback);
        this.compilationTaskTogglesController.setActiveTaskCallback(callback);
    }

    public void setEngine(Engine engine){
        this.engine = engine;
        this.chooseTargetsTogglesController.setEngine(engine);
        this.chooseThreadsAndTaskTogglesController.setEngine(engine);
    }




}
