package worker_engine;

import RefreshingItems.TaskListRefresherTimer;
import constants.Constants;
import dto.TargetDTO;
import dto.TaskDTO;
import dto.TaskParamsDTO;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class WorkerEngine {

    private PausableThreadPoolExecutor pausableThreadPoolExecutor;
    private static WorkerEngine instance;

    private SimpleListProperty<TaskDTO> systemTasks;
    private List<Consumer< List<TargetDTO>>> targetsConsumer;
    private Map<String, TaskParamsDTO> registeredTasksParams;
    private Set<TargetDTO> workerTargets;

    private WorkerEngine(){
        this.registeredTasksParams = new HashMap<>();
        workerTargets = new HashSet<>();
        systemTasks = new SimpleListProperty<>();
        targetsConsumer = new ArrayList<>();
        TaskListRefresherTimer.getInstance().addConsumer(this::setSystemTasks);
    }

    public void setPausableThreadPoolExecutor(PausableThreadPoolExecutor pausableThreadPoolExecutor) {
        if(this.pausableThreadPoolExecutor == null){
            this.pausableThreadPoolExecutor = pausableThreadPoolExecutor;
        }
    }

    public static WorkerEngine getInstance(){
        if(instance == null){
            instance = new WorkerEngine();
        }
        return instance;
    }



    public void setSystemTasks(List<TaskDTO> systemTasks) {
        ObservableList<TaskDTO> taskDTOS = FXCollections.observableArrayList(systemTasks);
        this.systemTasks.set(taskDTOS);
    }

    public Map<String, TaskParamsDTO> getRegisteredTasksParams() {
        return registeredTasksParams;
    }

    public Set<TargetDTO> getWorkerTargets() {
        return workerTargets;
    }

    public void addConsumer(Consumer<List<TargetDTO>> dtoConsumer){
        this.targetsConsumer.add(dtoConsumer);
    }

    public void addWorkerTarget(TargetDTO targetDTO){

        workerTargets.add(targetDTO);
        List<TargetDTO> list = new ArrayList<>(workerTargets);
        targetsConsumer.forEach(listConsumer -> listConsumer.accept(list));
    }

    public void addTask(Runnable run){
        if(pausableThreadPoolExecutor == null){
            return;
        }
        pausableThreadPoolExecutor.execute(run);
    }

    public int getNumOfFreeThreads(){
        if(pausableThreadPoolExecutor == null){
            return 0;
        }
        return pausableThreadPoolExecutor.getMaximumPoolSize() - pausableThreadPoolExecutor.getActiveCount();
    }

    public void closeThreadPool(){
        pausableThreadPoolExecutor.shutdown();
    }

}
