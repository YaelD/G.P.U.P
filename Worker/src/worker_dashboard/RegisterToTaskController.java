package worker_dashboard;

import RefreshingItems.TaskListRefresherTimer;
import com.google.gson.Gson;
import constants.Constants;
import dto.CompilationTaskParamsDTO;
import dto.SimulationTaskParamsDTO;
import dto.TaskDTO;
import dto.TaskParamsDTO;
import general_enums.TaskStatus;
import general_enums.TaskType;
import http_utils.HttpUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import worker_engine.ExecutionTargetsRefresherTimer;
import worker_engine.WorkerEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterToTaskController {

    private SimpleStringProperty taskName;
    private String userName;

    List<TaskDTO> taskDTOS;

    @FXML
    private Label taskNameLabel;

    @FXML
    private TextArea taskInfoTextArea;

    @FXML
    private Button registerToTaskBtn;



    public RegisterToTaskController() {
        taskName = new SimpleStringProperty("");
        taskDTOS = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        TaskListRefresherTimer.getInstance().addConsumer(this::updateTasks);
        taskNameLabel.textProperty().bind(taskName);
        taskNameLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                taskInfoTextArea.setText("");
            }
        });
    }

    @FXML
    void OnClickRegisterButton(ActionEvent event) {
        registerToTaskBtn.setDisable(false);
        for(TaskDTO taskDTO : taskDTOS){
            if(taskDTO.getTaskName().equals(taskName.getValueSafe())){
                if(taskDTO.getRegisteredWorkers().contains(this.userName)){
                    taskInfoTextArea.setText("You are already registered to the task");
                    return;
                }

                if(taskDTO.getTaskStatus().equals(TaskStatus.FINISHED) || taskDTO.getTaskStatus().equals(TaskStatus.STOPPED)){
                    taskInfoTextArea.setText("This task is " + taskDTO.getTaskStatus().name() + ". can't register to task");
                    return;
                }
            }
        }
        if(taskName.getValueSafe().isEmpty()){
            taskInfoTextArea.setText("Please choose a task to register to");
            return;
        }


        String finalUrl = Objects.requireNonNull(HttpUrl
                .parse(Constants.TASK_EXECUTION)).newBuilder()
                .addQueryParameter(Constants.TASK_NAME, taskName.getValue())
                .build()
                .toString();

        Request request= new Request.Builder().url(finalUrl).put(RequestBody.create(new byte[0])).build();

        HttpUtils.runAsyncWithRequest(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //TODO: FILL THIS SECTION!!!
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200){
                    String taskParams;
                    String jsonOfTaskParamsDTO = response.body().string();

                    TaskParamsDTO taskParamsDTOS = new Gson().fromJson(jsonOfTaskParamsDTO, TaskParamsDTO.class);
                    if(taskParamsDTOS.getTaskType().equals(TaskType.COMPILATION_TASK)){
                        CompilationTaskParamsDTO compilationTaskParamsDTO = new Gson().fromJson(jsonOfTaskParamsDTO, CompilationTaskParamsDTO.class);
                        WorkerEngine.getInstance().getRegisteredTasksParams().put(compilationTaskParamsDTO.getTaskName(), compilationTaskParamsDTO);
                        taskParams = compilationTaskParamsDTO.toString();
                    }
                    else{
                        SimulationTaskParamsDTO simulationTaskParamsDTO = new Gson().fromJson(jsonOfTaskParamsDTO, SimulationTaskParamsDTO.class);
                        WorkerEngine.getInstance().getRegisteredTasksParams().put(simulationTaskParamsDTO.getTaskName(), simulationTaskParamsDTO);
                        taskParams = simulationTaskParamsDTO.toString();
                    }
                    response.body().close();
                    ExecutionTargetsRefresherTimer.getInstance();
                    taskInfoTextArea.textProperty().set(taskParams);
                }
            }
        });
    }

    private void updateTasks(List<TaskDTO> taskDTOList){
        this.taskDTOS = new ArrayList<>(taskDTOList);
    }

    public SimpleStringProperty taskNameProperty() {
        return taskName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

