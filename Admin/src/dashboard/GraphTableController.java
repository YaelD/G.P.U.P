package dashboard;

import RefreshingItems.GraphListRefresher;
import dto.GraphDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GraphTableController {

    private Timer timer;
    private TimerTask listRefresher;


    private DashboardController dashboardController;

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

    @FXML
    private void initialize(){
        loadGraphTableColumns();
        startGraphListRefresher();
    }


    //TODO: function that calls the sever and get all the graphs

    private void loadGraphTableColumns() {
        graphInSystemTableView.setRowFactory(tv-> {
            TableRow<GraphDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    GraphDTO rowData = row.getItem();
                    System.out.println("Clickedddd");
                    dashboardController.loadGraphInfo(rowData);
                }
            });
            return row;
        });
        graph_graphNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        graph_uploaderNameColumn.setCellValueFactory(new PropertyValueFactory<>("creatorName"));
        graph_totalNumOfTargetsColumn.setCellValueFactory(new PropertyValueFactory<>("totalNumOfTargets"));
        graph_numOfRootsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfRoots"));
        graph_numOfIndependentsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfIndependents"));
        graph_numOfMiddlesColumn.setCellValueFactory(new PropertyValueFactory<>("numOfMiddles"));
        graph_numOfLeavesColumn.setCellValueFactory(new PropertyValueFactory<>("numOfLeaves"));
        graph_simulationPriceColumn.setCellValueFactory(new PropertyValueFactory<>("priceOfSimulationTask"));
        graph_CompilationPriceColumn.setCellValueFactory(new PropertyValueFactory<>("priceOfCompilationTask"));
    }

    private void updateGraphsList(List<GraphDTO> graphs) {
        Platform.runLater(() -> {
            ObservableList<GraphDTO> graphDTOS = graphInSystemTableView.getItems();
            graphDTOS.clear();
            graphDTOS.addAll(graphs);
        });
    }

    public void startGraphListRefresher() {
        listRefresher = new GraphListRefresher(
                this::updateGraphsList);
        timer = new Timer(true);
        timer.schedule(listRefresher, 15000, 15000);
    }


    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}
