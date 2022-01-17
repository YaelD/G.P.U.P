package runtask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SimulationTaskParamToggles {

    @FXML
    private TextField processTimeTextArea;

    @FXML
    private CheckBox isRandomCheckBox;

    @FXML
    private TextField successRateTextArea;

    @FXML
    private TextField successWithWaringsRateTextArea;

    @FXML
    private Button confirmButton;

    @FXML
    private Label warningLabel;


    private void initialize(){
        processTimeTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    processTimeTextArea.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        Pattern pattern = Pattern.compile("\\d*|\\d+\\,\\d*");
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        successRateTextArea.setTextFormatter(formatter);
        successWithWaringsRateTextArea.setTextFormatter(formatter);
    }





    @FXML
    void onConfirm(ActionEvent event) {
        double successRate = Double.parseDouble(successRateTextArea.getText());
        double successWithWarningRate = Double.parseDouble(successRateTextArea.getText());
        if(successRate > 1 || successRate < 0){
            warningLabel.setVisible(true);
            warningLabel.setText("The success rate should be between 0 to 1");
        }
        else if(successWithWarningRate > 1 || successWithWarningRate < 0){
            warningLabel.setVisible(true);
            warningLabel.setText("The success with warning rate should be between 0 to 1");
        }
       //Todo: send params



    }

}
