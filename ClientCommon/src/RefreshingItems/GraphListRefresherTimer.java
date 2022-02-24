package RefreshingItems;

import com.google.gson.Gson;
import constants.Constants;
import dto.GraphDTO;
import dto.TaskDTO;
import http_utils.HttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class GraphListRefresherTimer extends Timer {


    private static GraphListRefresherTimer instance;
    private GraphListRefresher graphListRefresher;

    public synchronized static GraphListRefresherTimer getInstance(){
        if(instance == null){
            instance = new GraphListRefresherTimer();
        }
        return instance;
    }

    public synchronized void addConsumer(Consumer<List<GraphDTO>> consumer){
        this.graphListRefresher.addConsumer(consumer);
    }


    private GraphListRefresherTimer() {
        super(true);
//        taskListRefresher = TaskListRefresher.getInstance();
        graphListRefresher = new GraphListRefresher();
        this.schedule(graphListRefresher, Constants.TWO_SECS, Constants.TWO_SECS);
    }



    private class GraphListRefresher extends TimerTask {

        private final List<Consumer<List<GraphDTO>>> consumersList;

        public GraphListRefresher() {
            this.consumersList = new ArrayList<>();
        }

        public void addConsumer(Consumer<List<GraphDTO>> consumer){
            this.consumersList.add(consumer);
        }

        @Override
        public void run() {

            HttpUtils.runAsync(Constants.GRAPH_LIST, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    //TODO
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() == 200) {
                        String jsonArrayOfGraphsList = response.body().string();
                        GraphDTO[] graphs = new Gson().fromJson(jsonArrayOfGraphsList, GraphDTO[].class);
                        List<GraphDTO> graphDTOS = Arrays.asList(graphs);
                        graphDTOS.sort(new Comparator<GraphDTO>() {
                            @Override
                            public int compare(GraphDTO o1, GraphDTO o2) {
                                return o1.getName().compareToIgnoreCase(o2.getName());
                            }
                        });
                        consumersList.forEach(consumer -> {consumer.accept(graphDTOS);});
                    }
                    response.body().close();
                }
            });
        }
    }
}
