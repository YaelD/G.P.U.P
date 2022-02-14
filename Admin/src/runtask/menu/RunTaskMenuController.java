package runtask.menu;

//import engine.Engine;
//import graph.Dependency;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import runtask.compilation_task.CompilationParamsController;
import runtask.running_task_window.RunWindowController;
import runtask.simulation_task.SimulationParamsController;
//import task.RunType;
//import task.TaskType;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class RunTaskMenuController {

    @FXML private HBox baseHBox;



    @FXML private GridPane simulationTaskToggles;
    @FXML private SimulationParamsController simulationTaskTogglesController;

    @FXML private GridPane compilationTaskToggles;
    @FXML private CompilationParamsController compilationTaskTogglesController;

    private SimpleListProperty<String> targetsList;
//    private SimpleObjectProperty<TaskType> taskType;
//    private SimpleObjectProperty<RunType> runType;
    private SimpleIntegerProperty numOfThreads;

    @FXML private VBox targetsSubMenu;
    @FXML private CheckBox chooseAllTargetsCheckBox;
    @FXML private ListView<CheckBox> targetsCheckBoxList;
    @FXML private RadioButton chooseTargetsRB;
    @FXML private ToggleGroup chooseTargetsToogleGroup;
    @FXML private RadioButton chooseWhatIfRB;
    @FXML private VBox whatIfSubMenu;
    @FXML private ChoiceBox<String> whatIf_targetsCB;
//    @FXML private ChoiceBox<Dependency> whatIf_DependencyCB;
    @FXML private Button WhatIfButton;
    @FXML private ListView<String> selectedTargetsListView;
    @FXML private Button continueButton;
    //Threads and task controllers
    @FXML private ChoiceBox<Integer> numOfThreadsCB;
    @FXML private RadioButton simulationRadioButton;
    @FXML private ToggleGroup taskToggleGroup;
    @FXML private RadioButton compilationRadioButton;
    @FXML private RadioButton fromScratchRadioButton;
    @FXML private ToggleGroup runTypeToggle;
    @FXML private RadioButton incrementalRadioButton;
    @FXML private Label warningLabel;




//    private Engine engine;



//    public RunTaskMenuController(){
//        this.taskType = new SimpleObjectProperty<>(TaskType.SIMULATION_TASK);
//        this.runType = new SimpleObjectProperty<>(RunType.FROM_SCRATCH);
//        this.targetsList = new SimpleListProperty<>();
//        this.numOfThreads = new SimpleIntegerProperty(1);
//    }



