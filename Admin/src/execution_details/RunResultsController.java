package execution_details;

import dto.TargetDTO;
import dto.TaskDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class RunResultsController {


    @FXML
    private GridPane runResultsPane;

    @FXML
    private Label numFinishedSuccessLabel;

    @FXML
    private Label numFinishedWarningsLabel;

    @FXML
    private Label numSkippedLabel;

    @FXML
    private Label numFailureLabel;


    public void setTaskDTO(TaskDTO taskDTO) {
        int numFinishedSuccess = 0, numFinishedWarning = 0, numSkipped = 0, numFailure = 0;
        for (TargetDTO targetDTO : taskDTO.getGraphDTO().getTargets().values()) {
            switch (targetDTO.getRunResult()) {
                case FAILURE:
                    numFailure++;
                    break;
                case SKIPPED:
                    numSkipped++;
                    break;
                case SUCCESS:
                    numFinishedSuccess++;
                    break;
                case WARNING:
                    numFinishedWarning++;
                    break;
            }
        }
        showRunResultsDetails(numFailure, numSkipped, numFinishedSuccess, numFinishedWarning);
    }

    private void showRunResultsDetails(int numFailure, int numSkipped, int numFinishedSuccess, int numFinishedWarning) {
        Platform.runLater(()->{
            this.numFailureLabel.setText(String.valueOf(numFailure));
            this.numSkippedLabel.setText(String.valueOf(numSkipped));
            this.numFinishedSuccessLabel.setText(String.valueOf(numFinishedSuccess));
            this.numFinishedWarningsLabel.setText(String.valueOf(numFinishedWarning));
        });
    }
}
