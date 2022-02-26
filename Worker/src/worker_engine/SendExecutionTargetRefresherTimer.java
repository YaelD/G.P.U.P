package worker_engine;

import com.google.gson.Gson;
import constants.Constants;
import dto.ExecutionTargetDTO;
import general_enums.RunStatus;
import http_utils.HttpUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalTime;
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
            this.executionTargetRefresher.targets.add(target);
    }



        private class SendExecutionTargetRefresher extends TimerTask {

            private Set<ExecutionTarget> targets;

            public SendExecutionTargetRefresher() {
                this.targets = new HashSet<>();
            }



            @Override
            public void run() {
                WorkerEngine.getInstance().updateCurrTargetsList();
                Set<ExecutionTarget> targetsToRemove = new HashSet<>();
                for(ExecutionTarget target : this.targets){
                    if(target.getRunStatus().equals(RunStatus.FINISHED)){
                        targetsToRemove.add(target);
                    }
                    sendInfoToServer(target);
                }
                for(ExecutionTarget target : targetsToRemove){
                    this.targets.remove(target);
                }
            }



            private void sendInfoToServer(ExecutionTarget target){
//                ExecutionTargetDTO dto = target.makeDTO();
//                String finalUrl = Objects.requireNonNull(HttpUrl
//                                .parse(Constants.TASK_EXECUTION)).newBuilder()
//                        .addQueryParameter(Constants.TASK_NAME, dto.getTaskName())
//                        .build()
//                        .toString();
//                String targetJson = new Gson().toJson(dto);
//                System.out.println(LocalTime.now() + "-in sending execution DTO, Sendning==>"  + targetJson);
//                Request request= new Request.Builder().url(finalUrl).post(RequestBody.create(targetJson.getBytes())).build();
//                HttpUtils.runAsyncWithRequest(request, new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        System.out.println("Exeption :((==" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        System.out.println("");
//                        if(response.code() == 200){
//                            String payment = response.body().string();
//                            System.out.println(LocalTime.now() +"-in sending execution DTO, Received payment==>" + payment);
//                            payment.replace("\"", "");
//                            if(!payment.isEmpty()){
//                                WorkerEngine.getInstance().addCredits(Integer.valueOf(payment));
//                            }
//                        }
//
//                        response.body().close();
//                    }
//                });

            }

        }
    }

