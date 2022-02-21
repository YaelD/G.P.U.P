package dashboard;

import dto.GraphDTO;
import dto.TaskDTO;
import dto.UserDTO;
import header.HeaderController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kotlin.jvm.functions.Function2;
import loadfile.LoadFileController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class DashboardController {

    private String userName;

    private TabPane topTabPane;

    @FXML
    private TableView<GraphDTO> graphsTable;

    @FXML
    private GraphTableController graphsTableController;


    @FXML
    private TableView<GraphDTO> tasksTable;

    @FXML
    private TaskTableController tasksTableController;

    @FXML
    private GridPane usersList;

    @FXML
    private UsersListController usersListController;

    @FXML
    private Button taskDetailsButton;

    @FXML
    private TableView<?> tasksInSystemTableView;

    @FXML
    private Button CreateNewTaskButton;


    public DashboardController() {

    }
    @FXML
    private void initialize(){

        graphsTableController.setDashboardController(this);
        tasksTableController.setDashboardController(this);
    }




    @FXML
    void onClickTaskDetails(ActionEvent event) {

    }


    public void setUserName(String userName) {
        this.userName = userName;

    }

    public void loadGraphInfo(GraphDTO graph){
        try {
            URL resource = HeaderController.class.getResource("head_and_missing_body.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(resource);
            Parent root = fxmlLoader.load(resource.openStream());
            Scene scene = new Scene(root,1000, 1000);
            Stage secondaryStage = new Stage();
            HeaderController headerController = fxmlLoader.getController();
            headerController.setPrimaryStage(secondaryStage);
            headerController.setGraphDTO(graph);
            headerController.setUserName(this.userName);
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
            Scene scene = new Scene(root);
            Stage secondaryStage = new Stage();
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Load file");
            secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setTopTabPane(TabPane topTabPane) {
        this.topTabPane = topTabPane;
    }

    public void addTab(String tabName, Node newNode){
        for(Tab tab: topTabPane.getTabs()){
            if(tab.getText().equals(tabName)){
                topTabPane.getSelectionModel().select(tab);
                return;
            }
        }
        topTabPane.getTabs().add(new Tab(tabName, newNode));
    }

    public String getUserName() {
        return userName;
    }
}
