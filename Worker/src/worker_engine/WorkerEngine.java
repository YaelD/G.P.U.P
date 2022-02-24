package worker_engine;

import dto.TaskParamsDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WorkerEngine {

    private ThreadPoolExecutor threadPool;

    private PausableThreadPoolExecutor pausableThreadPoolExecutor;


    private static WorkerEngine instance;

    private Map<String, TaskParamsDTO> registeredTasksParams;


    private WorkerEngine(int numOfThreads){
        this.pausableThreadPoolExecutor = new PausableThreadPoolExecutor(numOfThreads, new ArrayBlockingQueue<Runnable>(8));
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
        threadPool.execute(run);
    }

    public int getNumOfFreeThreads(){
        return 1;
    }



}
