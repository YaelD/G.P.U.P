package admin_login;

import dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class LoginWindowController {

    final private String LOGIN_URL = "";//TODO: add login url

    private Stage primaryStage;

    @FXML
    private Button logInButton;

    @FXML
    private TextField userNameTextField;

    @FXML
    void OnLogin(ActionEvent event){

        try {
            URL resource = DashboardController.class.getResource("dashboard.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(resource);
            Parent root = fxmlLoader.load(resource.openStream());
            Scene scene = new Scene(root, 1000, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
