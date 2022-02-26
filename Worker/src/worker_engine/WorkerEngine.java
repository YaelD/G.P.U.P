package worker_engine;

import RefreshingItems.TaskListRefresherTimer;
import dto.TaskDTO;
import dto.TaskParamsDTO;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.function.Consumer;

public class WorkerEngine {

    private PausableThreadPoolExecutor pausableThreadPoolExecutor;
    private static WorkerEngine instance;



    private SimpleIntegerProperty totalCredits;
    private SimpleIntegerProperty numOfFreeThreads;

    private SimpleListProperty<TaskDTO> systemTasks;
    private List<Consumer< List<ExecutionTarget>>> targetsConsumer;
    private Map<String, TaskParamsDTO> registeredTasksParams;
    private Set<ExecutionTarget> workerTargets;
    private Set<ExecutionTarget> currentRunningTargets;

    private WorkerEngine(){
        totalCredits = new SimpleIntegerProperty();
        numOfFreeThreads = new SimpleIntegerProperty();
        this.registeredTasksParams = new HashMap<>();
        workerTargets = new HashSet<>();
        currentRunningTargets = new HashSet<>();
        systemTasks = new SimpleListProperty<>();
        targetsConsumer = new ArrayList<>();
        TaskListRefresherTimer.getInstance().addConsumer(this::setSystemTasks);
    }

    public SimpleIntegerProperty numOfFreeThreadsProperty() {
        return numOfFreeThreads;
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

    public SimpleIntegerProperty totalCreditsProperty() {
        return totalCredits;
    }


    public void addCredits(int credits){
        Platform.runLater(()->{
            totalCredits.set(totalCredits.getValue() + credits);
        });
    }

    public synchronized void setSystemTasks(List<TaskDTO> systemTasks) {
        ObservableList<TaskDTO> taskDTOS = FXCollections.observableArrayList(systemTasks);
        this.systemTasks.set(taskDTOS);
    }

    public Map<String, TaskParamsDTO> getRegisteredTasksParams() {
        return registeredTasksParams;
    }

    public Set<ExecutionTarget> getWorkerTargets() {
        return workerTargets;
    }

    public synchronized void addConsumer(Consumer<List<ExecutionTarget>> dtoConsumer){
        this.targetsConsumer.add(dtoConsumer);
    }

    public synchronized void addWorkerTarget(ExecutionTarget targetDTO){
        workerTargets.add(targetDTO);
        this.currentRunningTargets.add(targetDTO);
    }

    public void updateCurrTargetsList(){
        List<ExecutionTarget> list = new ArrayList<>(workerTargets);
        targetsConsumer.forEach(listConsumer -> listConsumer.accept(list));
    }

    public void addTask(Runnable run){
        if(pausableThreadPoolExecutor == null){
            return;
        }
        Platform.runLater(()->{
            numOfFreeThreads.set(pausableThreadPoolExecutor.getMaximumPoolSize() - pausableThreadPoolExecutor.getActiveCount());
        });
        pausableThreadPoolExecutor.execute(run);

    }

    public int getNumOfFreeThreads(){
        if(pausableThreadPoolExecutor == null){
            return 0;
        }
        Platform.runLater(()->{
            numOfFreeThreads.set(pausableThreadPoolExecutor.getMaximumPoolSize() - pausableThreadPoolExecutor.getActiveCount());
        });
        return numOfFreeThreads.getValue();
    }

    public void closeThreadPool(){
        if(pausableThreadPoolExecutor != null){
            pausableThreadPoolExecutor.shutdown();
        }
    }

}
