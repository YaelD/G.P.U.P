package runtask;

import dto.TargetDTO;
import dto.TaskParamsDTO;
import engine.Engine;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import target.RunResults;
import task.PausableThreadPoolExecutor;
import task.RunType;
import task.TaskType;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


public class RunWindowController {

    private Engine engine;

    @FXML
    private TextArea taskRunConsole;

    @FXML
    private TableView<TargetRepresenter> targetsTable;

    @FXML
    private TableColumn<TargetRepresenter, StackPane> frozenColumn;

    @FXML
    private TableColumn<TargetRepresenter, StackPane> waitingColumn;

    @FXML
    private TableColumn<TargetRepresenter, StackPane> inProccessColumn;

    @FXML
    private TableColumn<TargetRepresenter, StackPane> finishedColumn;

    @FXML
    private TableColumn<TargetRepresenter, StackPane> SkippedColumn;

    @FXML
    private TextArea targetInfoConsole;

    @FXML
    private Label numFinishedSuccessLabel;

    @FXML
    private Label numFinishedWarningsLabel;

    @FXML
    private Label numSkippedLabel;

    @FXML
    private Label numFailureLabel;

    @FXML
    private ToggleButton pauseToggle;

    @FXML
    private Label percentLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private void initialize(){
        progressBar.setProgress(0);
        progressBar.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                percentLabel.setText(String.valueOf(Integer.valueOf((int)(newValue.doubleValue()*100))  + "%"));
            }
        });

        frozenColumn.setCellValueFactory(new PropertyValueFactory<TargetRepresenter, StackPane>("button"));
        waitingColumn.setCellValueFactory(new PropertyValueFactory<TargetRepresenter, StackPane>("button"));
        finishedColumn.setCellValueFactory(new PropertyValueFactory<TargetRepresenter, StackPane>("button"));
        inProccessColumn.setCellValueFactory(new PropertyValueFactory<TargetRepresenter, StackPane>("button"));
        SkippedColumn.setCellValueFactory(new PropertyValueFactory<TargetRepresenter, StackPane>("button"));

    }

    private void boundUI(){

    }


    public void setEngine(Engine systemEngine) {
        this.engine = systemEngine;

    }

    public void runTask(TaskParamsDTO taskParams, int numOfThreads, TaskType taskType,
                        RunType runType, Set<String> targetsList) {

        final ObservableList<TargetRepresenter> data = FXCollections.observableArrayList();
        for(String targetName: targetsList){
            data.add(new TargetRepresenter(targetName));
        }
        targetsTable.setItems(data);
        double targetListSize = targetsList.size();
        Consumer<TargetDTO> consoleConsumer = targetDTO ->  {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    progressBar.progressProperty().set(progressBar.progressProperty().doubleValue() + (double)(1/targetListSize));
                    final String PRINT_LINE = "---------------------------------------------\n";
                    String currStr = "";
                    currStr += (PRINT_LINE);
                    currStr +=("Target name: " + targetDTO.getName()+ "\n") ;
                    currStr +=("Process result: " + targetDTO.getRunResult().getStatus() + "\n");
                    if(targetDTO.getInfo() != null){
                        currStr +=("Target info:" + targetDTO.getInfo() + "\n");
                    }
                    if(!targetDTO.getRunResult().equals(RunResults.SKIPPED)){
                        currStr +=("Process Start time:" + targetDTO.getStartingTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
                        currStr +=("Process End time:" + targetDTO.getEndingTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
                        if(!targetDTO.getTargetsThatCanBeRun().isEmpty()){
                            currStr +=("The dependent Targets that were opened:\n" + targetDTO.getTargetsThatCanBeRun() + "\n");
                        }
                        if(targetDTO.getRunResult().equals(RunResults.FAILURE)){
                            if(!targetDTO.getSkippedFathers().isEmpty()){
                                currStr +=("The targets that won't be able to process are: \n" + targetDTO.getSkippedFathers() + "\n");
                            }
                        }
                    }
                    if(targetDTO.getTaskRunResult() != null){
                        currStr +=("Run result:\n" + targetDTO.getTaskRunResult());
                    }
                    currStr +=(PRINT_LINE);
                    progressBar.setProgress(progressBar.getProgress() + (1/(targetsList.size())));
                    taskRunConsole.setText(taskRunConsole.getText() + currStr);
                }
            });
        };

        Consumer<TargetDTO> blabla = targetDTO -> {

        };

        Consumer<PausableThreadPoolExecutor> threadPoolExecutorConsumer = pausableThreadPoolExecutor -> {
            this.pauseToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue == true){
                        pausableThreadPoolExecutor.pause();
                    }
                    else{
                        pausableThreadPoolExecutor.resume();
                    }
                }
            });
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                engine.activateTask(consoleConsumer,threadPoolExecutorConsumer, taskParams,
                        taskType, runType.equals(RunType.INCREMENTAL), numOfThreads, targetsList);
            }
        }).start();
    }
}
