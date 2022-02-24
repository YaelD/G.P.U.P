package worker_engine;

import com.google.gson.Gson;
import constants.Constants;
import dto.ExecutionTargetDTO;
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
        executionTargetRefresher = new SendExecutionTargetRefresher();
        this.schedule(executionTargetRefresher, Constants.TWO_SECS, Constants.TWO_SECS);
    }

    public void addTarget(ExecutionTarget target){
            this.executionTargetRefresher.targetet.add(target);
    }



        private class SendExecutionTargetRefresher extends TimerTask {

            private Set<ExecutionTarget> targetet;

            public SendExecutionTargetRefresher() {
                this.targetet = new HashSet<>();
            }



            @Override
            public void run() {
                for(ExecutionTarget target : this.targetet){
                    ExecutionTargetDTO dto = target.makeDTO();
                    String finalUrl = Objects.requireNonNull(HttpUrl
                            .parse(Constants.TASK_LIST)).newBuilder()
                            .addQueryParameter(Constants.TASK_NAME, dto.getTaskName())
                            .build()
                            .toString();
                    String targetJson = new Gson().toJson(dto);
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

