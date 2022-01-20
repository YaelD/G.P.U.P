package runtask;

import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SimulationParamsController {

    private SimpleIntegerProperty proccessTime;
    private SimpleDoubleProperty successRate;
    private SimpleDoubleProperty successRateWithWarnings;

    @FXML private TextField processTimeTextArea;
    @FXML private CheckBox isRandomCheckBox;
    @FXML private Label warningLabel;
    @FXML private Slider successRateSlider;
    @FXML private Label successRateLabel;
    @FXML private Slider successWithWarningSlider;
    @FXML private Label successWithWarningsLabel;
    @FXML private Button confirmButton;

    Engine engine;

    public SimulationParamsController() {
        this.successRate = new SimpleDoubleProperty(0);
        successRateWithWarnings = new SimpleDoubleProperty(0);
        this.proccessTime = new SimpleIntegerProperty(1000);
    }

    @FXML
    private void initialize() {
        this.successRateLabel.textProperty().bind(Bindings.concat(successRateSlider.valueProperty() + "%"));
        this.successWithWarningsLabel.textProperty().bind(Bindings.concat(successWithWarningSlider.valueProperty() + "%"));
        this.successRate.bind(successRateSlider.valueProperty());
        this.successRateWithWarnings.bind(successWithWarningSlider.valueProperty());

        processTimeTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    processTimeTextArea.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    void onConfirm(ActionEvent event) {
    }

}

