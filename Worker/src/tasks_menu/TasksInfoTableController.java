package tasks_menu;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TasksInfoTableController {

    @FXML
    private TableView<?> taskInfoTable;

    @FXML
    private TableColumn<?, ?> taskName_column;

    @FXML
    private TableColumn<?, ?> numOfWorkers_column;

    @FXML
    private TableColumn<?, ?> taskProgressColumn;

    @FXML
    private TableColumn<?, ?> myNumberOfTargets_column;

    @FXML
    private TableColumn<?, ?> totalTaskCredits_column;

}
