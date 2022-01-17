package findcycles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

public class FindCyclesController {

    @FXML
    private ChoiceBox<String> targetsChoiceBox;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> cyclesListView;

    @FXML
    void searchCycles(ActionEvent event) {

    }

}

