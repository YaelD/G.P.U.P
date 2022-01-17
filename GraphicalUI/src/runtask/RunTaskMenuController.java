package runtask;

import engine.Engine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import task.TaskType;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RunTaskMenuController {

    private Stage primaryStage;

    @FXML
    private SplitPane menuSplitPane;

    @FXML
    private GridPane taskToggles;

    @FXML
    private RunTaskTogglesController taskTogglesController;

    private ScrollPane compilationToggles;

    private CompilationParamsController compilationTogglesController;

    private ScrollPane simulationToggles;

    private SimulationParamsController simulationTogglesController;

    private Engine engine;


    public void initialize() {
        initCompilationToggles();
        initSimulationToggles();
    }

    private void initSimulationToggles(){
        URL resource = SimulationParamsController.class.getResource("simlation_task_toggles.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            this.simulationToggles = fxmlLoader.load(resource.openStream());
            this.simulationTogglesController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initCompilationToggles(){
        URL resource = CompilationParamsController.class.getResource("compilation_task_toggles.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            this.compilationToggles = fxmlLoader.load(resource.openStream());
            this.compilationTogglesController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
        this.taskTogglesController.setEngine(engine);
        this.taskTogglesController.setTaskRunMenuCallback(new chooseTaskCallback() {
            @Override
            public void loadSpecificTaskToggles(List<String> targetsName, TaskType taskType) {
                menuSplitPane.getItems().remove(1);
                switch (taskType){
                    case SIMULATION_TASK:
                        menuSplitPane.getItems().add(simulationToggles);
                        break;
                    case COMPILATION_TASK:
                        menuSplitPane.getItems().add(compilationToggles);
                }
            }
        });
    }
}