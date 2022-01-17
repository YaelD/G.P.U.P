package header;

import engine.Engine;
import findcycles.FindCyclesController;
import findpath.FindPathsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loadfile.*;
import runtask.RunTaskTogglesController;
import tables.*;
import whatif.WhatIfMenuController;

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
        URL resource = FindCyclesController.class.getResource("searchCyclesMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            Parent root = fxmlLoader.load(resource.openStream());
            FindCyclesController findCyclesController = fxmlLoader.getController();
            findCyclesController.setEngine(this.engine);
            base_BorderPane.setCenter(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void loadFindPath(ActionEvent event) {
        URL resource = FindPathsController.class.getResource("find_paths_menu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            Parent root = fxmlLoader.load(resource.openStream());
            FindPathsController findPathsController = fxmlLoader.getController();
            findPathsController.setEngine(this.engine);
            base_BorderPane.setCenter(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            graphInfoController.setSerialSets(this.engine.getSerialSetsInfo());
            base_BorderPane.setCenter(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void loadTaskRun(ActionEvent event) {
        URL resource = RunTaskTogglesController.class.getResource("run_task_toggles.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            Parent root = fxmlLoader.load(resource.openStream());
            RunTaskTogglesController runTaskTogglesController = fxmlLoader.getController();
            runTaskTogglesController.setEngine(this.engine);
            base_BorderPane.setCenter(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void loadWhatIf(ActionEvent event) {
        URL resource = WhatIfMenuController.class.getResource("what_if_menu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            Parent root = fxmlLoader.load(resource.openStream());
            WhatIfMenuController whatIfMenuController = fxmlLoader.getController();
            whatIfMenuController.setEngine(this.engine);
            base_BorderPane.setCenter(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
