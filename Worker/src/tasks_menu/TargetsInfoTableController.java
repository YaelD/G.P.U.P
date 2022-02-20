package tasks_menu;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class TargetsInfoTableController {

    @FXML
    private TableColumn<?, ?> targetName_column;

    @FXML
    private TableColumn<?, ?> taskName_column;

    @FXML
    private TableColumn<?, ?> taskType_column;

    @FXML
    private TableColumn<?, ?> runStatus_Column;

    @FXML
    private TableColumn<?, ?> price_column;

    @FXML
    private TableColumn<?, ?> logs_column;

}
