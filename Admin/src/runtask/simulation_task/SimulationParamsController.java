package runtask.simulation_task;

//import dto.SimulationTaskParamsDTO;
import dto.SimulationTaskParamsDTO;
import general_enums.RunType;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import runtask.menu.ActiveTaskCallback;
import runtask.menu.ReturnCallback;
import runtask.menu.SimulationParamsCallBack;

public class SimulationParamsController {

    private SimpleIntegerProperty processTime;
    private SimpleDoubleProperty successRate;
    private SimpleDoubleProperty successRateWithWarnings;
    private SimpleBooleanProperty isRandomProcessTime;
//    private SimpleObjectProperty<SimulationTaskParamsDTO> simulationTaskParams;
//    private ActiveTaskCallback activeTaskCallback;

    private SimulationParamsCallBack taskCallBack;

    private ReturnCallback returnCallBack;




    @FXML private TextField processTimeTextArea;
    @FXML private CheckBox isRandomCheckBox;
    @FXML private Label warningLabel;
    @FXML private Slider successRateSlider;
    @FXML private Label successRateLabel;
    @FXML private Slider successWithWarningSlider;
    @FXML private Label successWithWarningsLabel;
    @FXML private Button confirmButton;

    @FXML
    void returnToPrevWindow(ActionEvent event) {
        returnCallBack.returnToPrev();
    }


    public void setTaskCallBack(SimulationParamsCallBack taskCallBack) {
        this.taskCallBack = taskCallBack;
    }

    public void setReturnCallBack(ReturnCallback returnCallBack) {
        this.returnCallBack = returnCallBack;
    }

    public SimulationParamsController() {
        this.successRate = new SimpleDoubleProperty(0.0);
        this.successRateWithWarnings = new SimpleDoubleProperty(0.0);
        this.processTime = new SimpleIntegerProperty(1000);
        this.isRandomProcessTime = new SimpleBooleanProperty(false);

//        this.simulationTaskParams = new SimpleObjectProperty<>();
    }

    @FXML
    private void initialize() {
        this.isRandomProcessTime.bind(isRandomCheckBox.selectedProperty());
        this.successRateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                successRateLabel.textProperty().setValue(String.valueOf(newValue.intValue()) + "%");
                successRate.setValue(newValue.floatValue()/100);
            }
        });
        this.successWithWarningSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                successWithWarningsLabel.textProperty().setValue(String.valueOf(newValue.intValue()) + "%");
                successRateWithWarnings.set(newValue.floatValue()/100);

            }
        });
        processTimeTextArea.setText("1000");
        processTimeTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    processTimeTextArea.setText(newValue.replaceAll("[^\\d]", "0"));
                    processTimeTextArea.setText("1000");
                }
                else if(newValue.equals("")){
//                    processTime.set(1000);
//                    processTimeTextArea.setText("1000");
                }
                else{
                    processTime.set(Integer.valueOf(newValue));
                }
            }
        });
    }

//    public void setActiveTaskCallback(ActiveTaskCallback activeTaskCallback) {
//        this.activeTaskCallback = activeTaskCallback;
//    }

    @FXML
    void onConfirm(ActionEvent event) {

        this.taskCallBack.sendSimulationTaskParams(this.processTime.get(), this.isRandomProcessTime.get(), this.successRate.get(), this.successRateWithWarnings.get());
    }


}

