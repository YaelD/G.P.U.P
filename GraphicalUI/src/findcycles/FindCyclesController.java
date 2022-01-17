package findcycles;

import dto.TargetDTO;
import engine.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

public class FindCyclesController {

    private Engine engine;

    @FXML
    private ChoiceBox<String> targetsChoiceBox;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> cyclesListView;

    @FXML
    void searchCycles(ActionEvent event) {

    }

    private void initTargetsChoiceBox(){
        for(TargetDTO targetDTO: this.engine.getGraphDTO().getTargets().values()){
            this.targetsChoiceBox.getItems().add(targetDTO.getName());
        }
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}

