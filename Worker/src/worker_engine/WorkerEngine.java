package worker_engine;

import dto.TargetDTO;
import dto.TaskParamsDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class WorkerEngine {

    private PausableThreadPoolExecutor pausableThreadPoolExecutor;
    private static WorkerEngine instance;

    private Map<String, TaskParamsDTO> registeredTasksParams;
    private Set<TargetDTO> workerTargets;


    private WorkerEngine(){
        this.registeredTasksParams = new HashMap<>();
        workerTargets = new HashSet<>();
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

    public Map<String, TaskParamsDTO> getRegisteredTasksParams() {
        return registeredTasksParams;
    }

    public Set<TargetDTO> getWorkerTargets() {
        return workerTargets;
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
