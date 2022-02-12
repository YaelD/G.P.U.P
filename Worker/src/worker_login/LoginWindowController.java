package worker_login;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class LoginWindowController {

    @FXML
    private Button logInButton;

    @FXML
    private TextField userNameTextField;

    @FXML
    private ChoiceBox<Integer> numOfThreadsChoiceBox;


    @FXML
    void OnLogin(ActionEvent event) {

    }

}
