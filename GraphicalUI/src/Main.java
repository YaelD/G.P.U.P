import engine.SystemEngine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import load_file.LoadFileController;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getResource("load_file/load_file.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        Parent root = fxmlLoader.load(resource.openStream());
        LoadFileController loadFileController = fxmlLoader.getController();
        loadFileController.setEngine(new SystemEngine());

        Scene scene = new Scene(root, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
