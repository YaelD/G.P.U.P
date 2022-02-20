package runtask.compilation_task;

//import dto.CompilationTaskParamsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import runtask.menu.CompilationParamsCallBack;
import runtask.menu.ReturnCallback;

import java.io.File;

public class CompilationParamsController {

    private String sourcePath;
    private String destinationPath;
    private ReturnCallback returnCallback;
    @FXML private Button sourceFolderButton;
    @FXML private Button destinationFolderButtons;
    @FXML private Button confirmButton;
    @FXML private TextField sourceTextArea;
    @FXML private TextField destinationTextArea;
    @FXML private Label warningLabel;


    CompilationParamsCallBack callback;


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
            callback.setCompilationParams(sourcePath, destinationPath);
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

    @FXML
    void returnToPrevWindow(ActionEvent event) {
        this.returnCallback.returnToPrev();
    }

    public void setActiveTaskCallback(CompilationParamsCallBack callback) {
        this.callback = callback;
    }

    public void setReturnCallBack(ReturnCallback returnCallBack) {
        this.returnCallback = returnCallBack;
    }


}
