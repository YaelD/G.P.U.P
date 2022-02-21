package tasks_menu;

import dto.TargetDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TargetsInfoTableController {

    @FXML
    private TableView<TargetDTO> targetTable;

    @FXML
    private TableColumn<TargetDTO, String> targetName_column;

    @FXML
    private TableColumn<TargetDTO, String> taskName_column;

    @FXML
    private TableColumn<TargetDTO, String> taskType_column;

    @FXML
    private TableColumn<TargetDTO, String> runStatus_Column;

    @FXML
    private TableColumn<TargetDTO, Integer> price_column;

    @FXML
    private TableColumn<TargetDTO, Button> logs_column;

}
