package runtask.targets_and_task_info;

import engine.Engine;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import runtask.menu.RunTaskMenuController;
import task.RunType;
import task.TaskType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChooseTaskController {

    @FXML private Button continueButton;
    @FXML private ChoiceBox<Integer> numOfThreadsCB;
    @FXML private RadioButton simulationRadioButton;
    @FXML private ToggleGroup taskToggleGroup;
    @FXML private RadioButton compilationRadioButton;
    @FXML private RadioButton fromScratchRadioButton;
    @FXML private ToggleGroup runTypeToggle;
    @FXML private RadioButton incrementalRadioButton;
    @FXML private Label warningLabel;

    private GridPane simulationLayout;
    private GridPane compilationLayout;
    private Engine engine;
    private HBox menuPane;

    private SimpleListProperty<String> targetsList;

    private SimpleObjectProperty<TaskType> currTaskType;
    private SimpleObjectProperty<RunType> currRunType;



    public ChooseTaskController() {
        currTaskType = new SimpleObjectProperty<>(TaskType.SIMULATION_TASK);
        currRunType = new SimpleObjectProperty<>(RunType.FROM_SCRATCH);
        this.targetsList = new SimpleListProperty<>();
    }


    public void setTargetsList(SimpleListProperty<String> targetListProperty) {
        this.targetsList = targetListProperty;
    }

    public void setSimulationLayout(GridPane simulationLayout) {
        this.simulationLayout = simulationLayout;
    }

    public void setCompilationLayout(GridPane compilationLayout) {
        this.compilationLayout = compilationLayout;
    }

    @FXML
    void enableNextPanel(ActionEvent event) {

        warningLabel.setVisible(false);
        if(!validation()){
            //TODO: add a warning label
        }
        else if(simulationRadioButton.isSelected()){

            menuPane.getChildren().remove(compilationLayout);
            try{
                menuPane.getChildren().add(simulationLayout);
                simulationLayout.setMaxHeight(Double.MAX_VALUE);
                simulationLayout.setMaxWidth(Double.MAX_VALUE);
            }catch (IllegalArgumentException e){

            }
        }
        else{
            menuPane.getChildren().remove(simulationLayout);
            try{
                menuPane.getChildren().add(compilationLayout);
                compilationLayout.setMaxHeight(Double.MAX_VALUE);
                compilationLayout.setMaxWidth(Double.MAX_VALUE);
            }catch (IllegalArgumentException e){

            }

        }
    }

    private boolean validation() {
        Set<String> targetSet = new HashSet<>();
        targetSet.addAll(this.targetsList);
        if(this.currRunType.getValue().equals(RunType.INCREMENTAL) && !engine.isRunInIncrementalMode(this.currTaskType.getValue(),targetSet )){
            warningLabel.setVisible(true);
            warningLabel.setText("The " + currTaskType.getValue().getTaskType() + " task cannot run incrementally" +
                    "\nSetting run From Scratch by default");
            this.currRunType.setValue(RunType.FROM_SCRATCH);
            this.runTypeToggle.selectToggle(fromScratchRadioButton);
            return false;
        }
        if(targetSet.isEmpty()){
            warningLabel.setVisible(true);
            warningLabel.setText("Please choose targets");
            return false;
        }
        return true;
    }

    public void setTaskTypeAndRunTypeListeners(SimpleObjectProperty<TaskType> taskType, SimpleObjectProperty<RunType> runType){
        taskType.bind(currTaskType);
        runType.bind(currRunType);
    }

    @FXML
    private void initialize(){
        //RunTaskMenuController.initControllers(taskToggleGroup, simulationRadioButton, currTaskType, menuPane, compilationLayout, compilationRadioButton, simulationLayout, warningLabel, runTypeToggle, incrementalRadioButton, currRunType, fromScratchRadioButton);
    }



    public void setMenuPane(HBox menuPane) {
        this.menuPane = menuPane;
    }

    public void setEngine(Engine engine){
        this.engine = engine;
        for(int i=1; i<= this.engine.getMaxNumOfThreads(); i++){
            numOfThreadsCB.getItems().add(i);
        }

        this.numOfThreadsCB.getSelectionModel().select(0);
    }

    public void setNumOfThreads(SimpleIntegerProperty numOfThreads) {
        numOfThreads.bind(this.numOfThreadsCB.valueProperty());
    }
}

