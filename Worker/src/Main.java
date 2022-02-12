import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getResource("worker_login/worker_login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        Parent root = fxmlLoader.load(resource.openStream());
        Scene scene = new Scene(root, 1000, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("GPUP");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(400);
        primaryStage.show();


    }

}
