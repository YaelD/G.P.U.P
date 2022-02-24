import RefreshingItems.GraphListRefresherTimer;
import RefreshingItems.TaskListRefresherTimer;
import RefreshingItems.UserListRefresherTimer;
import container.TopContainerController;
import http_utils.HttpUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import worker_engine.ExecutionTarget;
import worker_engine.ExecutionTargetsRefresherTimer;
import worker_engine.SendExecutionTargetRefresherTimer;
import worker_engine.WorkerEngine;
import worker_login.LoginWindowController;

import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
        System.out.println("Got back to main!!");
        HttpUtils.ShutDown();
        TaskListRefresherTimer.getInstance().cancel();
        GraphListRefresherTimer.getInstance().cancel();
        UserListRefresherTimer.getInstance().cancel();
        WorkerEngine.getInstance().closeThreadPool();
        SendExecutionTargetRefresherTimer.getInstance().cancel();
        ExecutionTargetsRefresherTimer.getInstance().cancel();
    }

    public void start(Stage primaryStage) throws Exception {
        URL resource = TopContainerController.class.getResource("top_container.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        Parent root = fxmlLoader.load(resource.openStream());
        Scene scene = new Scene(root, 1000, 600);
        TopContainerController topContainerController = fxmlLoader.getController();
        primaryStage.setScene(scene);
        primaryStage.setTitle("GPUP");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(400);
        primaryStage.show();


    }

}
