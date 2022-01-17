package runtask;

import dto.GraphDTO;
import dto.TargetDTO;
import engine.Engine;
import graph.Dependency;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import task.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RunTaskTogglesController {

    private Engine engine;

    @FXML
    private ChoiceBox<String> targetsChoiceBox;

    @FXML
    private Button WhatIfButton;

    @FXML
    private RadioButton CompilationTaskRadioButton;

    @FXML
    private ToggleGroup taskToogleGroup;

    @FXML
    private Button ConfirmButton;

    @FXML
    private ListView<CheckBox> targetsCheckBoxList;

    @FXML
    private RadioButton whatif_requiredForRB;

    @FXML
    private ToggleGroup whatIfToggleGroup;

    @FXML
    private RadioButton simulationTaskRadioButton;

    @FXML
    private RadioButton whatif_DependsOnRB;

    private chooseTaskCallback chooseTaskCallback;


    @FXML
    void checkTargetsWithWhatIf(ActionEvent event) {
        String targetName = targetsChoiceBox.getValue();
        Dependency dependency;
        RadioButton choosenButton = (RadioButton) this.whatIfToggleGroup.getSelectedToggle();
        if(choosenButton.getText().equals("requiredFor")){
            dependency = Dependency.REQUIRED_FOR;
        }
        else{
            dependency = Dependency.DEPENDS_ON;
        }
        for(CheckBox checkBox: targetsCheckBoxList.getItems()){
            checkBox.setSelected(false);
        }
        Set<String> targetsList = engine.whatIf(targetName, dependency);
        for(CheckBox checkBox: targetsCheckBoxList.getItems()){
            if(targetsList.contains(checkBox.getText())){
                checkBox.setSelected(true);
            }
        }

    }

    private void initialize(){

    }


    @FXML
    void confirm(ActionEvent event) {
        List<String> targetsList = new ArrayList<>();
        TaskType taskType;
        for(CheckBox currCheckBox:targetsCheckBoxList.getItems() ) {
            if(currCheckBox.isSelected()){
                targetsList.add(currCheckBox.getText());
            }
        }
        if(this.simulationTaskRadioButton.isSelected()){
            taskType = TaskType.SIMULATION_TASK;
        }
        else{
            taskType = TaskType.COMPILATION_TASK;
        }

        this.chooseTaskCallback.loadSpecificTaskToggles(targetsList, taskType);

    }

    public void setEngine(Engine engine) {
        this.engine = engine;
        GraphDTO graphDTO = engine.getGraphDTO();
        for(TargetDTO targetDTO: graphDTO.getTargets().values()){
            this.targetsChoiceBox.getItems().add(targetDTO.getName());
            targetsCheckBoxList.getItems().add(new CheckBox(targetDTO.getName()));
        }
        targetsChoiceBox.getSelectionModel().select(0);
    }

    public void setTaskRunMenuCallback(chooseTaskCallback chooseTaskCallback) {
        this.chooseTaskCallback = chooseTaskCallback;
    }
}

