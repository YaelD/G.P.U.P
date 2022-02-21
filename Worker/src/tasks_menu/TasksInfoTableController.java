package tasks_menu;

import dto.TaskDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TasksInfoTableController {

    @FXML
    private TableView<TaskDTO> taskInfoTable;

    @FXML
    private TableColumn<TaskDTO, String> taskName_column;

    @FXML
    private TableColumn<TaskDTO, Integer> numOfWorkers_column;

    @FXML
    private TableColumn<TaskDTO, String> taskProgressColumn;

    @FXML
    private TableColumn<TaskDTO, Integer> myNumberOfTargets_column;

    @FXML
    private TableColumn<TaskDTO, Integer> totalTaskCredits_column;

}
