package runtask;

import dto.TargetDTO;
import dto.TaskParamsDTO;
import engine.Engine;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
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
    private TableView<?> targetsTable;

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
        percentLabel.textProperty().bind(Bindings.concat(progressBar.progressProperty().intValue() + "%"));

    }

    private void boundUI(){

    }


    public void setEngine(Engine systemEngine) {
        this.engine = systemEngine;
    }

    public void runTask(TaskParamsDTO taskParams, int numOfThreads, TaskType taskType,
                        RunType runType, Set<String> targetsList) {
        Consumer<TargetDTO> consoleConsumer = targetDTO ->  {

//            String NEW_LINE = "-----------------------------------------------\n";
//            String str = "Target name: " + targetDTO.getName() +
//                    "\n";

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
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

                    taskRunConsole.setText(taskRunConsole.getText() + currStr);
                }
            });


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
