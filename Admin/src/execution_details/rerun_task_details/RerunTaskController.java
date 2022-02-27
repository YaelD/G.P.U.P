package execution_details.rerun_task_details;

import admin_engine.Utilities;
import com.google.gson.Gson;
import constants.Constants;
import dto.*;
import general_enums.Dependency;
import general_enums.RunResults;
import general_enums.RunType;
import general_enums.TaskType;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RerunTaskController {

    private TaskDTO currTask;
    private SimpleListProperty<String> targetsList;
    private SimpleObjectProperty<TaskType> taskType;
    private SimpleObjectProperty<RunType> runType;
    private SimpleStringProperty taskName;
    private SimpleStringProperty graphName;
    private SimpleIntegerProperty taskPrice;
    private SimpleStringProperty creatorName;


    private Set<String> allTaskTargetsList;
    private Set<String> failedTargetsList;
    private TaskParamsDTO taskParamsDTO;




    @FXML
    private RadioButton simulationRadioButton;

    @FXML
    private ToggleGroup taskToggleGroup;

    @FXML
    private RadioButton compilationRadioButton;

    @FXML
    private RadioButton fromScratchRadioButton;

    @FXML
    private ToggleGroup runTypeToggle;

    @FXML
    private RadioButton incrementalRadioButton;

    @FXML
    private Label taskNameTextField;

    @FXML
    private Label serverResponseLabel;

    @FXML
    private VBox targetsSubMenu;

    @FXML
    private CheckBox chooseAllTargetsCheckBox;

    @FXML
    private ListView<CheckBox> targetsCheckBoxList;

    @FXML
    private RadioButton chooseTargetsRB;

    @FXML
    private ToggleGroup chooseTargetsToogleGroup;

    @FXML
    private RadioButton chooseWhatIfRB;

    @FXML
    private VBox whatIfSubMenu;

    @FXML
    private ChoiceBox<String> whatIf_targetsCB;

    @FXML
    private ChoiceBox<Dependency> whatIf_DependencyCB;

    @FXML
    private Button WhatIfButton;

    @FXML
    private ListView<String> selectedTargetsListView;

    @FXML
    private Button continueButton;

    @FXML
    private Label warningChosenTargetsLabel;
    private String originalName;


    @FXML
    void clearAllTargets(ActionEvent event) {
        for(CheckBox checkBox: targetsCheckBoxList.getItems()){
            checkBox.setSelected(false);
        }
        selectedTargetsListView.getItems().clear();
        chooseAllTargetsCheckBox.setSelected(false);
    }

    public RerunTaskController() {
        targetsList = new SimpleListProperty<>();
        taskType = new SimpleObjectProperty<>();
        runType = new SimpleObjectProperty<>();
        taskName = new SimpleStringProperty();
        graphName = new SimpleStringProperty();
        taskPrice = new SimpleIntegerProperty();
        creatorName = new SimpleStringProperty();
    }

    @FXML
    private void initialize(){
        taskNameTextField.textProperty().bind(taskName);
        whatIf_DependencyCB.getItems().add(Dependency.DEPENDS_ON);
        whatIf_DependencyCB.getItems().add(Dependency.REQUIRED_FOR);
        whatIf_DependencyCB.getSelectionModel().select(0);
        continueButton.disableProperty().bind(warningChosenTargetsLabel.visibleProperty());
        initTargetChoiceControllers();

        targetsList.bind(selectedTargetsListView.itemsProperty());

        selectedTargetsListView.getItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                ObservableList<String> items = selectedTargetsListView.getItems();
                if(items.isEmpty()){
                    warningChosenTargetsLabel.setVisible(true);
                    return;
                }
                warningChosenTargetsLabel.setVisible(false);
                if(taskType.getValue().equals(TaskType.SIMULATION_TASK)){
                    taskPrice.set(items.size()*currTask.getGraphDTO().getPriceOfSimulationTask());
                }
                else{
                    taskPrice.set(items.size()*currTask.getGraphDTO().getPriceOfCompilationTask());
                }

            }
        });

    }

    private void sendTaskToServer(TaskParamsDTO taskParamsDTO){

        Gson gson = new Gson();
        SimulationTaskParamsDTO simulationTaskParamsDTO;
        CompilationTaskParamsDTO compilationTaskParamsDTO;
        String param = "";
        if(taskParamsDTO instanceof SimulationTaskParamsDTO){
            simulationTaskParamsDTO = (SimulationTaskParamsDTO) taskParamsDTO;
            param = gson.toJson(simulationTaskParamsDTO);
        }
        else{
            compilationTaskParamsDTO = (CompilationTaskParamsDTO) taskParamsDTO;
            param = gson.toJson(compilationTaskParamsDTO);
        }
        Request request = new Request.Builder().url(Constants.TASK_LIST)
                .post(RequestBody.create(param.getBytes())).build();
        HttpUtils.runAsyncWithRequest(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(()->{
                    try {
                        String res = response.body().string();
                        if(response.code() == 200) {
                            serverResponseLabel.setVisible(true);
                            serverResponseLabel.setTextFill(Color.GREEN);
                            serverResponseLabel.setText("Task created successfully");
                            Utilities.TASK_NAME_TO_ORIGINAL.put(taskParamsDTO.getTaskName(), currTask.getTaskName());
                            int numOfAppearances = Utilities.TASK_APPEARANCE_COUNTER.get(originalName);
                            Utilities.TASK_APPEARANCE_COUNTER.put(originalName, numOfAppearances+1);
                        }
                        else{
                            serverResponseLabel.setVisible(true);
                            serverResponseLabel.setTextFill(Color.RED);
                            serverResponseLabel.setText(res);
                        }
                        response.body().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
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
                    warningChosenTargetsLabel.setVisible(false);
                }
            }
        });

    }

    private void initTaskChoiceController() {
        simulationRadioButton.setDisable(currTask.getTaskType().equals(TaskType.COMPILATION_TASK));
        compilationRadioButton.setDisable(currTask.getTaskType().equals(TaskType.SIMULATION_TASK));

        runTypeToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == incrementalRadioButton){
                    initLists(failedTargetsList);
                    runType.set(RunType.INCREMENTAL);
                }
                else if(newValue == fromScratchRadioButton){
                    initLists(allTaskTargetsList);
                    runType.set(RunType.FROM_SCRATCH);
                }

                for(CheckBox checkBox: targetsCheckBoxList.getItems()){
                    checkBox.setSelected(false);
                }
                chooseAllTargetsCheckBox.setSelected(false);
                selectedTargetsListView.getItems().clear();


            }
        });
    }

    public void setCurrTask(TaskDTO taskDTO) {
        this.currTask = taskDTO;
        this.originalName = Utilities.getOriginalTaskString(currTask.getTaskName());

        this.graphName.set(taskDTO.getGraphDTO().getName());
        this.allTaskTargetsList = taskDTO.getGraphDTO().getTargets().keySet();
        this.failedTargetsList = new HashSet<>();
        for(String targetName: allTaskTargetsList){
            RunResults runResults = taskDTO.getGraphDTO().getTargets().get(targetName).getRunResult();
            if(runResults.equals(RunResults.FAILURE) || runResults.equals(RunResults.SKIPPED)){
                failedTargetsList.add(targetName);
            }
        }
        this.taskParamsDTO = Utilities.TASK_PARAMS.get(originalName);
        taskName.set(originalName +"(" + Utilities.TASK_APPEARANCE_COUNTER.get(originalName) + ")");
        taskType.set(taskDTO.getTaskType());
        incrementalRadioButton.setDisable(failedTargetsList.isEmpty());
        initLists(allTaskTargetsList);
        initTaskChoiceController();

    }


    private void initLists(Set<String> targetsName) {
        targetsCheckBoxList.getItems().clear();
        whatIf_targetsCB.getItems().clear();
        for(String target: targetsName){
            whatIf_targetsCB.getItems().add(target);
            CheckBox checkBox = new CheckBox(target);
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue == true){
                        selectedTargetsListView.getItems().add(checkBox.getText());
                        warningChosenTargetsLabel.setVisible(false);
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


    @FXML
    void onSendTask(ActionEvent event) {
        TaskParamsDTO newParams = null;
        switch (taskParamsDTO.getTaskType()){
            case SIMULATION_TASK:
                newParams = buildSimulationParams((SimulationTaskParamsDTO) taskParamsDTO);
                break;
            case COMPILATION_TASK:
                newParams = buildCompilationParams((CompilationTaskParamsDTO) taskParamsDTO);
                break;
        }
        sendTaskToServer(newParams);
    }


    private TaskParamsDTO buildSimulationParams(SimulationTaskParamsDTO oldParams){
        return new SimulationTaskParamsDTO(runType.get(), targetsList.get(), oldParams.getCreatorName(), oldParams.getGraphName(), taskName.getValue(),
                oldParams.getTotalTaskPrice(),  oldParams.getProcessTime(), oldParams.isRandom(), oldParams.getSuccessRate(), oldParams.getSuccessWithWarningsRate());
    }

    private TaskParamsDTO buildCompilationParams(CompilationTaskParamsDTO oldParams){
        return new CompilationTaskParamsDTO(runType.get(), targetsList.get(), oldParams.getCreatorName(), oldParams.getGraphName(), taskName.getValue(),
                oldParams.getTotalTaskPrice(), oldParams.getSourceDir(), oldParams.getDestinationDir());
    }

    @FXML
    void checkTargetsWithWhatIf(ActionEvent event) {
        selectedTargetsListView.getItems().clear();
        String finalUrl = HttpUrl
                .parse(Constants.WHAT_IF)
                .newBuilder()
                .addQueryParameter(Constants.SOURCE_TARGET, whatIf_targetsCB.getValue())
                .addQueryParameter(Constants.DEPENDENCY, whatIf_DependencyCB.getValue().name())
                .addQueryParameter(Constants.GRAPH_NAME, graphName.getValue())
                .build()
                .toString();
        HttpUtils.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Platform.runLater(()->{
                    if (response.code() != 200) {
                        warningChosenTargetsLabel.setVisible(true);
                        warningChosenTargetsLabel.setText(responseBody);
                    } else {
                        String[] targets = new Gson().fromJson(responseBody, String[].class);
                        if (targets.length == 0) {
                            warningChosenTargetsLabel.setVisible(true);
                        } else {
                            warningChosenTargetsLabel.setVisible(false);
                            ObservableList<String> data = FXCollections.observableArrayList();
                            if(incrementalRadioButton.isSelected()){
                                for(String name: targets) {
                                    if (failedTargetsList.contains(name)) {
                                        data.add(name);
                                    }
                                }
                            }
                            else{
                                for(String name: targets) {
                                    if (allTaskTargetsList.contains(name)) {
                                        data.add(name);
                                    }
                                }
                            }
                            selectedTargetsListView.setItems(data);

                        }
                    }

                });

            }
        });
    }
}
