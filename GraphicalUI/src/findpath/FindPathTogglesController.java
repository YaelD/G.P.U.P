package findpath;

import dto.GraphDTO;
import dto.TargetDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FindPathTogglesController {

    @FXML
    private ChoiceBox<String> sourceTargetChoiceBox;

    @FXML
    private ChoiceBox<String> destinationTargetChoiceBox;

    @FXML
    private RadioButton dependsOnRadioButton;

    @FXML
    private RadioButton requiredForRadioButton;

    @FXML
    private Button findPathsButton;


    private void initialize(){

        ToggleGroup toggleGroup = new ToggleGroup();
        dependsOnRadioButton.setToggleGroup(toggleGroup);
        requiredForRadioButton.setToggleGroup(toggleGroup);
    }

    public void initChoiceBoxes(GraphDTO graphDTO){
        for(TargetDTO currTarget: graphDTO.getTargets().values()){
            this.sourceTargetChoiceBox.getItems().add(currTarget.getName());
            this.destinationTargetChoiceBox.getItems().add(currTarget.getName());
        }
    }

    @FXML
    void findPaths(ActionEvent event) {
        //TODO: fill after making the scene
    }

}
