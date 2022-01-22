package runtask;

import dto.TargetDTO;
import dto.TaskParamsDTO;
import engine.Engine;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import target.RunResults;
import task.PausableThreadPoolExecutor;
import task.RunType;
import task.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


public class RunWindowController {

    private Engine engine;


    @FXML
    private TextArea taskRunConsole;


//    @FXML private VBox frozenList;
//    @FXML private VBox waitingList;
//    @FXML private VBox inProcessList;
//    @FXML private VBox finishedList;
//    @FXML private VBox skippedList;

    @FXML
    private TableView<TargetsTableButtonsHandler> targetsTable;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> frozenColumn;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> waitingColumn;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> inProcessColumn;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> finishedColumn;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> skippedColumn;


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
        frozenColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("frozenBtn"));
        waitingColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("waitingBtn"));





    }

    private void boundUI(){

    }


    public void setEngine(Engine systemEngine) {
        this.engine = systemEngine;

    }

    public void runTask(TaskParamsDTO taskParams, int numOfThreads, TaskType taskType,
                        RunType runType, Set<String> targetsList) {

        List<TargetsTableButtonsHandler> targetDraws = new ArrayList<>();
        ObservableList<TargetsTableButtonsHandler> data = FXCollections.observableArrayList();
        for(String targetName: targetsList){
            TargetsTableButtonsHandler targetDraw =  new TargetsTableButtonsHandler(targetName);
            for (Button currButton: targetDraw.getButtonsMap().values()) {
                currButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        TargetDTO targetInfo = engine.getRunningTarget(targetDraw.getName());
                        targetInfoConsole.setText(targetInfo.toString());
                    }
                });
            }
            data.add(targetDraw);

        }
        targetsTable.setItems(data);


//        for(String targetName: targetsList){
//            TargetDraw currTargetDraw = new TargetDraw(targetName);
//            currTargetDraw.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    try {
//                        TargetDTO targetInfo = engine.getTarget(currTargetDraw.getName());
//                        targetInfoConsole.setText(targetInfo.toString());
//                    } catch (TargetNotExistException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            frozenList.getChildren().add(currTargetDraw);
//        }

        //----------------------------------------------------
        //Add target run status change listener
        Thread statusChangeListener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressBar.progressProperty().get() != 1){
                    for(TargetsTableButtonsHandler currTargetDraw: data) {
                        TargetDTO targetDTO = engine.getRunningTarget(currTargetDraw.getName());
                        if(targetDTO.getRunStatus() != null){
                            System.out.println(targetDTO.getName() + "----" + targetDTO.getRunStatus());
                            currTargetDraw.updateButtons(targetDTO.getRunStatus());
                        }
                    }
                }
            }
        });




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
                statusChangeListener.start();
                engine.activateTask(consoleConsumer,threadPoolExecutorConsumer, taskParams,
                        taskType, runType.equals(RunType.INCREMENTAL), numOfThreads, targetsList);
            }
        }).start();
    }
}
