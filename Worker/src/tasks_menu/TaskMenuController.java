package tasks_menu;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class TaskMenuController {

    private SimpleIntegerProperty totalNumOfThreads;

    private SimpleStringProperty userName;

    @FXML
    private Label numOfThreadsLabel;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private TableView<?> taskInfoTable;

    @FXML
    private TableView<?> targetInfoTable;


    public TaskMenuController() {
        this.userName = new SimpleStringProperty();
        this.totalNumOfThreads = new SimpleIntegerProperty();
    }

    public void setTotalNumOfThreads(SimpleIntegerProperty totalNumOfThreads) {
        this.totalNumOfThreads.bind(totalNumOfThreads);
    }

    public void setUserName(SimpleStringProperty userName) {
        this.userName.bind(userName);
    }
}
