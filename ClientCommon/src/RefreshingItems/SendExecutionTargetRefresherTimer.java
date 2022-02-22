package RefreshingItems;

import com.google.gson.Gson;
import constants.Constants;
import dto.ExecutionTargetDTO;
import dto.GraphDTO;
import general_enums.TaskStatus;
import http_utils.HttpUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class SendExecutionTargetRefresherTimer extends Timer {

        private static SendExecutionTargetRefresherTimer instance;
        private SendExecutionTargetRefresher executionTargetRefresher;

        public synchronized static SendExecutionTargetRefresherTimer getInstance(){
        if(instance == null){
            instance = new SendExecutionTargetRefresherTimer();
        }
        return instance;
    }

    private SendExecutionTargetRefresherTimer() {
        super(true);
//        taskListRefresher = TaskListRefresher.getInstance();
        executionTargetRefresher = new SendExecutionTargetRefresher();
        this.schedule(executionTargetRefresher, Constants.TWO_SECS, Constants.TWO_SECS);
    }



        private class SendExecutionTargetRefresher extends TimerTask {

            private Map<String, ExecutionTargetDTO> executionTargetDTOMap;


            public SendExecutionTargetRefresher() {
                this.executionTargetDTOMap = new HashMap<>();
            }

            public void addExecutionTarget(ExecutionTargetDTO executionTargetDTO){
                this.executionTargetDTOMap.put(executionTargetDTO.getTaskName(), executionTargetDTO);
            }

            @Override
            public void run() {
                for(Map.Entry<String, ExecutionTargetDTO> entry : this.executionTargetDTOMap.entrySet()){
                    //TODO: change the URL of the request!
                    String finalUrl = Objects.requireNonNull(HttpUrl
                            .parse(Constants.TASK_LIST)).newBuilder()
                            .addQueryParameter(Constants.TASK_NAME, entry.getKey())
                            .build()
                            .toString();
                    String targetJson = new Gson().toJson(entry.getValue());
                    Request request= new Request.Builder().url(finalUrl).put(RequestBody.create(targetJson.getBytes())).build();
                    HttpUtils.runAsyncWithRequest(request, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        }
                    });

                }


            }
        }
    }

