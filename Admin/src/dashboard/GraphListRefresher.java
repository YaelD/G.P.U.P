package dashboard;

import com.google.gson.Gson;
import constants.Constants;
import dto.GraphDTO;
import http_utils.HttpUtils;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;


public class GraphListRefresher extends TimerTask {

    private final Consumer<List<GraphDTO>> graphListConsumer;

    public GraphListRefresher(Consumer<List<GraphDTO>> graphListConsumer) {
        this.graphListConsumer = graphListConsumer;
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
                if(response.code() ==200){
                    String jsonArrayOfGraphsList = response.body().string();
                    List<GraphDTO> graphs = new Gson().fromJson(jsonArrayOfGraphsList, List.class);
                    graphListConsumer.accept(graphs);
                }
                response.body().close();

            }
        });
    }
}
