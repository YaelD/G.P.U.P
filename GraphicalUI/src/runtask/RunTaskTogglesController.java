package runtask;

import dto.GraphDTO;
import dto.TargetDTO;
import engine.Engine;
import graph.Dependency;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import task.RunType;
import task.TaskType;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

public class RunTaskTogglesController {


    private Engine engine;

    private SimpleStringProperty whatIfTarget;
    private ObjectProperty<Dependency> whatIfDependency;

    private SplitPane currSplitPane;

    private ScrollPane compilationToggles;

    private CompilationParamsController compilationTogglesController;

    private ScrollPane simulationToggles;

    private SimulationParamsController simulationTogglesController;


    @FXML
    private ChoiceBox<Integer> numOfThreadsChoiceBox;

    @FXML
    private Button ConfirmButton;

    @FXML
    private VBox targetsSubMenu;

    @FXML
    private CheckBox chooseAllTargectsCheckBox;

    @FXML
    private ListView<CheckBox> targetsCheckBoxList;

    @FXML
    private RadioButton chooseTargetsRB;

    @FXML
    private ToggleGroup chooseTargetsToogleGroup;

    @FXML
    private RadioButton chooseWhatIfRB;

    @FXML
    private ChoiceBox<TaskType> taskTypeChoiceBox;

    @FXML
    private ChoiceBox<RunType> runTypeChoiceBox;

    @FXML
    private VBox whatIfSubMenu;

    @FXML
    private ChoiceBox<String> whatIf_targetsCB;

    @FXML
    private ChoiceBox<Dependency> whayIf_DependencyCB;

    @FXML
    private Button WhatIfButton;

    @FXML
    private ListView<String> whatif_targetsListView;

    public void setCurrSplitPane(SplitPane currSplitPane) {
        this.currSplitPane = currSplitPane;
    }


    public RunTaskTogglesController() {
        this.whatIfDependency = new SimpleObjectProperty<>(Dependency.DEPENDS_ON);
        this.whatIfTarget = new SimpleStringProperty();
    }

    @FXML
    public void initialize(){
        targetsSubMenu.disableProperty().bind(Bindings.not(chooseTargetsRB.selectedProperty()));
        whatIfSubMenu.disableProperty().bind(Bindings.not(chooseWhatIfRB.selectedProperty()));
        taskTypeChoiceBox.getItems().add(TaskType.SIMULATION_TASK);
        taskTypeChoiceBox.getItems().add(TaskType.COMPILATION_TASK);
        taskTypeChoiceBox.getSelectionModel().select(0);
        runTypeChoiceBox.getItems().add(RunType.FROM_SCRATCH);
        runTypeChoiceBox.getItems().add(RunType.INCREMENTAL);
        runTypeChoiceBox.getSelectionModel().select(0);
        whayIf_DependencyCB.getItems().add(Dependency.DEPENDS_ON);
        whayIf_DependencyCB.getItems().add(Dependency.REQUIRED_FOR);
        whayIf_DependencyCB.valueProperty().bindBidirectional(this.whatIfDependency);
        whayIf_DependencyCB.getSelectionModel().select(0);
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


    public void setSelectedRunType(ObjectProperty<RunType> selectedRunType) {
        //runTypeChoiceBox.valueProperty().bind(selectedRunType);
        selectedRunType.bind(runTypeChoiceBox.valueProperty());
    }

    public void setSelectedTaskType(ObjectProperty<TaskType> selectedTaskType) {
        //taskTypeChoiceBox.valueProperty().bind(selectedTaskType);
        selectedTaskType.bind(taskTypeChoiceBox.valueProperty());
    }

    public void setNumOfOfThreads(SimpleIntegerProperty integerProperty){
        integerProperty.bind(this.numOfThreadsChoiceBox.valueProperty());
    }

    @FXML
    void checkTargetsWithWhatIf(ActionEvent event) {
        Set<String> targets = this.engine.whatIf(this.whatIfTarget.get(), this.whatIfDependency.get());
        this.whatif_targetsListView.getItems().clear();
        if(targets.isEmpty()){
            whatif_targetsListView.setPlaceholder(new Label("No targets found"));
        }
        else{
            this.whatif_targetsListView.getItems().addAll(targets);
        }
    }

    @FXML
    void confirm(ActionEvent event) {
        if(taskTypeChoiceBox.getValue().equals(TaskType.SIMULATION_TASK)){
            currSplitPane.getItems().set(1,simulationToggles);
        }
        else{
            currSplitPane.getItems().set(1,compilationToggles);
        }
    }



    public void setEngine(Engine engine){
        this.engine = engine;
        for(int i =1; i <= engine.getMaxNumOfThreads(); ++i){
            this.numOfThreadsChoiceBox.getItems().add(i);
        }
        this.numOfThreadsChoiceBox.getSelectionModel().select(0);
        GraphDTO graphDTO = engine.getGraphDTO();
        for(TargetDTO targetDTO: graphDTO.getTargets().values()){
            this.whatIf_targetsCB.getItems().add(targetDTO.getName());
            CheckBox checkBox = new CheckBox(targetDTO.getName());
            this.targetsCheckBoxList.getItems().add(checkBox);
        }
        whatIf_targetsCB.valueProperty().bindBidirectional(this.whatIfTarget);
        whatIf_targetsCB.getSelectionModel().select(0);
        chooseAllTargectsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue == true){
                    for(CheckBox currCB: targetsCheckBoxList.getItems()){
                        currCB.setSelected(newValue);
                    }
                }
            }
        });


    }

}
