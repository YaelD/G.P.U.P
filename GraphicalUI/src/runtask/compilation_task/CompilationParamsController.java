package runtask.compilation_task;

import dto.CompilationTaskParamsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import runtask.menu.ActiveTaskCallback;

import java.io.File;

public class CompilationParamsController {

    private String sourcePath;
    private String destinationPath;
    private ActiveTaskCallback callback;

    @FXML private Button sourceFolderButton;
    @FXML private Button destinationFolderButtons;
    @FXML private Button confirmButton;
    @FXML private TextField sourceTextArea;
    @FXML private TextField destinationTextArea;
    @FXML private Label warningLabel;

    public CompilationParamsController() {
        this.sourcePath = "";
        this.destinationPath = "";
    }

    @FXML
    void confirm(ActionEvent event) {
        if(sourcePath.equals("")){
            warningLabel.setVisible(true);
            warningLabel.setText("There is no source path");

        }
        else if(destinationPath.equals("")){
            warningLabel.setVisible(true);
            warningLabel.setText("There is no destination path");
        }
        else{
            callback.activeTask(new CompilationTaskParamsDTO(sourcePath, destinationPath));
        }
    }

    @FXML
    void openDestinationFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Source folder");
        Stage windowStage = new Stage();

        File directory = directoryChooser.showDialog(windowStage);
        if(directory != null){
            destinationPath = directory.getAbsolutePath();
            destinationTextArea.setText(directory.getAbsolutePath());
        }

    }

    @FXML
    void openSourceFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Destination folder");
        Stage windowStage = new Stage();

        File directory = directoryChooser.showDialog(windowStage);
        if(directory != null){
            sourcePath = directory.getAbsolutePath();
            sourceTextArea.setText(directory.getAbsolutePath());
        }
    }

    public void setActiveTaskCallback(ActiveTaskCallback callback) {
        this.callback = callback;
    }
}
