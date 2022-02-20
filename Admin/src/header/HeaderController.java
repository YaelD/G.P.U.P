package header;

import dto.GraphDTO;
import dto.TaskDTO;
import findcycles.FindCyclesController;
import findpath.FindPathsController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loadfile.*;
import runtask.menu.RunTaskMenuController;
import tables.*;
import whatif.WhatIfMenuController;

import java.io.IOException;
import java.net.URL;

public class HeaderController {

    GraphDTO graphDTO;

    String userName;

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



    public HeaderController() {

    }

    @FXML
    private  void initialize(){

    }


    @FXML
    private void loadFindCycle(ActionEvent event) {
        URL resource = FindCyclesController.class.getResource("searchCyclesMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            Parent root = fxmlLoader.load(resource.openStream());
            FindCyclesController findCyclesController = fxmlLoader.getController();
            findCyclesController.initTargetsChoiceBox(graphDTO);
            findCyclesController.setGraphDTO(graphDTO);
            base_BorderPane.setCenter(root);
//            primaryStage.show();
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
            findPathsController.setGraph(graphDTO);
            base_BorderPane.setCenter(root);
//            primaryStage.show();
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
            graphInfoController.setGraphInfo(graphDTO);
            base_BorderPane.setCenter(root);
//            primaryStage.show();
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
            whatIfMenuController.setGraphDTO(graphDTO);
            base_BorderPane.setCenter(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void createTask(ActionEvent event) {
        //TODO: Check for cycle
//        if(true){
//            base_BorderPane.setCenter(new Label("There is cycle in the graph, cannot run task"));
//            return;
//        }
        URL resource = RunTaskMenuController.class.getResource("run_task_menu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            Parent root = fxmlLoader.load(resource.openStream());
            RunTaskMenuController runTaskMenuController = fxmlLoader.getController();
            runTaskMenuController.setCurrGraph(graphDTO);
            runTaskMenuController.setUserName(userName);
            runTaskMenuController.disableIncremental(false);
            base_BorderPane.setCenter(root);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setGraphDTO(GraphDTO graphDTO) {
        this.graphDTO = graphDTO;
    }

}
