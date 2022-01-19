package runtask;

import engine.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import javafx.scene.layout.GridPane;

public class ChooseTaskController {

    @FXML private Button continueButton;
    @FXML private ChoiceBox<Integer> numOfThreadsCB;
    @FXML private RadioButton simulationRadioButton;
    @FXML private ToggleGroup taskToggleGroup;
    @FXML private RadioButton compilationRadioButton;
    @FXML private RadioButton fromScratchRadioButton;
    @FXML private ToggleGroup runTypeToggle;
    @FXML private RadioButton incrementalRadioButton;

    private GridPane simulationLayout;
    private GridPane compilationLayout;
    private Engine engine;

    public void setSimulationLayout(GridPane simulationLayout) {
        this.simulationLayout = simulationLayout;
    }

    public void setCompilationLayout(GridPane compilationLayout) {
        this.compilationLayout = compilationLayout;
    }

    @FXML
    void enableNextPanel(ActionEvent event) {
        if(!validation()){
            //TODO: add a warning label
        }
        else if(simulationRadioButton.isSelected()){
            simulationLayout.setVisible(true);
        }
        else{
            compilationLayout.setVisible(true);
        }
    }

    private boolean validation() {
        return true;
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

