package findcycles;


import com.google.gson.Gson;
import constants.Constants;
import dto.GraphDTO;
import dto.TargetDTO;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class FindCyclesController {

    GraphDTO graphDTO;

    @FXML
    private ChoiceBox<String> targetsChoiceBox;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> cyclesListView;

    @FXML
    private Label warningLabel;

    @FXML
    void searchCycles(ActionEvent event) {
        if(validate()){
            String targetName = this.targetsChoiceBox.getValue();
            warningLabel.setVisible(false);
            cyclesListView.getItems().clear();

            String finalUrl = HttpUrl
                    .parse(Constants.FIND_CYCLE)
                    .newBuilder()
                    .addQueryParameter(Constants.SOURCE_TARGET, targetName)
                    .addQueryParameter(Constants.GRAPH_NAME, graphDTO.getName())
                    .build()
                    .toString();
            HttpUtils.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    cyclesListView.setPlaceholder(new Label("Something went wrong" + e.getMessage()));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.code() == 200){
                        String[] finalCycle = new Gson().fromJson(response.body().string(), String[].class);
                        Platform.runLater(()->{
                            if(finalCycle == null){
                                cyclesListView.setPlaceholder(new Label("This target does not take place in any cycle"));
                            }
                            else{
                                ObservableList<String> data = FXCollections.observableArrayList();
                                data.addAll(finalCycle);
                                cyclesListView.setItems(data);
                            }
                        });
                    }
                    else{
                    }

                }
            });

        }
    }

    private boolean validate(){
        boolean isValidate = true;
        if(this.targetsChoiceBox.getValue() == null){
            warningLabel.setVisible(true);
            warningLabel.setText("Please choose a target");
            isValidate = false;
        }
        return isValidate;
    }

    public void initTargetsChoiceBox(GraphDTO graphDTO){
        for(TargetDTO targetDTO: graphDTO.getTargets().values()){
            this.targetsChoiceBox.getItems().add(targetDTO.getName());
        }
    }

    public void setGraphDTO(GraphDTO graphDTO) {
        this.graphDTO = graphDTO;
    }
}

