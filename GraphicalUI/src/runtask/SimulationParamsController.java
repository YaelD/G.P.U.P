package runtask;

import dto.SimulationTaskParamsDTO;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SimulationParamsController {

    private SimpleIntegerProperty processTime;
    private SimpleDoubleProperty successRate;
    private SimpleDoubleProperty successRateWithWarnings;
    private SimpleBooleanProperty isRandomProcessTime;
    private SimpleObjectProperty<SimulationTaskParamsDTO> simulationTaskParams;
    private ActiveTaskCallback activeTaskCallback;

    @FXML private TextField processTimeTextArea;
    @FXML private CheckBox isRandomCheckBox;
    @FXML private Label warningLabel;
    @FXML private Slider successRateSlider;
    @FXML private Label successRateLabel;
    @FXML private Slider successWithWarningSlider;
    @FXML private Label successWithWarningsLabel;
    @FXML private Button confirmButton;


    public SimulationParamsController() {
        this.successRate = new SimpleDoubleProperty(0);
        successRateWithWarnings = new SimpleDoubleProperty(0);
        this.processTime = new SimpleIntegerProperty(1000);
        this.isRandomProcessTime = new SimpleBooleanProperty(false);
        this.simulationTaskParams = new SimpleObjectProperty<>();
    }

    @FXML
    private void initialize() {
        this.isRandomProcessTime.bind(isRandomCheckBox.selectedProperty());
        this.successRateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                successRateLabel.textProperty().setValue(String.valueOf(newValue.intValue()) + "%");

            }
        });
        this.successWithWarningSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                successWithWarningsLabel.textProperty().setValue(String.valueOf(newValue.intValue()) + "%");
            }
        });

        this.successRate.bind(successRateSlider.valueProperty());
        this.successRateWithWarnings.bind(successWithWarningSlider.valueProperty());
        processTimeTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    processTimeTextArea.setText(newValue.replaceAll("[^\\d]", "0"));
                }
                else{
                    processTime.set(Integer.valueOf(newValue));
                }
            }
        });
    }

    public void bindParamsDTO(ObjectProperty<SimulationTaskParamsDTO> paramsDTOObjectProperty){
        paramsDTOObjectProperty.bind(this.simulationTaskParams);
    }

    public void setActiveTaskCallback(ActiveTaskCallback activeTaskCallback) {
        this.activeTaskCallback = activeTaskCallback;
    }

    @FXML
    void onConfirm(ActionEvent event) {
//        activeTaskCallback.activeTask(new SimulationTaskParamsDTO(this.processTime.intValue(),
//                this.isRandomProcessTime.getValue(), this.successRate.doubleValue(), this.successRateWithWarnings.doubleValue()));
        activeTaskCallback.activeTask(new SimulationTaskParamsDTO(3000,
                false, 0.5, 0.4));

    }

}

