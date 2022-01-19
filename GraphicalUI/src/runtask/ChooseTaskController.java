package runtask;

import engine.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleGroup;

public class ChooseTaskController {

    @FXML private Button continueButton;
    @FXML private ChoiceBox<Integer> numOfThreadsCB;
    @FXML private ToggleGroup taskToggleGroup;
    @FXML private ToggleGroup runTypeToggle;
    private Engine engine;

    @FXML
    void enableNextPanel(ActionEvent event) {
        if(!checkValidation()){
            //TODO:show warning label
        }
        else if(taskToggleGroup.getSelectedToggle().)
    }

    @FXML
    private void initialize(){

    }

    public void setEngine(Engine engine){
        this.engine = engine;
        for(int i=1; i<= this.engine.getMaxNumOfThreads(); i++){
            numOfThreadsCB.getItems().add(i);
        }
    }

}

