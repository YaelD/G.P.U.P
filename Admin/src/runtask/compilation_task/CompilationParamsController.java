package runtask.compilation_task;

//import dto.CompilationTaskParamsDTO;
import dto.CompilationTaskParamsDTO;
import general_enums.RunType;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import runtask.menu.ActiveTaskCallback;
import runtask.menu.ReturnCallback;

import java.io.File;

public class CompilationParamsController {

    private String sourcePath;
    private String destinationPath;
    private ActiveTaskCallback callback;
    private ReturnCallback returnCallback;
    @FXML private Button sourceFolderButton;
    @FXML private Button destinationFolderButtons;
    @FXML private Button confirmButton;
    @FXML private TextField sourceTextArea;
    @FXML private TextField destinationTextArea;
    @FXML private Label warningLabel;

    private SimpleListProperty<String> targetsList;
    private SimpleObjectProperty<RunType> runType;


    public CompilationParamsController() {
        this.sourcePath = "";
        this.destinationPath = "";
        this.runType = new SimpleObjectProperty<>(RunType.FROM_SCRATCH);
        this.targetsList = new SimpleListProperty<>();

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
            callback.sendTask(new CompilationTaskParamsDTO(runType.get(), targetsList.get(), sourcePath, destinationPath));
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

    public void setActiveTaskCallback(ActiveTaskCallback callback) {
        this.callback = callback;
    }

    public void setReturnCallBack(ReturnCallback returnCallBack) {
        this.returnCallback = returnCallBack;
    }

    public void bindProperties(SimpleListProperty<String> targetsList,SimpleObjectProperty<RunType> runType){
        this.runType.bind(runType);
        this.targetsList.bind(targetsList);
    }

}
