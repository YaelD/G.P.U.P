package tasks_menu;

import constants.Constants;
import dto.TargetDTO;
import dto.TaskDTO;
import dto.TaskParamsDTO;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import worker_engine.WorkerEngine;

import javax.naming.Binding;
import java.io.IOException;
import java.util.Objects;

public class TaskMenuController {

    private SimpleIntegerProperty totalNumOfThreads;

    private SimpleStringProperty userName;

    @FXML
    private Label numOfThreadsLabel;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private Label taskNameLabel;

    @FXML
    private Label serverResponseLabel;

    @FXML
    private TableView<TaskDTO> taskInfoTable;

    @FXML
    private TasksInfoTableController taskInfoTableController;

    @FXML
    private TableView<TargetDTO> targetInfoTable;

    @FXML
    private TargetsInfoTableController targetInfoTableController;

    @FXML
    private ToggleButton pauseToggle;

    @FXML
    private TextArea targetLogConsole;

    @FXML
    private void initialize(){
        taskInfoTableController.setTaskMenuController(this);
        taskNameLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(WorkerEngine.getInstance().getPausedTasks().containsKey(newValue)){
                    pauseToggle.selectedProperty().setValue(true);
                }
                else if(WorkerEngine.getInstance().getRegisteredTasksParams().containsKey(newValue)){
                    pauseToggle.selectedProperty().setValue(false);
                }
                serverResponseLabel.setVisible(false);

            }
        });
        totalIncomeLabel.textProperty().bind(Bindings.convert(WorkerEngine.getInstance().totalCreditsProperty()));
        targetInfoTableController.setTaskMenuController(this);
        pauseToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue.booleanValue() == true){
                    pauseToggle.setText("Resume");
                    if(!taskNameLabel.textProperty().getValue().equals("")){
                        String taskName = taskNameLabel.textProperty().getValue();
                        if(WorkerEngine.getInstance().getRegisteredTasksParams().containsKey(taskName)){
                            TaskParamsDTO taskParamsDTO = WorkerEngine.getInstance().getRegisteredTasksParams().get(taskName);
                            WorkerEngine.getInstance().getRegisteredTasksParams().remove(taskName);
                            WorkerEngine.getInstance().getPausedTasks().put(taskName, taskParamsDTO);
                        }
                    }
                }
                else{
                    pauseToggle.setText("Pause");
                    String taskName = taskNameLabel.textProperty().getValue();
                    if(WorkerEngine.getInstance().getPausedTasks().containsKey(taskName)){
                        TaskParamsDTO taskParamsDTO = WorkerEngine.getInstance().getPausedTasks().get(taskName);
                        WorkerEngine.getInstance().getPausedTasks().remove(taskName);
                        WorkerEngine.getInstance().getRegisteredTasksParams().put(taskName, taskParamsDTO);
                    }

                }
            }
        });
    }

    public TaskMenuController() {
        this.userName = new SimpleStringProperty();
        this.totalNumOfThreads = new SimpleIntegerProperty();
    }

    public void setTotalNumOfThreads(SimpleIntegerProperty totalNumOfThreads) {
        this.totalNumOfThreads.bind(totalNumOfThreads);
        numOfThreadsLabel.textProperty().bind(Bindings.format("%d/%d", WorkerEngine.getInstance().numOfFreeThreadsProperty(),totalNumOfThreads));

    }

    public void showInConsole(String str){
        Platform.runLater(()->{
            this.targetLogConsole.setText(str);
        });
    }


    public void setUserName(SimpleStringProperty userName) {
        this.userName.bind(userName);
    }


    public void onStop(ActionEvent actionEvent) {
        if(taskNameLabel.textProperty().getValue().isEmpty()){
            return;
        }
        serverResponseLabel.setVisible(false);
        String finalUrl = Objects.requireNonNull(HttpUrl
                        .parse(Constants.UNREGISTER_EXECUTION)).newBuilder()
                .addQueryParameter(Constants.TASK_NAME, taskNameLabel.textProperty().getValue())
                .build()
                .toString();

        Request request= new Request.Builder().url(finalUrl).put(RequestBody.create(new byte[0])).build();
        HttpUtils.runAsyncWithRequest(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(()->{
                    if(response.code() == 200){
//                        System.out.println("IN RETURN GOOD RESPONSE->" +taskNameLabel.textProperty().getValue());

                        WorkerEngine.getInstance().getRegisteredTasksParams().remove(taskNameLabel.textProperty().getValue());
                        WorkerEngine.getInstance().getPausedTasks().remove(taskNameLabel.textProperty().getValue());

                        serverResponseLabel.setText("Successfully removed!");
                    }
                });
            }
        });

    }

    public void setTaskNameLabel(String newTaskName) {
        this.taskNameLabel.setText(newTaskName);
    }
}
