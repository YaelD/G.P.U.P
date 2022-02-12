package loadfile;

import engine.Engine;
import exceptions.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class LoadFileController {

    private Engine engine;
    SimpleBooleanProperty isFileLoaded;

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
    private void confirmFile(ActionEvent event) {
        if(file_path_TextFiled.getText().isEmpty()){
            warning_label.setDisable(false);
            warning_label.setText("Please enter a file");
            return;
        }
//
//        try {
//            warning_label.setVisible(true);
//            engine.loadFile(file_path_TextFiled.getText());
//            warning_label.setText("File loaded successfully YAY!!!!!!!");
//            isFileLoaded.set(true);
//            //headerController.enableButtons();
//        } catch (InvalidFileException e) {
//            warning_label.setText("Invalid File");
//        } catch (DependencyConflictException e) {
//            warning_label.setText("Can't load file: " +
//                    "\nDependency conflict between '" + e.getFirstTarget() + "' and '" +
//                    e.getSecondTarget() + "' With dependency: '" + e.getDependencyType() +"'");
//        } catch (DuplicateTargetsException e) {
//            warning_label.setText("Can't load file: " +
//                    "\nThe Target '" + e.getTargetName() + "' is appearing more than once");
//        } catch (InvalidDependencyException e) {
//            warning_label.setText("Can't load file: " +
//                    "\nThe dependency '" + e.getDependency() + "' is invalid") ;
//        } catch (TargetNotExistException e) {
//            warning_label.setText("Can't load file: " +
//                    "\nThe target '" + e.getName() + "' is not exist");
//        } catch (SerialSetException e) {
//            warning_label.setText("Can't load file: " +
//                    "\nThe serial set " + e.getSerialSetName() + " contains invalid target '" + e.getTargetName() + "'") ;
//        } catch (DupSerialSetsNameException e) {
//            warning_label.setText("Can't load file: " +
//                    "\nThe serial set '" + e.getSerialSetName() + "' appears several times");
//        }

    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setIsFileLoaded(SimpleBooleanProperty isFileLoaded) {
        this.isFileLoaded = isFileLoaded;
    }
}
