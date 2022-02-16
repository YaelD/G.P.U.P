package dashboard;

import dto.GraphDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class GraphTableController {

    @FXML
    private TableView<GraphDTO> graphInSystemTableView;

    @FXML
    private TableColumn<GraphDTO, String> graph_graphNameColumn;

    @FXML
    private TableColumn<GraphDTO, String> graph_uploaderNameColumn;

    @FXML
    private TableColumn<GraphDTO, Integer> graph_totalNumOfTargetsColumn;

    @FXML
    private TableColumn<GraphDTO, Integer> graph_numOfLeavesColumn;

    @FXML
    private TableColumn<GraphDTO, Integer> graph_numOfMiddlesColumn;

    @FXML
    private TableColumn<GraphDTO, Integer> graph_numOfRootsColumn;

    @FXML
    private TableColumn<GraphDTO, Integer> graph_numOfIndependentsColumn;

    @FXML
    private TableColumn<GraphDTO, Integer> graph_simulationPriceColumn;

    @FXML
    private TableColumn<GraphDTO, Integer> graph_CompilationPriceColumn;

    public GraphTableController() {
    }


}

