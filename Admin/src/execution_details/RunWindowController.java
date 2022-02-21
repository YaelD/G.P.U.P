package execution_details;

//import dto.GraphDTO;
//import dto.TargetDTO;
//import dto.TaskParamsDTO;
//import engine.Engine;
//import graph.Graph;
import RefreshingItems.TaskListRefresherTimer;
import constants.Constants;
import dto.TargetDTO;
import dto.TaskDTO;
import general_enums.RunResults;
import general_enums.TaskStatus;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
//import runtask.running_task_window.TargetsTableButtonsHandler;
//import target.RunResults;
//import target.RunStatus;
//import task.PausableThreadPoolExecutor;
//import task.RunType;
//import task.TaskType;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class RunWindowController {

    SimpleObjectProperty<TaskDTO> taskDTOProperty;


    @FXML
    private GridPane runResultsPane;

    @FXML
    private RunResultsController runResultsPaneController;

    @FXML
    private GridPane taskDetails;

    @FXML
    private TaskDetailsPaneController taskDetailsController;

    @FXML
    private TableView<TargetsTableButtonsHandler> taskTable;

    @FXML
    private RunningTargetsTableController taskTableController;

    @FXML
    private TextArea taskRunConsole;

    @FXML
    private TextArea targetInfoConsole;


    @FXML
    private ToggleButton pauseToggle;

    @FXML
    private Label percentLabel;

    @FXML
    private ProgressBar progressBar;



    @FXML
    private Button playButton;

    @FXML
    private Button stopButton;


    @FXML
    void onPlay(ActionEvent event) {
        String finalUrl = Objects.requireNonNull(HttpUrl
                        .parse(Constants.TASK_LIST)).newBuilder()
                .addQueryParameter(Constants.TASK_STATUS, TaskStatus.ACTIVE.name())
                .addQueryParameter(Constants.TASK_NAME, taskDTOProperty.get().getTaskName())
                .build()
                .toString();
        Request  request= new Request.Builder().url(finalUrl).put(RequestBody.create(new byte[0])).build();

        HttpUtils.runAsyncWithRequest(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(":((((((((((((((((((((((((((");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(":]]]]]]]]]]]]]]]]]]]]]]]]]");
            }
        });
    }

    @FXML
    void onStop(ActionEvent event) {

    }

    private void getTask(List<TaskDTO> taskDTOS){
        for(TaskDTO taskDTO: taskDTOS){
            if(taskDTO.getTaskName().equals(taskDTOProperty.get().getTaskName())){
                taskDTOProperty.set(taskDTO);
                return;
            }
        }
    }

    public RunWindowController() {
        this.taskDTOProperty = new SimpleObjectProperty<>();
    }

    @FXML
    private void initialize(){
        taskTableController.setRunWindowController(this);
        taskTableController.setTaskDTO(taskDTOProperty);
        TaskListRefresherTimer.getInstance().addConsumer(this::getTask);

        progressBar.setProgress(0);
        progressBar.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                percentLabel.setText(String.valueOf(Integer.valueOf((int)(newValue.doubleValue()*100))  + "%"));
            }
        });

        taskDTOProperty.addListener(new ChangeListener<TaskDTO>() {
            @Override
            public void changed(ObservableValue<? extends TaskDTO> observable, TaskDTO oldValue, TaskDTO newValue) {
                taskDetailsController.setTaskDTO(taskDTOProperty.getValue());
            }
        });


    }


    public void setTaskDTOProperty(TaskDTO taskDTOProperty) {
       this.taskDTOProperty.set(taskDTOProperty);
    }

    private void runTask() {
//        Map<String, TargetDTO> targetsMap  = this.task.getValue().getGraphDTO().getTargets();
//        ObservableList<TargetsTableButtonsHandler> data = FXCollections.observableArrayList();
//        for(TargetDTO currTarget: targetsMap.values()){
//            TargetsTableButtonsHandler targetDraw = new TargetsTableButtonsHandler(currTarget.getName());
//            for(Button currButton: targetDraw.getButtonsMap().values()){
//                currButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        TargetDTO targetDTO = task.getValue().getGraphDTO().getTargets().get(targetDraw.getName());
//                        String str = targetDTO.getRunningTargetStatus();
//                        if(str.equals("")){
//                            str = createRunResultString(targetDTO);
//                        }
//                        targetInfoConsole.setText(str);
//
//                    }
//                });
//            }
//        }


//        ObservableList<TargetsTableButtonsHandler> data = FXCollections.observableArrayList();
//        for(String targetName: targetsList){
//            TargetsTableButtonsHandler targetDraw =  new TargetsTableButtonsHandler(targetName);
//            for (Button currButton: targetDraw.getButtonsMap().values()) {
//                currButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        //TargetDTO targetDTO = engine.getRunningTarget(targetDraw.getName());
//                        String str = targetDTO.getRunningTargetStatus();
//                        if(str.equals("")){
//                            str = createRunResultString(targetDTO);
//                        }
//                        targetInfoConsole.setText(str);
//                    }
//                });
//            }
//            data.add(targetDraw);
//        }
//        targetsTable.setItems(data);

        //----------------------------------------------------
//        Thread statusChangeListener = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (progressBar.progressProperty().get() != 1){
//                    for(TargetsTableButtonsHandler currTargetDraw: data) {
//                        TargetDTO targetDTO = engine.getRunningTarget(currTargetDraw.getName());
//                        if(targetDTO != null && targetDTO.getRunStatus()!= null){
//                            currTargetDraw.setRunStatus(targetDTO.getRunStatus());
//                            if(targetDTO.getRunStatus().equals(RunStatus.FINISHED)){
//                                if(targetDTO.getRunResult() != null){
//                                    currTargetDraw.setRunResults(targetDTO.getRunResult());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });
//
//        statusChangeListener.setDaemon(false);






////        double targetListSize = targetsList.size();
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
    }

    private String createRunResultString(TargetDTO targetDTO){
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
//        if(targetDTO.getTaskRunResult() != null && !(targetDTO.getTaskRunResult().isEmpty())){
//            currStr +=("Run result:\n" + targetDTO.getTaskRunResult());
//        }

        currStr +=(PRINT_LINE);
        return currStr;
    }

    public void writeToTargetDetails(String str){
        Platform.runLater(()->{
            this.targetInfoConsole.setText(str);
        });
    }


}
