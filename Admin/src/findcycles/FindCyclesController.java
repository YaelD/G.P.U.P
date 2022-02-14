package findcycles;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class FindCyclesController {
//
//    private Engine engine;
//
//    @FXML
//    private ChoiceBox<String> targetsChoiceBox;
//
//    @FXML
//    private Button searchButton;
//
//    @FXML
//    private ListView<String> cyclesListView;
//
//    @FXML
//    private Label warningLabel;
//
//    @FXML
//    void searchCycles(ActionEvent event) {
//        if(validate()){
//            String targetName = this.targetsChoiceBox.getValue();
//            try {
//                warningLabel.setVisible(false);
//                cyclesListView.getItems().clear();
//                List<String> cycle = null;
//                //List<String> cycle = this.engine.findCycle(targetName);
//                if(cycle == null){
//                    cyclesListView.setPlaceholder(new Label("This target does not take place in any cycle"));
//                }
//                else{
//                    ObservableList<String> data = FXCollections.observableArrayList();
//                    data.addAll(cycle);
//                    cyclesListView.setItems(data);
//                }
//            } catch (TargetNotExistException ignored) {
//            }
//        }
//    }
//
//    private boolean validate(){
//        boolean isValidate = true;
//        if(this.targetsChoiceBox.getValue() == null){
//            warningLabel.setVisible(true);
//            warningLabel.setText("Please choose a target");
//            isValidate = false;
//        }
//        return isValidate;
//    }
//
//    private void initTargetsChoiceBox(){
//        GraphDTO graphDTO = this.engine.getGraphDTO();
//        for(TargetDTO targetDTO: this.engine.getGraphDTO().getTargets().values()){
//            this.targetsChoiceBox.getItems().add(targetDTO.getName());
//        }
//    }
//
//    public void setEngine(Engine engine) {
//        this.engine = engine;
//        initTargetsChoiceBox();
//    }
}