//
//
//    @FXML
//    void checkTargetsWithWhatIf(ActionEvent event) {
//
////        Set<String> whatIfTargets = this.engine.whatIfForRunningTask(whatIf_targetsCB.getValue(),
////                whatIf_DependencyCB.getValue(), taskType.getValue(), runType.getValue());
//        selectedTargetsListView.getItems().clear();
//        selectedTargetsListView.getItems().add(whatIf_targetsCB.getValue());
//        //selectedTargetsListView.getItems().addAll(whatIfTargets);
//
//    }
//
//
//
//
//
//    @FXML
//    void clearAllTargets(ActionEvent event) {
//        for(CheckBox checkBox: targetsCheckBoxList.getItems()){
//            checkBox.setSelected(false);
//        }
//        selectedTargetsListView.getItems().clear();
//        chooseAllTargetsCheckBox.setSelected(false);
//    }
//
//    @FXML
//    void enableNextPanel(ActionEvent event) {
//        warningLabel.setVisible(false);
//        if(!validation()){
//        }
//        else if(simulationRadioButton.isSelected()){
//            baseHBox.getChildren().remove(compilationTaskToggles);
//            try{
//                baseHBox.getChildren().add(simulationTaskToggles);
//                baseHBox.getChildren().get(0).setDisable(true);
//                baseHBox.getChildren().get(1).setDisable(true);
//                simulationTaskToggles.setMaxHeight(Double.MAX_VALUE);
//                simulationTaskToggles.setMaxWidth(Double.MAX_VALUE);
//            }catch (IllegalArgumentException e){
//
//            }
//        }
//        else{
//            baseHBox.getChildren().remove(simulationTaskToggles);
//            try{
//                baseHBox.getChildren().add(compilationTaskToggles);
//                baseHBox.getChildren().get(0).setDisable(true);
//                baseHBox.getChildren().get(1).setDisable(true);
//                compilationTaskToggles.setMaxHeight(Double.MAX_VALUE);
//                compilationTaskToggles.setMaxWidth(Double.MAX_VALUE);
//            }catch (IllegalArgumentException e){
//
//            }
//        }
//
//
//
//
//    }
//
//
//    @FXML
//    private void initialize(){
//        baseHBox.getChildren().remove(simulationTaskToggles);
//        baseHBox.getChildren().remove(compilationTaskToggles);
//        targetsList.bind(selectedTargetsListView.itemsProperty());
//        initTaskChoiceController();
//        initTargetChoiceControllers();
//        ActiveTaskCallback activeTaskCallback = new ActiveTaskCallback() {
//            @Override
//            public void activeTask(TaskParamsDTO taskParams) {
//                if(targetsList.isEmpty()){
//                    return;
//                }
//                URL resource = RunWindowController.class.getResource("run_task_running_task_window.fxml");
//                FXMLLoader fxmlLoader = new FXMLLoader();
//                fxmlLoader.setLocation(resource);
//                BorderPane root = null;
//                try {
//                    root = fxmlLoader.load(resource.openStream());
//                    Scene scene = new Scene(root, 1000, 600);
//                    RunWindowController runWindowController = fxmlLoader.getController();
//                    runWindowController.setEngine(engine);
//                    Set<String> targetSet = new HashSet<>();
//                    targetSet.addAll(targetsList.getValue());
//                    Stage secondaryStage = new Stage();
//                    secondaryStage.initModality(Modality.APPLICATION_MODAL);
//                    secondaryStage.setScene(scene);
//                    secondaryStage.setTitle("Run task");
//                    secondaryStage.show();
//                    runWindowController.runTask(taskParams, numOfThreads.getValue(),
//                            taskType.getValue(), runType.getValue(), targetSet);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        simulationTaskTogglesController.setActiveTaskCallback(activeTaskCallback);
//        simulationTaskTogglesController.setReturnCallBack(new ReturnCallback() {
//            @Override
//            public void returnToPrev() {
//                baseHBox.getChildren().remove(simulationTaskToggles);
//                baseHBox.getChildren().get(0).setDisable(false);
//                baseHBox.getChildren().get(1).setDisable(false);
//
//            }
//        });
//        compilationTaskTogglesController.setActiveTaskCallback(activeTaskCallback);
//        compilationTaskTogglesController.setReturnCallBack(new ReturnCallback() {
//            @Override
//            public void returnToPrev() {
//                baseHBox.getChildren().remove(compilationTaskToggles);
//                baseHBox.getChildren().get(0).setDisable(false);
//                baseHBox.getChildren().get(1).setDisable(false);
//            }
//        });
//
//    }
//
//    private boolean validation() {
//        Set<String> targetSet = new HashSet<>();
//        targetSet.addAll(this.targetsList);
//        if(this.runType.getValue().equals(RunType.INCREMENTAL) && !engine.isRunInIncrementalMode(this.taskType.getValue(),targetSet )){
//            warningLabel.setVisible(true);
//            warningLabel.setText("The " + taskType.getValue().getTaskType() + " task cannot run incrementally" +
//                    "\nSetting run From Scratch by default");
//            this.runType.setValue(RunType.FROM_SCRATCH);
//            this.runTypeToggle.selectToggle(fromScratchRadioButton);
//            return false;
//        }
//        if(targetSet.isEmpty()){
//            warningLabel.setVisible(true);
//            warningLabel.setText("Please choose targets");
//            return false;
//        }
//        return true;
//    }
//
//    private void initTargetChoiceControllers(){
//        whatIfSubMenu.disableProperty().bind(Bindings.not(chooseWhatIfRB.selectedProperty()));
//        targetsSubMenu.disableProperty().bind(Bindings.not(chooseTargetsRB.selectedProperty()));
//        chooseWhatIfRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                selectedTargetsListView.getItems().clear();
//            }
//        });
//        chooseTargetsRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                selectedTargetsListView.getItems().clear();
//                if(newValue == false){
//                    for(CheckBox currCheckBox: targetsCheckBoxList.getItems()){
//                        currCheckBox.selectedProperty().set(false);
//                    }
//                    chooseAllTargetsCheckBox.selectedProperty().set(false);
//                }
//            }
//        });
//        chooseAllTargetsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if(newValue.booleanValue() == true){
//                    for(CheckBox checkBox: targetsCheckBoxList.getItems()){
//                        checkBox.setSelected(newValue);
//                    }
//                }
//            }
//        });
//        whatIf_DependencyCB.getItems().addAll(Dependency.DEPENDS_ON, Dependency.REQUIRED_FOR);
//        whatIf_DependencyCB.setValue(whatIf_DependencyCB.getItems().get(0));
//
//    }
//
//    private void initTaskChoiceController() {
//        taskToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//            @Override
//            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                if(newValue == simulationRadioButton){
//                    taskType.set(TaskType.SIMULATION_TASK);
//                    baseHBox.getChildren().remove(compilationTaskToggles);
//                }
//                else if(newValue == compilationRadioButton){
//                    taskType.set(TaskType.COMPILATION_TASK);
//                    baseHBox.getChildren().remove(simulationTaskToggles);
//                }
//                warningLabel.setVisible(false);
//
//            }
//        });
//        runTypeToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//            @Override
//            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                if(newValue == incrementalRadioButton){
//                    runType.set(RunType.INCREMENTAL);
//                    Set<String> targets = engine.getTaskGraphInSystem(taskType.getValue());
//                    initTargetsCheckBoxList(targets);
//                    baseHBox.getChildren().remove(simulationTaskToggles);
//                    baseHBox.getChildren().remove(compilationTaskToggles);
//                }
//                else if(newValue == fromScratchRadioButton){
//                    initLists(engine.getGraphDTO());
//                    runType.set(RunType.FROM_SCRATCH);
//                    baseHBox.getChildren().remove(simulationTaskToggles);
//                    baseHBox.getChildren().remove(compilationTaskToggles);
//                }
//
//                for(CheckBox checkBox: targetsCheckBoxList.getItems()){
//                    checkBox.setSelected(false);
//                }
//                chooseAllTargetsCheckBox.setSelected(false);
//                selectedTargetsListView.getItems().clear();
//
//
//            }
//        });
//    }
//
//    public void setEngine(Engine engine){
//        this.engine = engine;
//        GraphDTO graphDTO = engine.getGraphDTO();
//
//        initLists(graphDTO);
//
//        for(int i=1; i<= this.engine.getMaxNumOfThreads(); i++){
//            numOfThreadsCB.getItems().add(i);
//        }
//        this.numOfThreadsCB.getSelectionModel().select(0);
//        this.numOfThreads.bind(numOfThreadsCB.valueProperty());
//    }
//
//    private void initTargetsCheckBoxList(Set<String> optionalTargetsSet){
//        if(!optionalTargetsSet.isEmpty()){
//            targetsCheckBoxList.getItems().clear();
//            whatIf_targetsCB.getItems().clear();
//            for(String currTarget: optionalTargetsSet){
//                whatIf_targetsCB.getItems().add(currTarget);
//                CheckBox checkBox = new CheckBox(currTarget);
//                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                        if(newValue == true){
//                            selectedTargetsListView.getItems().add(checkBox.getText());
//                        }
//                        else{
//                            selectedTargetsListView.getItems().remove(checkBox.getText());
//                        }
//                    }
//                });
//                targetsCheckBoxList.getItems().add(checkBox);
//            }
//            whatIf_targetsCB.setValue(whatIf_targetsCB.getItems().get(0));
//
//        }
//    }
//
//
//    private void initLists(GraphDTO graphDTO) {
//        targetsCheckBoxList.getItems().clear();
//        whatIf_targetsCB.getItems().clear();
//        for(TargetDTO targetDTO: graphDTO.getTargets().values()){
//            whatIf_targetsCB.getItems().add(targetDTO.getName());
//            CheckBox checkBox = new CheckBox(targetDTO.getName());
//            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if(newValue == true){
//                        selectedTargetsListView.getItems().add(checkBox.getText());
//                    }
//                    else{
//                        selectedTargetsListView.getItems().remove(checkBox.getText());
//                    }
//                }
//            });
//            targetsCheckBoxList.getItems().add(checkBox);
//        }
//        whatIf_targetsCB.setValue(whatIf_targetsCB.getItems().get(0));
//    }
//

}
