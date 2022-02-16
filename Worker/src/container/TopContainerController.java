package container;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

    private SimpleStringProperty userName;
    private SimpleIntegerProperty numOfThreads;

    @FXML
    private void initialize(){
        loadLoginPage();
        loadDashboardPage();
    }

    public TopContainerController() {
        userName = new SimpleStringProperty();
    }

    public SimpleIntegerProperty getNumOfThreads() {
        return numOfThreads;
    }


    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    private void loadLoginPage() {
        URL loginPageUrl = LoginWindowController.class.getResource("login.fxml");
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
        URL dashboardPageURL = DashboardController.class.getResource("dashboard.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(dashboardPageURL);
            this.dashboard = fxmlLoader.load(dashboardPageURL.openStream());
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
       // currentUserName.set(JHON_DOE);
        //chatRoomComponentController.setInActive();
        setMainPanelTo(dashboard);

    }

}

