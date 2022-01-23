import engine.SystemEngine;
import header.HeaderController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getResource("header/head_and_missing_body.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        BorderPane root = fxmlLoader.load(resource.openStream());
        Scene scene = new Scene(root, 1000, 600);
        HeaderController headerController = fxmlLoader.getController();
        headerController.setPrimaryStage(primaryStage);
        headerController.setEngine(new SystemEngine());
        primaryStage.setScene(scene);
        primaryStage.setTitle("GPUP");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(400);

        primaryStage.show();
    }
}
