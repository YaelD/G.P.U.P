package header;

import engine.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loadfile.*;
import tables.*;

import java.io.IOException;
import java.net.URL;

public class HeaderController {

    private Engine engine;

    private Stage primaryStage;

    @FXML
    private BorderPane base_BorderPane;

    @FXML
    private Button graphInfo_btn;

    @FXML
    private Button findPath_btn;

    @FXML
    private Button findCycle_btn;

    @FXML
    private Button whatIf_btn;

    @FXML
    private Button runTask_btn;

    @FXML
    private Button uploadFile_btn;

    @FXML
    private void loadFileLoader(ActionEvent event) {
        URL resource =  LoadFileController.class.getResource("load_file.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            Parent root = fxmlLoader.load(resource.openStream());
            LoadFileController loadFileController = fxmlLoader.getController();
            loadFileController.setEngine(this.engine);
            loadFileController.setHeaderController(this);
            base_BorderPane.setCenter(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enableButtons(){
        this.graphInfo_btn.setDisable(false);
        this.findCycle_btn.setDisable(false);
        this.findPath_btn.setDisable(false);
        this.runTask_btn.setDisable(false);
        this.whatIf_btn.setDisable(false);
    }


    @FXML
    private void loadFindCycle(ActionEvent event) {

    }

    @FXML
    private void loadFindPath(ActionEvent event) {

    }

    @FXML
    private void loadGraphInfo(ActionEvent event) {
        URL resource = GraphInfoController.class.getResource("graph_info.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            Parent root = fxmlLoader.load(resource.openStream());
            GraphInfoController graphInfoController = fxmlLoader.getController();
            graphInfoController.setGraphInfo(this.engine.getGraphDTO());
            base_BorderPane.setCenter(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void loadTaskRun(ActionEvent event) {

    }

    @FXML
    private void loadWhatIf(ActionEvent event) {

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
