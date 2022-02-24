package worker_engine;

import dto.TaskParamsDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkerEngine {

    private ExecutorService threadPool;

    private static WorkerEngine instance;

    private Map<String, TaskParamsDTO> registeredTasksParams;


    private WorkerEngine(int numOfThreads){
        this.threadPool = Executors.newFixedThreadPool(numOfThreads);
        this.registeredTasksParams = new HashMap<>();
    }

    public static WorkerEngine getInstance(int numOfThreads){
        if(instance == null){
            instance = new WorkerEngine(numOfThreads);
        }
        return instance;
    }

    public Map<String, TaskParamsDTO> getRegisteredTasksParams() {
        return registeredTasksParams;
    }

    public void addTask(Runnable run){
        threadPool.submit(run);

    }



}
