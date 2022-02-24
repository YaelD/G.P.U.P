package RefreshingItems;

import com.google.gson.Gson;
import constants.Constants;
import dto.TaskDTO;
import dto.UserDTO;
import http_utils.HttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class UserListRefresherTimer extends Timer {

    private static UserListRefresherTimer instance;
    private UserListRefresher userListRefresher;

    private UserListRefresherTimer(){
        super(true);
        userListRefresher = new UserListRefresher();
        this.schedule(userListRefresher, Constants.TWO_SECS, Constants.TWO_SECS);
    }

    public static UserListRefresherTimer getInstance(){
        if(instance == null){
            instance = new UserListRefresherTimer();
        }
        return instance;
    }

    public void addConsumer(Consumer<List<UserDTO>> consumer){
        this.userListRefresher.addConsumer(consumer);

    }

    private class UserListRefresher extends TimerTask {

        private final List<Consumer<List<UserDTO>>> consumers;

        public UserListRefresher() {
            this.consumers = new ArrayList<>();
        }

        public void addConsumer(Consumer<List<UserDTO>> consumer){
            this.consumers.add(consumer);
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
                    if(response.code() == 200){
                        String jsonArrayOfUsers = response.body().string();
                        UserDTO[] usersList = new Gson().fromJson(jsonArrayOfUsers, UserDTO[].class);
                        List<UserDTO> userDTOS = Arrays.asList(usersList);
                        userDTOS.sort(new Comparator<UserDTO>() {
                            @Override
                            public int compare(UserDTO o1, UserDTO o2) {
                                return o1.getUserName().compareToIgnoreCase(o2.getUserName());
                            }
                        });
                        consumers.forEach(consumer -> {consumer.accept(userDTOS);});
                    }
                    response.body().close();
                }
            });
        }

    }


}



