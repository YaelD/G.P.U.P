package runtask.targets_and_task_info;

import dto.GraphDTO;
import dto.TargetDTO;
import engine.Engine;
import graph.Dependency;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import runtask.menu.RunTaskMenuController;

import java.util.Set;

public class ChooseTargetsController {

    private Engine engine;

    @FXML private VBox targetsSubMenu;
    @FXML private CheckBox chooseAllTargetsCheckBox;
    @FXML private ListView<CheckBox> targetsCheckBoxList;
    @FXML private RadioButton chooseTargetsRB;
    @FXML private ToggleGroup chooseTargetsToggleGroup;
    @FXML private RadioButton chooseWhatIfRB;
    @FXML private VBox whatIfSubMenu;
    @FXML private ChoiceBox<String> whatIf_targetsCB;
    @FXML private ChoiceBox<Dependency> whatIf_DependencyCB;
    @FXML private Button WhatIfButton;
    @FXML private ListView<String> selectedTargetsListView;

    @FXML
    void clearAllTargets(ActionEvent event) {
        for(CheckBox checkBox: targetsCheckBoxList.getItems()){
            checkBox.setSelected(false);
        }
        selectedTargetsListView.getItems().clear();
        chooseAllTargetsCheckBox.setSelected(false);
    }


    public ChooseTargetsController() {

    }




    public void setTargetsList(SimpleListProperty<String> targetListProperty) {
        targetListProperty.bind(selectedTargetsListView.itemsProperty());
    }

    public void setEngine(Engine engine){
        this.engine= engine;
        GraphDTO graphDTO = this.engine.getGraphDTO();
        whatIf_targetsCB.valueProperty().setValue(whatIf_targetsCB.getItems().get(0));

    }

    @FXML
    private void initialize(){
        //RunTaskMenuController.initTargetChoiceControllers(whatIfSubMenu, chooseWhatIfRB, targetsSubMenu, chooseTargetsRB, selectedTargetsListView, targetsCheckBoxList, chooseAllTargetsCheckBox, whatIf_DependencyCB);
        whatIf_DependencyCB.setValue(Dependency.DEPENDS_ON);

    }




    @FXML
    void checkTargetsWithWhatIf(ActionEvent event) {

//        Set<String> whatIfTargets = this.engine.whatIf(whatIf_targetsCB.getValue(),
//                whatIf_DependencyCB.getValue(), );
        selectedTargetsListView.getItems().clear();
        selectedTargetsListView.getItems().add(whatIf_targetsCB.getValue());
        //selectedTargetsListView.getItems().addAll(whatIfTargets);

    }


}
