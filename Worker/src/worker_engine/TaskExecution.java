package worker_engine;

import com.google.gson.Gson;
import constants.Constants;
import dto.ExecutionTargetDTO;
import dto.TargetDTO;
import http_utils.HttpUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Objects;

public class TaskExecution {

    ExecutionTarget executionTarget;

    public TaskExecution(TargetDTO targetDTO) {
        executionTarget = new ExecutionTarget(targetDTO);
        WorkerEngine.getInstance().addWorkerTarget(executionTarget);

    }

    protected void sendTarget(){
        ExecutionTargetDTO dto = executionTarget.makeDTO();
        String finalUrl = Objects.requireNonNull(HttpUrl
                        .parse(Constants.TASK_EXECUTION)).newBuilder()
                .addQueryParameter(Constants.TASK_NAME, dto.getTaskName())
                .build()
                .toString();
        String targetJson = new Gson().toJson(dto);
        System.out.println(LocalTime.now() + "-in sending execution DTO, Sendning==>"  + targetJson);
        Request request= new Request.Builder().url(finalUrl).post(RequestBody.create(targetJson.getBytes())).build();
        HttpUtils.runAsyncWithRequest(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Exeption :((==" + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("");
                if(response.code() == 200){
                    String payment = response.body().string();
                    System.out.println(LocalTime.now() +"-in sending execution DTO, Received payment==>" + payment);
                    payment.replace("\"", "");
                    if(!payment.isEmpty()){
                        WorkerEngine.getInstance().addCredits(Integer.valueOf(payment));
                    }
                }

                response.body().close();
            }
        });

    }

}
