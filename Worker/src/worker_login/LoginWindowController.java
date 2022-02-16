package worker_login;

import constants.Constants;
import container.TopContainerController;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginWindowController {

    SimpleStringProperty errorMessageProperty;


    TopContainerController topContainerController;

    @FXML
    private Button logInButton;

    @FXML
    private TextField userNameTextField;

    @FXML
    private Label warningLabel;

    @FXML
    private ChoiceBox<Integer> numOfThreadsChoiceBox;

    public LoginWindowController() {
        this.errorMessageProperty = new SimpleStringProperty();
    }

    @FXML
    void OnLoginClick(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("userType", "worker")
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
                        errorMessageProperty.set("logged in successfully");
                        topContainerController.setUserName(userName);
                        topContainerController.switchToDashboard();
                    });
                }
            }
        });

    }



    @FXML
    private void initialize(){
        this.warningLabel.textProperty().bind(this.errorMessageProperty);
        ObservableList<Integer> numOfThreads = FXCollections.observableArrayList();
        numOfThreads.addAll(1,2,3,4,5);
        numOfThreadsChoiceBox.setItems(numOfThreads);
        numOfThreadsChoiceBox.getSelectionModel().select(0);

    }

    public void setTopContainerController(TopContainerController topContainerController) {
        this.topContainerController = topContainerController;
        topContainerController.getNumOfThreads().bind(this.numOfThreadsChoiceBox.valueProperty());
    }



}
