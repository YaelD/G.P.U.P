package dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class TaskTableController {

    @FXML
    private TableColumn<?, ?> taskname_column;

    @FXML
    private TableColumn<?, ?> creator_name_column;

    @FXML
    private TableColumn<?, ?> graphName_column;

    @FXML
    private TableColumn<?, ?> totalNumOfTargets_column;

    @FXML
    private TableColumn<?, ?> numOfLeaves_column;

    @FXML
    private TableColumn<?, ?> numOfMiddles_column;

    @FXML
    private TableColumn<?, ?> numOfRoots_column;

    @FXML
    private TableColumn<?, ?> numOfIndepepndens_column;

    @FXML
    private TableColumn<?, ?> totalTaskPrice_column;

    @FXML
    private TableColumn<?, ?> numberOfWorkers_column;



}
