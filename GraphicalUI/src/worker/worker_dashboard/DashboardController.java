package worker.worker_dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class DashboardController {

    @FXML
    private TableView<?> activeUsersTableView;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private TableView<?> taskInfoTable;

    @FXML
    private TextArea taskDetailsTextArea;

    @FXML
    void onStartWork(ActionEvent event) {

    }

    @FXML
    void onTaskDetailsClick(ActionEvent event) {

    }

    @FXML
    void onTaskSighIn(ActionEvent event) {

    }

}

