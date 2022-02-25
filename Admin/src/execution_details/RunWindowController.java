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
import general_enums.RunStatus;
import general_enums.TaskStatus;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
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
    SimpleDoubleProperty finishedTargetsProgress;


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
        sendStatusToServer(TaskStatus.ACTIVE);
    }

    @FXML
    void onStop(ActionEvent event) {
        sendStatusToServer(TaskStatus.STOPPED);
        pauseToggle.setDisable(true);
        playButton.setDisable(true);

    }

    private void getTask(List<TaskDTO> taskDTOS){
        for(TaskDTO taskDTO: taskDTOS){
            System.out.println("IN admin getTask===>" + taskDTO.getTaskName());
            if(taskDTO.getTaskName().equals(taskDTOProperty.get().getTaskName())){
                taskDTOProperty.set(taskDTO);
                finishedTargetsProgress.set(0);
                for(TargetDTO targetDTO : taskDTO.getGraphDTO().getTargets().values()){
                    if(!targetDTO.getRunStatus().equals(RunStatus.WAITING) && !targetDTO.getRunStatus().equals(RunStatus.FROZEN)){
                        finishedTargetsProgress.set(finishedTargetsProgress.doubleValue() + (double) (1 / taskDTO.getGraphDTO().getTotalNumOfTargets()));
                    }
                }
                return;
            }
        }
    }

    public RunWindowController() {
        this.taskDTOProperty = new SimpleObjectProperty<>();
        this.finishedTargetsProgress = new SimpleDoubleProperty();
    }

    @FXML
    private void initialize(){
        pauseToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue == true){
                    sendStatusToServer(TaskStatus.SUSPENDED);
                }
                else{
                    sendStatusToServer(TaskStatus.ACTIVE);
                }
            }
        });
        progressBar.progressProperty().bind(finishedTargetsProgress);
        taskTableController.setRunWindowController(this);
        taskTableController.setTaskDTO(taskDTOProperty);
        TaskListRefresherTimer.getInstance().addConsumer(this::getTask);
        finishedTargetsProgress.set(0);

        progressBar.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(()->{
                    percentLabel.setText(String.valueOf(Integer.valueOf((int)(newValue.doubleValue()*100))  + "%"));

                });
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

        currStr +=(PRINT_LINE);
        return currStr;
    }

    public void writeToTargetDetails(String str){
        Platform.runLater(()->{
            this.targetInfoConsole.setText(str);
        });
    }


    private void sendStatusToServer(TaskStatus status){
        String finalUrl = Objects.requireNonNull(HttpUrl
                        .parse(Constants.TASK_LIST)).newBuilder()
                .addQueryParameter(Constants.TASK_STATUS, status.name())
                .addQueryParameter(Constants.TASK_NAME, taskDTOProperty.get().getTaskName())
                .build()
                .toString();
        Request  request= new Request.Builder().url(finalUrl).put(RequestBody.create(new byte[0])).build();

        HttpUtils.runAsyncWithRequest(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                response.body().close();
            }
        });

    }




}
