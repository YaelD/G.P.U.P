package load_file;

import engine.Engine;
import exceptions.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class LoadFileController {

    Engine engine;

    @FXML
    private Button fileChooser_btn;

    @FXML
    private Button cancel_btn;

    @FXML
    private Button confirm_btn;

    @FXML
    private TextField file_path_TextFiled;

    @FXML
    private Label warning_label;
    @FXML
    void selectFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML files", "*.xml");
        fileChooser.getExtensionFilters().add(xmlFilter);
        Stage windowStage = new Stage();
        File file = fileChooser.showOpenDialog(windowStage);
        if(file != null){
            file_path_TextFiled.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void confirmFile(ActionEvent event) {
        if(file_path_TextFiled.getText().isEmpty()){
            warning_label.setDisable(false);
            warning_label.setText("Please enter a file");
            return;
        }

        try {
            engine.readFile(file_path_TextFiled.getText());
            warning_label.setVisible(true);
            warning_label.setText("File loaded sucessfully YAY!!!!!!!");
        } catch (InvalidFileException e) {
            warning_label.setText("Invalid File");
        } catch (DependencyConflictException e) {
            warning_label.setText("Dependency conflict between " + e.getFirstTarget() + " and " +
                    e.getSecondTarget() + "With dependency: " + e.getDependencyType());
        } catch (DuplicateTargetsException e) {
            e.printStackTrace();
        } catch (InvalidDependencyException e) {
            e.printStackTrace();
        } catch (TargetNotExistException e) {
            e.printStackTrace();
        } catch (SerialSetException e) {
            e.printStackTrace();
        }

    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
