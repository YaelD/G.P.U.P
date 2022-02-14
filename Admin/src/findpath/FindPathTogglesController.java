package findpath;

//import dto.GraphDTO;
//import dto.TargetDTO;
//import graph.Dependency;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FindPathTogglesController {

    private findPathCallback findPathCallback;

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

    @FXML
    private Label warningLabel;

    @FXML
    private ToggleGroup dependencyToggleGroup;

//
//    private void initialize(){
//        dependsOnRadioButton.setUserData(Dependency.DEPENDS_ON.getDependency());
//        requiredForRadioButton.setUserData(Dependency.REQUIRED_FOR.getDependency());
////        dependsOnRadioButton.getProperties().put("dependency", Dependency.DEPENDS_ON);
////        requiredForRadioButton.getProperties().put("dependency", Dependency.REQUIRED_FOR);
//
//    }
//
//    public void initChoiceBoxes(GraphDTO graphDTO){
//        for(TargetDTO currTarget: graphDTO.getTargets().values()){
//            this.sourceTargetChoiceBox.getItems().add(currTarget.getName());
//            this.destinationTargetChoiceBox.getItems().add(currTarget.getName());
//        }
//        this.sourceTargetChoiceBox.getSelectionModel().select(0);
//        this.destinationTargetChoiceBox.getSelectionModel().select(1);
//
//
//    }
//
//    @FXML
//    void findPaths(ActionEvent event) {
//        String sourceTarget = sourceTargetChoiceBox.getValue();
//        String destinationTarget = destinationTargetChoiceBox.getValue();
//        RadioButton currSelected = (RadioButton) dependencyToggleGroup.getSelectedToggle();
//        Dependency dependency;
//        if(currSelected.getText().equals("DependsOn")){
//            dependency = Dependency.DEPENDS_ON;
//        }
//        else {
//            dependency = Dependency.REQUIRED_FOR;
//        }
//        if(validate()){
//            this.findPathCallback.findPaths(sourceTarget, destinationTarget, dependency);
//        }
//    }
//
//    public void setFindPathCallback(findPathCallback findPathCallback) {
//        this.findPathCallback = findPathCallback;
//    }
//
//    public Label getWarningLabel() {
//        return warningLabel;
//    }
//
//    private boolean validate(){
//        boolean isValidate = true;
//        if(this.sourceTargetChoiceBox.getValue() == null){
//            warningLabel.setVisible(true);
//            warningLabel.setText("Please choose a Source target");
//            isValidate = false;
//        }
//        if(this.destinationTargetChoiceBox.getValue() == null){
//            warningLabel.setVisible(true);
//            warningLabel.setText("Please choose a Destination target");
//            isValidate = false;
//        }
//        return isValidate;
//    }
}
