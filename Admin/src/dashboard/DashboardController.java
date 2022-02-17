package dashboard;

import RefreshingItems.GraphListRefresher;
import RefreshingItems.UserListRefresher;
import dto.GraphDTO;
import dto.UserDTO;
import header.HeaderController;
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
import javafx.scene.control.TableRow;
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
    private TableView<UserDTO> activeUsersTableView;

    @FXML
    private TableColumn<UserDTO, String> users_name_column;

    @FXML
    private TableColumn<UserDTO, String> rule_users_column;

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
        startGraphListRefresher();
    }


    private void loadGraphTable(List<GraphDTO> graphs){
        final ObservableList<GraphDTO> data = FXCollections.observableArrayList(graphs);
        this.graphInSystemTableView.setItems(data);
    }

    //TODO: function that calls the sever and get all the graphs

    private void loadGraphTableColumns() {
        graphInSystemTableView.setRowFactory(tv-> {
            TableRow<GraphDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    GraphDTO rowData = row.getItem();
                    System.out.println("Clickedddd");
                    loadGraphInfo(rowData);
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




    @FXML
    void onClickTaskDetails(ActionEvent event) {

    }



    private void loadGraphInfo(GraphDTO graph){
        try {
            URL resource = HeaderController.class.getResource("head_and_missing_body.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(resource);
            Parent root = fxmlLoader.load(resource.openStream());
            Scene scene = new Scene(root, 600, 600);
            Stage secondaryStage = new Stage();
            HeaderController headerController = fxmlLoader.getController();
            headerController.setPrimaryStage(secondaryStage);
            headerController.setGraphDTO(graph);
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Load file");
            secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void onUploadGraphClick(ActionEvent event) {
        try {
            URL resource = LoadFileController.class.getResource("load_file.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(resource);
            Parent root = fxmlLoader.load(resource.openStream());
            Scene scene = new Scene(root, 400, 400);
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
            ObservableList<GraphDTO> graphDTOS = graphInSystemTableView.getItems();
            graphDTOS.clear();
            if(!graphs.isEmpty()){
                boolean a = true;
            }
            graphDTOS.addAll(graphs);
        });
    }

    public void startGraphListRefresher() {
        listRefresher = new GraphListRefresher(
                this::updateGraphsList);
        timer = new Timer();
        timer.schedule(listRefresher, 15000, 15000);
    }



}
