package runtask;

import dto.GraphDTO;
import dto.TargetDTO;
import engine.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RunTaskTogglesController {

    private Engine engine;


    @FXML
    private ChoiceBox<String> targetsCheckBox;

    @FXML
    private Button WhatIfButton;

    @FXML
    private RadioButton CompilationTaskRadioButton;

    @FXML
    private ToggleGroup taskToogleGroup;

    @FXML
    private RadioButton simulationTaskRadioButton;

    @FXML
    private Button ConfirmButton;

    @FXML
    private ListView<CheckBox> targetsCheckBoxList;

    @FXML
    void checkTargetsWithWhatIf(ActionEvent event) {

    }

    private void initialize(){

    }


    @FXML
    void confirm(ActionEvent event) {

    }

    public void setEngine(Engine engine) {
        this.engine = engine;
        GraphDTO graphDTO = engine.getGraphDTO();
        for(TargetDTO targetDTO: graphDTO.getTargets().values()){
            this.targetsCheckBox.getItems().add(targetDTO.getName());
            targetsCheckBoxList.getItems().add(new CheckBox(targetDTO.getName()));
        }

    }
}

