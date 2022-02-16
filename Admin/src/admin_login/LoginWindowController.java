package admin_login;

import constants.Constants;
import container.TopContainerController;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginWindowController {

    @FXML private Button logInButton;
    @FXML private TextField userNameTextField;
    @FXML private Label warningLabel;


    TopContainerController topContainerController;

    private SimpleStringProperty errorMessageProperty;

    public LoginWindowController() {
        errorMessageProperty = new SimpleStringProperty();
    }

    @FXML
    private void initialize(){
        this.warningLabel.textProperty().bind(errorMessageProperty);
    }

    public void setTopContainerController(TopContainerController topContainerController) {
        this.topContainerController = topContainerController;
    }

    @FXML void OnLogin(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName).addQueryParameter("userType", "admin")
                .build()
                .toString();

       // updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpUtils.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                warningLabel.setVisible(true);
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        //TODO: ADD UPDATING USER NAME LABEL IN THE DASHBOARD ;)
                        //chatAppMainController.updateUserName(userName);
                        errorMessageProperty.set("logged in successfully");
                        topContainerController.setUserName(userName);
                        topContainerController.switchToDashboard();
                        //chatAppMainController.switchToChatRoom();
                    });
                }
            }
        });
    }
}
