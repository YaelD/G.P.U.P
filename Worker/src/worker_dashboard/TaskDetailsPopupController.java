package worker_dashboard;

import dto.TaskDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class TaskDetailsPopupController {

    private TaskDTO taskDTO;

    @FXML
    private TextArea taskDetailsTextArea;

    @FXML
    private Button signin_btn;

    @FXML
    private Button startwork_btn;




    @FXML
    void onStartWork(ActionEvent event) {

    }

    @FXML
    void onTaskSighIn(ActionEvent event) {


    }

    public void setTaskDTO(TaskDTO taskDTO) {
        this.taskDTO = taskDTO;
    }
}

