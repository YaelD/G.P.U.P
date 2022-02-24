package execution_details;

import dto.TaskDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TaskDetailsPaneController {

    private TaskDTO taskDTO;

    @FXML
    private Label taskNameLabel;

    @FXML
    private Label graphNameLabel;

    @FXML
    private Label totalTargetsLabel;

    @FXML
    private Label numOfRootsLabel;

    @FXML
    private Label numOfMiddlesLabel;

    @FXML
    private Label numOfLeavesLabel;

    @FXML
    private Label numOfIndependentsLabel;

    @FXML
    private Label numOfWorkersLabel;

    @FXML
    private Label numOfTargetsInQueueLabel;

    public void setTaskDTO(TaskDTO taskDTO) {
        Platform.runLater(()->{
            this.taskDTO = taskDTO;
            this.taskNameLabel.setText(taskDTO.getTaskName());
            this.graphNameLabel.setText(taskDTO.getGraphDTO().getName());
            this.totalTargetsLabel.setText(String.valueOf(taskDTO.getGraphDTO().getTotalNumOfTargets()));
            this.numOfRootsLabel.setText(String.valueOf(taskDTO.getGraphDTO().getNumOfRoots()));
            this.numOfMiddlesLabel.setText(String.valueOf(taskDTO.getGraphDTO().getNumOfMiddles()));
            this.numOfLeavesLabel.setText(String.valueOf(taskDTO.getGraphDTO().getNumOfLeaves()));
            this.numOfIndependentsLabel.setText(String.valueOf(taskDTO.getGraphDTO().getNumOfIndependents()));
            this.numOfWorkersLabel.setText(String.valueOf(taskDTO.getNumOfRegisteredWorkers()));
            this.numOfTargetsInQueueLabel.setText(String.valueOf(taskDTO.getNumOfTargetsInQueue()));
        });

    }
}
