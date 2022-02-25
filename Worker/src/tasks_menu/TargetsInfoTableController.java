package tasks_menu;

import dto.TargetDTO;
import dto.TaskDTO;
import dto.TaskParamsDTO;
import general_enums.TaskType;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Builder;
import javafx.util.Callback;
import worker_engine.WorkerEngine;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class TargetsInfoTableController {

    @FXML
    private TableView<TargetDTO> targetTable;

    @FXML
    private TableColumn<TargetDTO, String> targetName_column;

    @FXML
    private TableColumn<TargetDTO, String> taskName_column;

    @FXML
    private TableColumn<TargetDTO, String> taskType_column;

    @FXML
    private TableColumn<TargetDTO, String> runStatus_Column;

    @FXML
    private TableColumn<TargetDTO, String> price_column;

    @FXML
    private TableColumn<TargetDTO, Button> logs_column;


    @FXML
    private void initialize(){
        WorkerEngine.getInstance().addConsumer(this::updateTasksList);
        targetName_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TargetDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TargetDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getName());
            }
        });
        taskName_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TargetDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TargetDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getTaskName());
            }
        });
        taskType_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TargetDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TargetDTO, String> param) {
                TaskType taskType = WorkerEngine.getInstance().getRegisteredTasksParams().get(param.getValue().getTaskName()).getTaskType();
                return new SimpleStringProperty(taskType.name());
            }
        });
        runStatus_Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TargetDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TargetDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getRunStatus().name());
            }
        });
        price_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TargetDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TargetDTO, String> param) {
                TaskParamsDTO  task =WorkerEngine.getInstance().getRegisteredTasksParams().get(param.getValue().getTaskName());
                int price = task.getTotalTaskPrice();
                return new SimpleStringProperty(String.valueOf(price));

            }
        });
        logs_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TargetDTO, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<TargetDTO, Button> param) {

                Button button = new Button("Logs");
                button.onActionProperty().set(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //TODO: show logs
                    }
                });
                SimpleObjectProperty<Button> b = new SimpleObjectProperty<>(button);
                return b;
            }
        });
    }


    private void updateTasksList(List<TargetDTO> dtoTargets) {
        Platform.runLater(() -> {
            ObservableList<TargetDTO> targets = targetTable.getItems();
            targets.clear();
            targets.addAll(dtoTargets);
        });
    }







}
