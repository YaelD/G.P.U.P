package worker_dashboard;

import com.google.gson.Gson;
import constants.Constants;
import dto.CompilationTaskParamsDTO;
import dto.SimulationTaskParamsDTO;
import dto.TaskParamsDTO;
import general_enums.TaskType;
import http_utils.HttpUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import worker_engine.SimulationTaskExecution;
import worker_engine.WorkerEngine;

import java.io.IOException;
import java.util.Objects;

public class RegisterToTaskController {
    private SimpleStringProperty taskName;

    @FXML
    private Label taskNameLabel;

    @FXML
    private TextArea taskInfoTextArea;

    @FXML
    private Button registerToTaskBtn;


    public RegisterToTaskController() {
        taskName = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
        taskNameLabel.textProperty().bind(taskName);
    }

    @FXML
    void OnClickRegisterButton(ActionEvent event) {
        String finalUrl = Objects.requireNonNull(HttpUrl
                .parse(Constants.REGISTER_TO_TASK)).newBuilder()
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
                String taskParams;
                String jsonOfTaskParamsDTO = response.body().string();
                TaskParamsDTO taskParamsDTOS = new Gson().fromJson(jsonOfTaskParamsDTO, TaskParamsDTO.class);
                if(taskParamsDTOS.getTaskType().equals(TaskType.COMPILATION_TASK)){
                    CompilationTaskParamsDTO compilationTaskParamsDTO = new Gson().fromJson(jsonOfTaskParamsDTO, CompilationTaskParamsDTO.class);
                    WorkerEngine.getInstance(1).getRegisteredTasksParams().put(compilationTaskParamsDTO.getTaskName(), compilationTaskParamsDTO);
                    taskParams = compilationTaskParamsDTO.toString();
                }
                else{
                    SimulationTaskParamsDTO simulationTaskParamsDTO = new Gson().fromJson(jsonOfTaskParamsDTO, SimulationTaskParamsDTO.class);
                    WorkerEngine.getInstance(1).getRegisteredTasksParams().put(simulationTaskParamsDTO.getTaskName(), simulationTaskParamsDTO);
                    taskParams = simulationTaskParamsDTO.toString();
                }
                response.body().close();
                taskInfoTextArea.textProperty().set(taskParams);
            }
        });
    }

    public SimpleStringProperty taskNameProperty() {
        return taskName;
    }


}

