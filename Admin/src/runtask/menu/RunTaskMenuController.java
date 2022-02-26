package runtask.menu;

import RefreshingItems.TaskListRefresherTimer;
import admin_engine.Utilities;
import com.google.gson.Gson;
import constants.Constants;
import dto.*;
import general_enums.Dependency;
import general_enums.RunType;
import general_enums.TaskType;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import runtask.compilation_task.CompilationParamsController;
import runtask.simulation_task.SimulationParamsController;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class RunTaskMenuController {


    private GraphDTO currGraph;
    private String userName;

    private List<TaskDTO> currTasksInSystem;

    @FXML private HBox baseHBox;
    @FXML private HBox bigHBox;



    @FXML private GridPane simulationTaskToggles;
    @FXML private SimulationParamsController simulationTaskTogglesController;

    @FXML private GridPane compilationTaskToggles;
    @FXML private CompilationParamsController compilationTaskTogglesController;


    private SimpleListProperty<String> targetsList;
    private SimpleObjectProperty<TaskType> taskType;
    private SimpleObjectProperty<RunType> runType;
    private SimpleStringProperty taskName;
    private SimpleStringProperty graphName;
    private SimpleIntegerProperty taskPrice;
    private SimpleStringProperty creatorName;



    @FXML private Label warningTaskNameLabel;
    @FXML private Label warningRunTypeLabel;
    @FXML private Label warningChosenTargetsLabel;
    @FXML private Label serverResponseLabel;


    @FXML private TextField taskNameTextField;
    @FXML private VBox targetsSubMenu;
    @FXML private CheckBox chooseAllTargetsCheckBox;
    @FXML private ListView<CheckBox> targetsCheckBoxList;
    @FXML private RadioButton chooseTargetsRB;
    @FXML private ToggleGroup chooseTargetsToogleGroup;
    @FXML private RadioButton chooseWhatIfRB;
    @FXML private VBox whatIfSubMenu;
    @FXML private ChoiceBox<String> whatIf_targetsCB;
    @FXML private ChoiceBox<Dependency> whatIf_DependencyCB;
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




    public RunTaskMenuController(){
        this.taskType = new SimpleObjectProperty<>(TaskType.SIMULATION_TASK);
        this.runType = new SimpleObjectProperty<>(RunType.FROM_SCRATCH);
        this.targetsList = new SimpleListProperty<>();
        this.taskName = new SimpleStringProperty();
        this.graphName = new SimpleStringProperty();
        this.taskPrice = new SimpleIntegerProperty();
        this.creatorName = new SimpleStringProperty();

    }

    public void disableIncremental(boolean allowIncremental){
        this.incrementalRadioButton.setDisable(!allowIncremental);
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
                            data.addAll(targets);
                            selectedTargetsListView.setItems(data);

                        }
                    }

                });

            }
        });
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
        if(simulationRadioButton.isSelected()){
            baseHBox.getChildren().remove(compilationTaskToggles);
            try{
                baseHBox.getChildren().add(simulationTaskToggles);
                bigHBox.getChildren().get(0).setDisable(true);
                bigHBox.getChildren().get(1).setDisable(true);
                simulationTaskToggles.setMaxHeight(Double.MAX_VALUE);
                simulationTaskToggles.setMaxWidth(Double.MAX_VALUE);
            }catch (IllegalArgumentException e){

            }
        }
        else{
            baseHBox.getChildren().remove(simulationTaskToggles);
            try{
                baseHBox.getChildren().add(compilationTaskToggles);
                bigHBox.getChildren().get(0).setDisable(true);
                bigHBox.getChildren().get(1).setDisable(true);
                compilationTaskToggles.setMaxHeight(Double.MAX_VALUE);
                compilationTaskToggles.setMaxWidth(Double.MAX_VALUE);
            }catch (IllegalArgumentException e){

            }
        }

    }


    @FXML
    private void initialize(){
        whatIf_DependencyCB.getItems().add(Dependency.DEPENDS_ON);
        whatIf_DependencyCB.getItems().add(Dependency.REQUIRED_FOR);
        whatIf_DependencyCB.getSelectionModel().select(0);
        TaskListRefresherTimer.getInstance().addConsumer(this::setCurrTasksInSystem);
        continueButton.disableProperty().bind(Bindings.or(warningChosenTargetsLabel.visibleProperty(),Bindings.or(warningTaskNameLabel.visibleProperty(), warningRunTypeLabel.visibleProperty())));

        this.taskName.bind(this.taskNameTextField.textProperty());
        taskNameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){
                    warningTaskNameLabel.setVisible(true);
                    warningTaskNameLabel.setText("Please enter task name");
                    return;
                }
                else{
                    if(!checkIfNameIsOk()){
                        warningTaskNameLabel.setVisible(true);
                        warningTaskNameLabel.setText("Name already in use");
                        return;
                    }
                }
                warningTaskNameLabel.setVisible(false);
            }
        });
        baseHBox.getChildren().remove(simulationTaskToggles);
        baseHBox.getChildren().remove(compilationTaskToggles);
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
                    taskPrice.set(items.size()*currGraph.getPriceOfSimulationTask());
                }
                else{
                    taskPrice.set(items.size()*currGraph.getPriceOfCompilationTask());
                }

            }
        });
        simulationTaskTogglesController.setTaskCallBack(new SimulationParamsCallBack() {
            @Override
            public void sendSimulationTaskParams(int processTime, boolean isRandom, double successRate, double successRateWithWarnings) {
                RunType currRunType = runType.get();
                List<String> currTargetList = targetsList.get();
                String creatorName = userName;
                String graphName = currGraph.getName();
                String currTaskName = taskName.get();
                int totalTaskPrice = currGraph.getPriceOfSimulationTask()*currTargetList.size();
                serverResponseLabel.setVisible(false);
                sendTaskToServer(new SimulationTaskParamsDTO(currRunType, currTargetList, creatorName, graphName,
                        currTaskName, totalTaskPrice, processTime, isRandom, successRate, successRateWithWarnings));

            }
        });

        simulationTaskTogglesController.setReturnCallBack(new ReturnCallback() {
            @Override
            public void returnToPrev() {
                serverResponseLabel.setVisible(false);
                baseHBox.getChildren().remove(simulationTaskToggles);
                bigHBox.getChildren().get(0).setDisable(false);
                bigHBox.getChildren().get(1).setDisable(false);

            }
        });

        compilationTaskTogglesController.setActiveTaskCallback(new CompilationParamsCallBack() {
            @Override
            public void setCompilationParams(String sourceDir, String destDir) {
                RunType currRunType = runType.get();
                List<String> currTargetList = targetsList.get();
                String creatorName = userName;
                String graphName = currGraph.getName();
                String currTaskName = taskName.get();
                int totalTaskPrice = currGraph.getPriceOfCompilationTask()*currTargetList.size();
                serverResponseLabel.setVisible(false);
                sendTaskToServer(new CompilationTaskParamsDTO(currRunType, currTargetList, creatorName, graphName,
                        currTaskName,totalTaskPrice, sourceDir, destDir));
            }
        });


        compilationTaskTogglesController.setReturnCallBack(new ReturnCallback() {
            @Override
            public void returnToPrev() {
                serverResponseLabel.setVisible(false);
                baseHBox.getChildren().remove(compilationTaskToggles);
                bigHBox.getChildren().get(0).setDisable(false);
                bigHBox.getChildren().get(1).setDisable(false);
            }
        });

    }

    private void sendTaskToServer(TaskParamsDTO taskParamsDTO){
        Utilities.TASK_PARAMS.put(taskParamsDTO.getTaskName(), taskParamsDTO);

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
                Platform.runLater(()->{
                    serverResponseLabel.setVisible(true);
                    serverResponseLabel.setTextFill(Color.RED);
                    serverResponseLabel.setText(e.getMessage());
                });
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
                            Utilities.TASK_PARAMS.put(taskParamsDTO.getTaskName(), taskParamsDTO);
                            Utilities.TASK_APPEARANCE_COUNTER.put(taskParamsDTO.getTaskName(), 1);
                        }
                        else{
                            serverResponseLabel.setVisible(true);
                            serverResponseLabel.setTextFill(Color.RED);
                            serverResponseLabel.setText(res);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    private boolean checkIfNameIsOk() {
        if(currTasksInSystem == null){
            return false;
        }
        for(TaskDTO taskDTO: this.currTasksInSystem){
            if(taskDTO.getTaskName().equals(this.taskName.getValue())){
                return false;
            }
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
                warningChosenTargetsLabel.setVisible(true);
            }
        });
        chooseTargetsRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                selectedTargetsListView.getItems().clear();
                warningChosenTargetsLabel.setVisible(true);
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
        simulationRadioButton.setDisable(currGraph.getPriceOfSimulationTask() == 0);
        compilationRadioButton.setDisable(currGraph.getPriceOfCompilationTask() == 0);

        taskToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == simulationRadioButton){
                    baseHBox.getChildren().remove(compilationTaskToggles);
                }
                else if(newValue == compilationRadioButton){
                    baseHBox.getChildren().remove(simulationTaskToggles);
                }

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
        this.graphName.set(currGraph.getName());

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


    public void setCurrTasksInSystem(List<TaskDTO> currTasksInSystem) {

        this.currTasksInSystem = currTasksInSystem;
    }
}
