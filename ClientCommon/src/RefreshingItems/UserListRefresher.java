package RefreshingItems;

import com.google.gson.Gson;
import constants.Constants;
import dto.UserDTO;
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


public class UserListRefresher extends TimerTask {

    private final Consumer<List<UserDTO>> usersListConsumer;

    public UserListRefresher(Consumer<List<UserDTO>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;
    }

    @Override
    public void run() {

        HttpUtils.runAsync(Constants.USERS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //TODO
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsers = response.body().string();
                UserDTO[] usersList = new Gson().fromJson(jsonArrayOfUsers, UserDTO[].class);
                usersListConsumer.accept(Arrays.asList(usersList));
            }
        });
    }
}
