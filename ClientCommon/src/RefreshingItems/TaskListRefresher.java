package RefreshingItems;

import com.google.gson.Gson;
import constants.Constants;
import dto.TaskDTO;
import http_utils.HttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class TaskListRefresher extends TimerTask {

        private final Consumer<List<TaskDTO>> taskListConsumer;

    public TaskListRefresher(Consumer < List < TaskDTO >> taskListConsumer) {
        this.taskListConsumer = taskListConsumer;
    }

        @Override
        public void run () {

        HttpUtils.runAsync(Constants.TASK_LIST, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //TODO
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String jsonArrayOfTasksList = response.body().string();
                    TaskDTO[] taskDTOS = new Gson().fromJson(jsonArrayOfTasksList, TaskDTO[].class);
                    taskListConsumer.accept(Arrays.asList(taskDTOS));
                }
                response.body().close();
            }
        });
    }
}
