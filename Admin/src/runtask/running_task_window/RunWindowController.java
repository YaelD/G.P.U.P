package runtask.running_task_window;

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
import javafx.scene.layout.GridPane;

import java.util.Set;
import java.util.function.Consumer;


public class RunWindowController {

//    private Engine engine;
//    private Graph graph;

    @FXML
    private GridPane runResultsPane;

    @FXML
    private TextArea taskRunConsole;

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
    private ChoiceBox<Integer> numOfThreadsCB;


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
        inProcessColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("inProcessBtn"));
        finishedColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("finishedBtn"));
        skippedColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("skippedBtn"));

    }
//
//
//
//    public void setEngine(Engine systemEngine) {
//        this.engine = systemEngine;
//        this.graph = this.engine.getGraphForRunning();
//    }
//
//    public void runTask(TaskParamsDTO taskParams, int numOfThreads, TaskType taskType,
//                        RunType runType, Set<String> targetsList) {
//
//
//        for(int i =1; i <= engine.getMaxNumOfThreads(); ++i){
//            this.numOfThreadsCB.getItems().add(i);
//        }
//        numOfThreadsCB.valueProperty().setValue(numOfThreads);
//
//
//        ObservableList<TargetsTableButtonsHandler> data = FXCollections.observableArrayList();
//        for(String targetName: targetsList){
//            TargetsTableButtonsHandler targetDraw =  new TargetsTableButtonsHandler(targetName);
//            for (Button currButton: targetDraw.getButtonsMap().values()) {
//                currButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
////                        TargetDTO targetDTO = engine.getRunningTarget(targetDraw.getName());
////                        String str = targetDTO.getRunningTargetStatus();
////                        if(str.equals("")){
////                            str = createRunResultString(targetDTO);
////                        }
////                        targetInfoConsole.setText(str);
//                    }
//                });
//            }
//            data.add(targetDraw);
//        }
//        targetsTable.setItems(data);
//
//        //----------------------------------------------------
//        Thread statusChangeListener = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (progressBar.progressProperty().get() != 1){
//                    for(TargetsTableButtonsHandler currTargetDraw: data) {
////                        TargetDTO targetDTO = engine.getRunningTarget(currTargetDraw.getName());
////                        if(targetDTO != null && targetDTO.getRunStatus()!= null){
////                            currTargetDraw.setRunStatus(targetDTO.getRunStatus());
////                            if(targetDTO.getRunStatus().equals(RunStatus.FINISHED)){
////                                if(targetDTO.getRunResult() != null){
////                                    currTargetDraw.setRunResults(targetDTO.getRunResult());
////                                }
////                            }
////                        }
//                    }
//                }
//            }
//        });
//
//        statusChangeListener.setDaemon(false);
//
//
//
//
//
//
//        double targetListSize = targetsList.size();
//        Consumer<TargetDTO> consoleConsumer = targetDTO ->  {
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    progressBar.progressProperty().set(progressBar.progressProperty().doubleValue() + (double) (1/targetListSize));
//                    String currStr = createRunResultString(targetDTO);
//                    taskRunConsole.setText(taskRunConsole.getText() + currStr);
//
//                }
//            });
//        };
//
//        Consumer<PausableThreadPoolExecutor> threadPoolNumOfThreadsConsumer = pausableThreadPoolExecutor -> {
//
//        };
//
//
//        Consumer<PausableThreadPoolExecutor> threadPoolExecutorConsumer = pausableThreadPoolExecutor -> {
//            numOfThreadsCB.valueProperty().addListener(new ChangeListener<Integer>() {
//                @Override
//                public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
//                    pausableThreadPoolExecutor.setCorePoolSize(newValue);
//                    pausableThreadPoolExecutor.setMaximumPoolSize(newValue);
//                }
//            });
//            this.pauseToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if(newValue == true){
//                        pausableThreadPoolExecutor.pause();
//                        pauseToggle.setText("Resume");
//                        numOfThreadsCB.setDisable(false);
//                    }
//                    else{
//                        pausableThreadPoolExecutor.resume();
//                        pauseToggle.setText("Pause");
//                        numOfThreadsCB.setDisable(true);
//
//                    }
//                }
//            });
//        };
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                statusChangeListener.start();
//                GraphDTO taskResult = engine.activateTask(consoleConsumer,threadPoolExecutorConsumer, taskParams,
//                        taskType, runType.equals(RunType.INCREMENTAL), numOfThreads, targetsList);
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setProgress(1);
//                        pauseToggle.setDisable(true);
//                        runResultsPane.setVisible(true);
//                        numFinishedSuccessLabel.setText(String.valueOf(taskResult.getNumOfTargetsRunResult(RunResults.SUCCESS)));
//                        numFailureLabel.setText(String.valueOf(taskResult.getNumOfTargetsRunResult(RunResults.FAILURE)));
//                        numSkippedLabel.setText(String.valueOf(taskResult.getNumOfTargetsRunResult(RunResults.SKIPPED)));
//                        numFinishedWarningsLabel.setText(String.valueOf(taskResult.getNumOfTargetsRunResult(RunResults.WARNING)));
//                    }
//                });
//            }
//        }).start();
//    }
//
//    private String createRunResultString(TargetDTO targetDTO){
//        final String PRINT_LINE = "---------------------------------------------\n";
//        String currStr = "";
//        currStr += (PRINT_LINE);
//        currStr +=("Target name: " + targetDTO.getName()+ "\n") ;
//        currStr +=("Process result: " + targetDTO.getRunResult().getStatus() + "\n");
//        if(targetDTO.getInfo() != null){
//            currStr +=("Target info:" + targetDTO.getInfo() + "\n");
//        }
//        if(!targetDTO.getRunResult().equals(RunResults.SKIPPED)){
//            if(!targetDTO.getTargetsThatCanBeRun().isEmpty()){
//                currStr +=("The dependent Targets that were opened:\n" + targetDTO.getTargetsThatCanBeRun() + "\n");
//            }
//            if(targetDTO.getRunResult().equals(RunResults.FAILURE)){
//                if(!targetDTO.getSkippedFathers().isEmpty()){
//                    currStr +=("The targets that won't be able to process are: \n" + targetDTO.getSkippedFathers() + "\n");
//                }
//            }
//        }
//        if(targetDTO.getTaskRunResult() != null && !(targetDTO.getTaskRunResult().isEmpty())){
//            currStr +=("Run result:\n" + targetDTO.getTaskRunResult());
//        }
//        if(targetDTO.getSerialSetNames() != null){
//            if(!targetDTO.getSerialSetNames().isEmpty()){
//                currStr += "\nSerialSets: \n" + targetDTO.getSerialSetNames().toString() + "\n";
//            }
//        }
//
//        currStr +=(PRINT_LINE);
//        return currStr;
//    }
//
//

}
