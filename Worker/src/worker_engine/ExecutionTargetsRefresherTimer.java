package worker_engine;

import com.google.gson.Gson;
import constants.Constants;
import dto.CompilationTaskParamsDTO;
import dto.SimulationTaskParamsDTO;
import dto.TargetDTO;
import dto.TaskParamsDTO;
import http_utils.HttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ExecutionTargetsRefresherTimer extends Timer {

    private static ExecutionTargetsRefresherTimer instance;

    private ExecutionTargetsRefresher refresher;

    private ExecutionTargetsRefresherTimer(){
        super(true);
        refresher = new ExecutionTargetsRefresher();
        this.schedule(refresher, Constants.TWO_SECS, Constants.TWO_SECS);
    }

    public static ExecutionTargetsRefresherTimer getInstance() {
        if(instance == null){
            instance = new ExecutionTargetsRefresherTimer();
        }
        return instance;
    }

    private class ExecutionTargetsRefresher extends TimerTask{
        public ExecutionTargetsRefresher() {
        }

        @Override
        public void run() {
            WorkerEngine.getInstance().updateCurrTargetsList();
            int numOfFreeThreads =WorkerEngine.getInstance().getNumOfFreeThreads();
            if(numOfFreeThreads <= 0){
                return;
            }

            if(WorkerEngine.getInstance().getRegisteredTasksParams().isEmpty()){
                return;
            }

            String finalUrl = Objects.requireNonNull(HttpUrl
                            .parse(Constants.TASK_EXECUTION)).newBuilder()
                    .addQueryParameter(Constants.NUMBER_OF_TARGETS, String.valueOf(numOfFreeThreads))
                    .build()
                    .toString();


            HttpUtils.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("OH NOOOOOOOOOO" + e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    //TODO: care bad request(400)=(When the uses is not signed in, and when the tasks not exist) and conflict
                    if(response.code() == 200){

                        String jsonArr = response.body().string();
                        System.out.println("In get targets==>" + jsonArr);
                        response.body().close();
                        Gson gson = new Gson();
                        String[] res = gson.fromJson(jsonArr, String[].class);
                        TargetDTO[] targetDTOS = gson.fromJson(res[0], TargetDTO[].class);
                        String[] taskNameToRemove = gson.fromJson(res[1], String[].class);
                        for(String taskNames: taskNameToRemove) {
                            if (WorkerEngine.getInstance().getRegisteredTasksParams().containsKey(taskNames)) {
                                WorkerEngine.getInstance().getRegisteredTasksParams().remove(taskNames);
                                System.out.println("UNREGISTER FROM TASK-->" + taskNames);
                            }
                        }
                        for (TargetDTO targetDTO : targetDTOS) {
                            System.out.println("Got target: " + targetDTO.getName());
                            String taskName = targetDTO.getTaskName();
                            TaskParamsDTO taskParamsDTO = WorkerEngine.getInstance().getRegisteredTasksParams().get(taskName);
                            if (taskParamsDTO instanceof SimulationTaskParamsDTO) {
                                SimulationTaskExecution simulationTaskExecution = new SimulationTaskExecution((SimulationTaskParamsDTO) taskParamsDTO, targetDTO);
                                WorkerEngine.getInstance().addTask(simulationTaskExecution);
                            }
                            if (taskParamsDTO instanceof CompilationTaskParamsDTO) {
                                CompilationTaskExecution compilationTaskExecution = new CompilationTaskExecution((CompilationTaskParamsDTO) taskParamsDTO, targetDTO);
                                WorkerEngine.getInstance().addTask(compilationTaskExecution);
                            }
                        }
                    }
                    else{
                        System.out.println("Got response->" + response.code() + " Body " + response.body().string());

                    }
                 }
            });

        }

    }


}
