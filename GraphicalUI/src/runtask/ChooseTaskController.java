package runtask;

import engine.Engine;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import task.RunType;
import task.TaskType;

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

    private SimpleObjectProperty<TaskType> currTaskType;
    private SimpleObjectProperty<RunType> currRunType;


    public ChooseTaskController() {
        currTaskType = new SimpleObjectProperty<>(TaskType.SIMULATION_TASK);
        currRunType = new SimpleObjectProperty<>(RunType.FROM_SCRATCH);
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
        // TODO: send set of targets!
//        if(this.currRunType.getValue().equals(RunType.INCREMENTAL) && !engine.isRunInIncrementalMode(this.currTaskType.getValue(), )){
//            warningLabel.setVisible(true);
//            warningLabel.setText("The " + currTaskType.getValue().getTaskType() + " task cannot run incrementally" +
//                    "\nSetting run From Scratch by default");
//            //this.currRunType.setValue(RunType.FROM_SCRATCH);
//            this.runTypeToggle.selectToggle(fromScratchRadioButton);
//        }

        return true;
    }

    public void setTaskTypeAndRunTypeListeners(SimpleObjectProperty<TaskType> taskType, SimpleObjectProperty<RunType> runType){
        taskType.bind(currTaskType);
        runType.bind(currRunType);
    }

    @FXML
    private void initialize(){
        taskToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == simulationRadioButton){
                    currTaskType.set(TaskType.SIMULATION_TASK);
                    menuPane.getChildren().remove(compilationLayout);
                }
                else if(newValue == compilationRadioButton){
                    currTaskType.set(TaskType.COMPILATION_TASK);
                    menuPane.getChildren().remove(simulationLayout);
                }
                warningLabel.setVisible(false);

            }
        });
        runTypeToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == incrementalRadioButton){
                    currRunType.set(RunType.INCREMENTAL);
                    menuPane.getChildren().remove(simulationLayout);
                    menuPane.getChildren().remove(compilationLayout);
                }
                else if(newValue == fromScratchRadioButton){
                    currRunType.set(RunType.FROM_SCRATCH);
                    menuPane.getChildren().remove(simulationLayout);
                    menuPane.getChildren().remove(compilationLayout);
                }
                //warningLabel.setVisible(false);
            }
        });
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

