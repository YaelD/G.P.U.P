package admin_login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginWindowController {

    final private String LOGIN_URL = "";//TODO: add login url

    @FXML
    private Button logInButton;

    @FXML
    private TextField userNameTextField;

    @FXML
    void OnLogin(ActionEvent event){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
        url(LOGIN_URL).build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Opps there is an error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                
            }
        });

    }

}
