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
import java.util.*;
import java.util.function.Consumer;

public class TaskListRefresherTimer extends Timer {

    private static TaskListRefresherTimer instance;
    private TaskListRefresher taskListRefresher;

    public synchronized static TaskListRefresherTimer getInstance(){
        if(instance == null){
            instance = new TaskListRefresherTimer();
        }
        return instance;
    }

    public synchronized void addConsumer(Consumer<List<TaskDTO>> consumer){
        this.taskListRefresher.addConsumer(consumer);
    }


    private TaskListRefresherTimer() {
        super(true);
//        taskListRefresher = TaskListRefresher.getInstance();
        taskListRefresher = new TaskListRefresher();
        this.schedule(taskListRefresher, Constants.TWO_SECS, Constants.TWO_SECS);
    }



    private class TaskListRefresher extends TimerTask {

        private final List<Consumer<List<TaskDTO>>> consumersList;


        private TaskListRefresher() {
            consumersList = new ArrayList<>();
        }


        public void addConsumer(Consumer<List<TaskDTO>> consumer){
            this.consumersList.add(consumer);
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
                        List<TaskDTO> taskDTOList = Arrays.asList(taskDTOS);
                        taskDTOList.sort(new Comparator<TaskDTO>() {
                            @Override
                            public int compare(TaskDTO o1, TaskDTO o2) {
                                return o1.getTaskName().compareToIgnoreCase(o2.getTaskName());
                            }
                        });
                        consumersList.forEach(consumer -> {consumer.accept(taskDTOList);});
                    }
                    response.body().close();
                }
            });
        }
    }
}
