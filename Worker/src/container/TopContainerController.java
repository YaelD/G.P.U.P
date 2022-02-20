package container;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import tasks_menu.TaskMenuController;
import worker_dashboard.DashboardController;
import worker_login.LoginWindowController;

import java.io.IOException;
import java.net.URL;


public class TopContainerController {
    @FXML
    private GridPane loginComponent;
    @FXML
    private LoginWindowController loginController;
    @FXML
    private BorderPane mainPane;
    private BorderPane dashboard;
    private DashboardController dashboardController;

    private BorderPane taskMenu;
    private TaskMenuController taskMenuController;

    private SimpleStringProperty userName;
    private SimpleIntegerProperty numOfThreads;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label helloLabel;

    @FXML
    private void initialize(){
        userNameLabel.textProperty().bind(userName);
        loadLoginPage();
        loadDashboardPage();
        loadTaskMenu();
    }


    public TopContainerController() {
        userName = new SimpleStringProperty();
        numOfThreads = new SimpleIntegerProperty();
    }

    public SimpleIntegerProperty getNumOfThreads() {
        return numOfThreads;
    }


    public void setUserName(String userName) {
        this.userName.set(userName);
        Platform.runLater(()->{
            helloLabel.setVisible(true);
            userNameLabel.setVisible(true);
        });
    }

    private void loadTaskMenu() {
        URL loginPageUrl = TaskMenuController.class.getResource("task_menu.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            taskMenu = fxmlLoader.load();
            taskMenuController = fxmlLoader.getController();
            taskMenuController.setTotalNumOfThreads(this.numOfThreads);
            taskMenuController.setUserName(this.userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void loadLoginPage() {
        URL loginPageUrl = LoginWindowController.class.getResource("worker_login.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setTopContainerController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDashboardPage() {
        URL dashboardPageURL = DashboardController.class.getResource("worker_dashboard.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(dashboardPageURL);
            ScrollPane scrollPane = fxmlLoader.load(dashboardPageURL.openStream());
            this.dashboard = (BorderPane) scrollPane.contentProperty().get();
            this.dashboardController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMainPanelTo(Parent pane) {
        mainPane.centerProperty().set(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    public void switchToDashboard(){
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        Tab dashBoardTab = new Tab("Dashboard",dashboard);
        Tab taskInfoTab = new Tab("Task menu", taskMenu);
        dashBoardTab.setClosable(false);
        taskInfoTab.setClosable(false);
        dashboardController.setTopTabPane(tabs);
        tabs.getTabs().add(dashBoardTab);
        tabs.getTabs().add(taskInfoTab);
        mainPane.centerProperty().set(tabs);
        AnchorPane.setBottomAnchor(tabs, 1.0);
        AnchorPane.setTopAnchor(tabs, 1.0);
        AnchorPane.setLeftAnchor(tabs, 1.0);
        AnchorPane.setRightAnchor(tabs, 1.0);

    }

}

