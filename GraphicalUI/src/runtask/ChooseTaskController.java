package runtask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleGroup;

public class ChooseTaskController {

    @FXML
    private Button continueButton;

    @FXML
    private ChoiceBox<Integer> numOfThreadsCB;

    @FXML
    private ToggleGroup taskToggleGroup;

    @FXML
    private ToggleGroup runTypeToggle;

    @FXML
    private Button returnButton;

    @FXML
    void enableNextPanel(ActionEvent event) {

    }

    @FXML
    void returnToPrevPanel(ActionEvent event) {

    }

}

