package dashboard;

import dto.GraphDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import loadfile.LoadFileController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardController {

    private Timer timer;
    private TimerTask listRefresher;

    @FXML
    private TableView<?> activeUsersTableView;

    @FXML
    private Button taskDetailsButton;

    @FXML
    private TableView<?> tasksInSystemTableView;

    @FXML
    private Button CreateNewTaskButton;

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




    public DashboardController() {

    }
    @FXML
    private void initialize(){
        loadGraphTableColumns();
        startListRefresher();
    }



    //TODO: function that calls the sever and get all the graphs

    private void loadGraphTableColumns() {
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


    @FXML
    void onClickTaskDetails(ActionEvent event) {

    }

    @FXML
    void onCreateNewTask(ActionEvent event) {

    }

    @FXML
    void onGraphInfoClick(ActionEvent event) {

    }

    @FXML
    void onUploadGraphClick(ActionEvent event) {
        try {
            URL resource = LoadFileController.class.getResource("load_file.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(resource);
            Parent root = fxmlLoader.load(resource.openStream());
            Scene scene = new Scene(root, 600,600);
            Stage secondaryStage = new Stage();
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Load file");
            secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateGraphsList(List<GraphDTO> graphs) {
        Platform.runLater(() -> {

            ObservableList<GraphDTO> graphDTOS = FXCollections.observableArrayList(graphs);
            this.graphInSystemTableView.setItems(graphDTOS);
//            graphDTOS.clear();
//            graphDTOS.addAll(graphs);
        });
    }

    public void startListRefresher() {
        listRefresher = new GraphListRefresher(
                this::updateGraphsList);
        timer = new Timer();
        timer.schedule(listRefresher, 2, 20);
    }

}
