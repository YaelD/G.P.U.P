package runtask;

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



    public ChooseTargetsController() {

    }

    public void setTargetsList(SimpleListProperty<String> targetListProperty) {
        targetListProperty.bind(selectedTargetsListView.itemsProperty());
    }

    public void setEngine(Engine engine){
        this.engine= engine;
        GraphDTO graphDTO = this.engine.getGraphDTO();
        for(TargetDTO targetDTO: graphDTO.getTargets().values()){
            this.whatIf_targetsCB.getItems().add(targetDTO.getName());
            CheckBox checkBox = new CheckBox(targetDTO.getName());
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue == true){
                        selectedTargetsListView.getItems().add(checkBox.getText());
                    }
                    else{
                        selectedTargetsListView.getItems().remove(checkBox.getText());
                    }
                }
            });
            this.targetsCheckBoxList.getItems().add(checkBox);
        }
        whatIf_targetsCB.valueProperty().setValue(whatIf_targetsCB.getItems().get(0));

    }

    @FXML
    private void initialize(){
        whatIfSubMenu.disableProperty().bind(Bindings.not(chooseWhatIfRB.selectedProperty()));
        targetsSubMenu.disableProperty().bind(Bindings.not(chooseTargetsRB.selectedProperty()));
        chooseWhatIfRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                selectedTargetsListView.getItems().clear();
            }
        });
        chooseTargetsRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                selectedTargetsListView.getItems().clear();
                if(newValue == false){
                    for(CheckBox currCheckBox: targetsCheckBoxList.getItems()){
                        currCheckBox.selectedProperty().set(false);
                    }
                    chooseAllTargetsCheckBox.selectedProperty().set(false);
                }
            }
        });
        chooseAllTargetsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue.booleanValue() == true){
                    for(CheckBox checkBox: targetsCheckBoxList.getItems()){
                        checkBox.setSelected(newValue);
                    }
                }
                else{
                    for(CheckBox checkBox: targetsCheckBoxList.getItems()){
                        checkBox.setSelected(false);
                    }
                }
            }
        });
        whatIf_DependencyCB.getItems().addAll(Dependency.DEPENDS_ON, Dependency.REQUIRED_FOR);
        whatIf_DependencyCB.setValue(Dependency.DEPENDS_ON);

    }


    @FXML
    void checkTargetsWithWhatIf(ActionEvent event) {

        Set<String> whatIfTargets = this.engine.whatIf(whatIf_targetsCB.getValue(),whatIf_DependencyCB.getValue());
        selectedTargetsListView.getItems().clear();
        selectedTargetsListView.getItems().add(whatIf_targetsCB.getValue());
        selectedTargetsListView.getItems().addAll(whatIfTargets);

    }


}
