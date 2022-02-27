package loadfile;

import constants.Constants;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class LoadFileController {

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
    private File file;

    @FXML
    void selectFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML files", "*.xml");
        fileChooser.getExtensionFilters().add(xmlFilter);
        Stage windowStage = new Stage();
        this.file = fileChooser.showOpenDialog(windowStage);
        if(this.file != null){
            file_path_TextFiled.setText(this.file.getAbsolutePath());
        }
    }

    @FXML
    private void confirmFile(ActionEvent event) {
        if(file_path_TextFiled.getText().isEmpty() || file == null){

            warning_label.setVisible(false);
            warning_label.setText("Please enter a file");
            return;
        }
        sendFileToServer(this.file);
    }

    private void sendFileToServer(File file) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", this.file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                .build();

        Request request = new Request.Builder().url(Constants.UPLOAD_FILE_PAGE)
                .post(requestBody).build();

        HttpUtils.runAsyncWithRequest(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(()->{
                    warning_label.setText(e.getMessage());
                    warning_label.setVisible(true);
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                warning_label.setVisible(false);
                if(response.code() != 200){
                    Platform.runLater(()->{
                        try {
                            warning_label.setText(response.body().string());
                            warning_label.setTextFill(Color.RED);
                            warning_label.setVisible(true);
                            response.body().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else{
                    Platform.runLater(()->{
                        warning_label.setText("File loaded successfully YAY!!!!!!!");
                        warning_label.setTextFill(Color.GREEN);
                        warning_label.setVisible(true);
                    });
                }
            }
        });

    }

}
