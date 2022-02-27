package tasks_menu;

import RefreshingItems.TaskListRefresherTimer;
import dto.TargetDTO;
import dto.TaskDTO;
import general_enums.RunStatus;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import worker_engine.ExecutionTarget;
import worker_engine.WorkerEngine;

import java.util.*;
import java.util.function.Predicate;

public class TasksInfoTableController {

    @FXML
    private TableView<TaskDTO> taskInfoTable;

    @FXML
    private TableColumn<TaskDTO, String> taskName_column;

    @FXML
    private TableColumn<TaskDTO, String> numOfWorkers_column;

    @FXML
    private TableColumn<TaskDTO, String> taskProgressColumn;

    @FXML
    private TableColumn<TaskDTO, String> myNumberOfTargets_column;

    @FXML
    private TableColumn<TaskDTO, String> totalTaskCredits_column;

    private TaskMenuController taskMenuController;

    @FXML
    private void initialize(){
        TaskListRefresherTimer.getInstance().addConsumer(this::updateTasksList);
        taskInfoTable.setRowFactory(new Callback<TableView<TaskDTO>, TableRow<TaskDTO>>() {
            @Override
            public TableRow<TaskDTO> call(TableView<TaskDTO> param) {
                TableRow<TaskDTO> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                        taskMenuController.setTaskNameLabel(row.getItem().getTaskName());
                    }
                });

                return row;
            }
        });
        taskName_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TaskDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TaskDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getTaskName());
            }
        });
        numOfWorkers_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TaskDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TaskDTO, String> param) {
                return new SimpleStringProperty(String.valueOf(param.getValue().getNumOfRegisteredWorkers()) );
            }
        });
        taskProgressColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TaskDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TaskDTO, String> param) {
                double numOfTargets = Double.valueOf(param.getValue().getGraphDTO().getTotalNumOfTargets());
                double numOfFinished = param.getValue().getGraphDTO().getNumOfTargetsByRunStatus(RunStatus.FINISHED);
                double numOfSkipped = param.getValue().getGraphDTO().getNumOfTargetsByRunStatus(RunStatus.SKIPPED);
                double totalFinishedTargets = numOfFinished + numOfSkipped;
                return new SimpleStringProperty(String.valueOf(100*( totalFinishedTargets/numOfTargets)) + "%");
            }
        });
        myNumberOfTargets_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TaskDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TaskDTO, String> param) {
                Set<ExecutionTarget> tempSet = new HashSet<>(WorkerEngine.getInstance().getWorkerTargets());
                tempSet.stream().filter(new Predicate<ExecutionTarget>() {
                    @Override
                    public boolean test(ExecutionTarget targetDTO) {
                        return param.getValue().getTaskName().equals(targetDTO.getTaskName());
                    }
                });

                return new SimpleStringProperty(String.valueOf(tempSet.size()));
            }
        });
        totalTaskCredits_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TaskDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TaskDTO, String> param) {
                Set<ExecutionTarget> tempSet = new HashSet<>(WorkerEngine.getInstance().getWorkerTargets());
                tempSet.stream().filter(new Predicate<ExecutionTarget>() {
                    @Override
                    public boolean test(ExecutionTarget targetDTO) {
                        return param.getValue().getTaskName().equals(targetDTO.getTaskName());
                    }
                });
                int val = 0;
                switch (param.getValue().getTaskType()){
                    case COMPILATION_TASK:
                        val = tempSet.size() * param.getValue().getGraphDTO().getPriceOfCompilationTask();
                        break;
                    case SIMULATION_TASK:
                        val = tempSet.size() * param.getValue().getGraphDTO().getPriceOfSimulationTask();
                }
                return new SimpleStringProperty(String.valueOf(val));
            }
        });
    }


    private void updateTasksList(List<TaskDTO> tasks) {
        Platform.runLater(() -> {
            List<TaskDTO> taskList = new ArrayList<>();
            for(TaskDTO taskDTO : tasks){
                if(WorkerEngine.getInstance().getRegisteredTasksParams().containsKey(taskDTO.getTaskName()) ||
                WorkerEngine.getInstance().getPausedTasks().containsKey(taskDTO.getTaskName())){
                    taskList.add(taskDTO);
                }
            }
            ObservableList<TaskDTO> taskDTOObservableList = taskInfoTable.getItems();
            taskDTOObservableList.clear();
            taskDTOObservableList.addAll(taskList);
        });
    }

    public void setTaskMenuController(TaskMenuController taskMenuController) {
        this.taskMenuController = taskMenuController;
    }


}
