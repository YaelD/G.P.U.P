package runtask.menu;

import com.google.gson.Gson;
import constants.Constants;
import dto.*;
import general_enums.RunType;
import general_enums.TaskType;
import http_utils.HttpUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import runtask.compilation_task.CompilationParamsController;
import runtask.simulation_task.SimulationParamsController;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RunTaskMenuController {


    GraphDTO currGraph;


    @FXML private HBox baseHBox;



    @FXML private GridPane simulationTaskToggles;
    @FXML private SimulationParamsController simulationTaskTogglesController;

    @FXML private GridPane compilationTaskToggles;
    @FXML private CompilationParamsController compilationTaskTogglesController;

    private SimpleListProperty<String> targetsList;
    private SimpleObjectProperty<TaskType> taskType;
    private SimpleObjectProperty<RunType> runType;





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

    @FXML private RadioButton simulationRadioButton;
    @FXML private ToggleGroup taskToggleGroup;
    @FXML private RadioButton compilationRadioButton;
    @FXML private RadioButton fromScratchRadioButton;
    @FXML private ToggleGroup runTypeToggle;
    @FXML private RadioButton incrementalRadioButton;
    @FXML private Label warningLabel;




    public RunTaskMenuController(){
        this.taskType = new SimpleObjectProperty<>(TaskType.SIMULATION_TASK);
        this.runType = new SimpleObjectProperty<>(RunType.FROM_SCRATCH);
        this.targetsList = new SimpleListProperty<>();

    }

    public void disableIncremental(boolean allowIncremental){
        this.incrementalRadioButton.setDisable(!allowIncremental);
    }



    @FXML
    void checkTargetsWithWhatIf(ActionEvent event) {
    }


    @FXML
    void clearAllTargets(ActionEvent event) {
        for(CheckBox checkBox: targetsCheckBoxList.getItems()){
            checkBox.setSelected(false);
        }
        selectedTargetsListView.getItems().clear();
        chooseAllTargetsCheckBox.setSelected(false);
    }

    @FXML
    void enableNextPanel(ActionEvent event) {
        warningLabel.setVisible(false);
        if(!validation()){
        }
        else if(simulationRadioButton.isSelected()){
            baseHBox.getChildren().remove(compilationTaskToggles);
            try{
                baseHBox.getChildren().add(simulationTaskToggles);
                baseHBox.getChildren().get(0).setDisable(true);
                baseHBox.getChildren().get(1).setDisable(true);
                simulationTaskToggles.setMaxHeight(Double.MAX_VALUE);
                simulationTaskToggles.setMaxWidth(Double.MAX_VALUE);
            }catch (IllegalArgumentException e){

            }
        }
        else{
            baseHBox.getChildren().remove(simulationTaskToggles);
            try{
                baseHBox.getChildren().add(compilationTaskToggles);
                baseHBox.getChildren().get(0).setDisable(true);
                baseHBox.getChildren().get(1).setDisable(true);
                compilationTaskToggles.setMaxHeight(Double.MAX_VALUE);
                compilationTaskToggles.setMaxWidth(Double.MAX_VALUE);
            }catch (IllegalArgumentException e){

            }
        }

    }


    @FXML
    private void initialize(){
        baseHBox.getChildren().remove(simulationTaskToggles);
        baseHBox.getChildren().remove(compilationTaskToggles);
        simulationTaskTogglesController.bindProperties(this.targetsList,this.runType);
        compilationTaskTogglesController.bindProperties(this.targetsList,this.runType);
        targetsList.bind(selectedTargetsListView.itemsProperty());
        ActiveTaskCallback activeTaskCallback = new ActiveTaskCallback() {
            @Override
            public void sendTask(TaskParamsDTO taskParams) {
                Gson gson = new Gson();
                String params = gson.toJson(taskParams);
                Request request = new Request.Builder().url(Constants.TASK_LIST)
                        .post(RequestBody.create(params.getBytes())).build();
                HttpUtils.runAsyncPost(request, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        //TODO: failure
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(response.code() == 200){
                            warningLabel.setVisible(true);
                            warningLabel.setText("Yayyyyyyyyyyyy");
                        }
                        else{
                            warningLabel.setVisible(true);
                            warningLabel.setText(":(((((((((((((((((((");

                        }
                    }
                });
            }
        };
        simulationTaskTogglesController.setActiveTaskCallback(activeTaskCallback);
        simulationTaskTogglesController.setReturnCallBack(new ReturnCallback() {
            @Override
            public void returnToPrev() {
                baseHBox.getChildren().remove(simulationTaskToggles);
                baseHBox.getChildren().get(0).setDisable(false);
                baseHBox.getChildren().get(1).setDisable(false);

            }
        });
        compilationTaskTogglesController.setActiveTaskCallback(activeTaskCallback);
        compilationTaskTogglesController.setReturnCallBack(new ReturnCallback() {
            @Override
            public void returnToPrev() {
                baseHBox.getChildren().remove(compilationTaskToggles);
                baseHBox.getChildren().get(0).setDisable(false);
                baseHBox.getChildren().get(1).setDisable(false);
            }
        });

    }

    private boolean validation() {
        Set<String> targetSet = new HashSet<>();
        targetSet.addAll(this.targetsList);
        if(!this.incrementalRadioButton.isDisabled()){
            warningLabel.setVisible(true);
            warningLabel.setText("The " + taskType.getValue().getTaskType() + " task cannot run incrementally" +
                    "\nSetting run From Scratch by default");
            this.runType.setValue(RunType.FROM_SCRATCH);
            this.runTypeToggle.selectToggle(fromScratchRadioButton);
            return false;
        }
        if(targetSet.isEmpty()){
            warningLabel.setVisible(true);
            warningLabel.setText("Please choose targets");
            return false;
        }
        return true;
    }

    private void initTargetChoiceControllers(){
        whatIfSubMenu.disableProperty().bind(Bindings.not(chooseWhatIfRB.selectedProperty()));
        targetsSubMenu.disableProperty().bind(Bindings.not(chooseTargetsRB.selectedProperty()));
        chooseWhatIfRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                selectedTargetsListView.getItems().clear();
            }
        });
        chooseTargetsRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                selectedTargetsListView.getItems().clear();
                if(newValue == false){
                    for(CheckBox currCheckBox: targetsCheckBoxList.getItems()){
                        currCheckBox.selectedProperty().set(false);
                    }
                    chooseAllTargetsCheckBox.selectedProperty().set(false);
                }
            }
        });
        chooseAllTargetsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue.booleanValue() == true){
                    for(CheckBox checkBox: targetsCheckBoxList.getItems()){
                        checkBox.setSelected(newValue);
                    }
                }
            }
        });

    }

    private void initTaskChoiceController() {
        simulationRadioButton.setDisable(currGraph.getPriceOfSimulationTask() == 0);
        compilationRadioButton.setDisable(currGraph.getPriceOfCompilationTask() == 0);

        taskToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == simulationRadioButton){
//                    taskType.set(TaskType.SIMULATION_TASK);
                    baseHBox.getChildren().remove(compilationTaskToggles);
                }
                else if(newValue == compilationRadioButton){
//                    taskType.set(TaskType.COMPILATION_TASK);
                    baseHBox.getChildren().remove(simulationTaskToggles);
                }
                warningLabel.setVisible(false);

            }
        });
        runTypeToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == incrementalRadioButton){
                    runType.set(RunType.INCREMENTAL);
                    baseHBox.getChildren().remove(simulationTaskToggles);
                    baseHBox.getChildren().remove(compilationTaskToggles);
                }
                else if(newValue == fromScratchRadioButton){
                    initLists(currGraph);
                    runType.set(RunType.FROM_SCRATCH);
                    baseHBox.getChildren().remove(simulationTaskToggles);
                    baseHBox.getChildren().remove(compilationTaskToggles);
                }

                for(CheckBox checkBox: targetsCheckBoxList.getItems()){
                    checkBox.setSelected(false);
                }
                chooseAllTargetsCheckBox.setSelected(false);
                selectedTargetsListView.getItems().clear();


            }
        });
    }

    public void setCurrGraph(GraphDTO currGraph) {
        this.currGraph = currGraph;
        initLists(currGraph);
        initTaskChoiceController();
        initTargetChoiceControllers();
    }


        private void initLists(GraphDTO graphDTO) {
        targetsCheckBoxList.getItems().clear();
        whatIf_targetsCB.getItems().clear();
        for(TargetDTO targetDTO: graphDTO.getTargets().values()){
            whatIf_targetsCB.getItems().add(targetDTO.getName());
            CheckBox checkBox = new CheckBox(targetDTO.getName());
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue == true){
                        selectedTargetsListView.getItems().add(checkBox.getText());
                    }
                    else{
                        selectedTargetsListView.getItems().remove(checkBox.getText());
                    }
                }
            });
            targetsCheckBoxList.getItems().add(checkBox);
        }
        whatIf_targetsCB.setValue(whatIf_targetsCB.getItems().get(0));
    }




}
