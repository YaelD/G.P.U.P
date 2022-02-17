package whatif;

//import dto.GraphDTO;
//import dto.TargetDTO;
//import graph.Dependency;
import dto.GraphDTO;
import dto.TargetDTO;
import general_enums.Dependency;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class WhatIfToggleController {

    @FXML
    private ChoiceBox<String> sourceTargetChoiceBox;

    @FXML
    private RadioButton dependsOnRadioButton;

    @FXML
    private ToggleGroup dependencyToggleGroup;

    @FXML
    private RadioButton requiredForRadioButton;

    @FXML
    private Button findWhatIfButton;

    @FXML
    private Label warningLabel;

    private WhatIfCallback whatIfCallback;




    @FXML
    void findTargets(ActionEvent event) {
        String sourceTarget = sourceTargetChoiceBox.getValue();
        RadioButton currSelected = (RadioButton) dependencyToggleGroup.getSelectedToggle();
        Dependency dependency;
        if(currSelected.getText().equals("DependsOn")){
            dependency = Dependency.DEPENDS_ON;
        }
        else {
            dependency = Dependency.REQUIRED_FOR;
        }
        if(validate()){
            warningLabel.setVisible(false);
            this.whatIfCallback.findWhatIf(sourceTarget, dependency);
        }
    }

    public void setWhatIfCallback(WhatIfCallback whatIfCallback) {
        this.whatIfCallback = whatIfCallback;
    }

    private boolean validate(){
        boolean isValidate = true;
        if(this.sourceTargetChoiceBox.getValue() == null){
            warningLabel.setVisible(true);
            warningLabel.setText("Please choose a Source target");
            isValidate = false;
        }

        return isValidate;
    }

    private void initialize(){
        dependsOnRadioButton.setUserData(Dependency.DEPENDS_ON.getDependency());
        requiredForRadioButton.setUserData(Dependency.REQUIRED_FOR.getDependency());
    }

    public void initChoiceBoxes(GraphDTO graphDTO){
        for(TargetDTO currTarget: graphDTO.getTargets().values()){
            this.sourceTargetChoiceBox.getItems().add(currTarget.getName());
        }

    }


}

