package whatif;

import com.google.gson.Gson;
import constants.Constants;
import dto.GraphDTO;
import general_enums.Dependency;
import http_utils.HttpUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;

public class WhatIfMenuController {


    @FXML
    private ListView<String> targetsListView;

    @FXML
    private GridPane togglesMenu;

    @FXML
    private WhatIfToggleController togglesMenuController;


    public void setGraphDTO(GraphDTO graphDTO) {
        togglesMenuController.initChoiceBoxes(graphDTO);
        togglesMenuController.setWhatIfCallback(new WhatIfCallback() {
            @Override
            public void findWhatIf(String targetName, Dependency dependency) {
                targetsListView.getItems().clear();
                String finalUrl = HttpUrl
                        .parse(Constants.WHAT_IF)
                        .newBuilder()
                        .addQueryParameter(Constants.SOURCE_TARGET, targetName)
                        .addQueryParameter(Constants.DEPENDENCY, dependency.name())
                        .addQueryParameter(Constants.GRAPH_NAME, graphDTO.getName())
                        .build()
                        .toString();
                HttpUtils.runAsync(finalUrl, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        togglesMenuController.getWarningLabel().setVisible(true);
                        togglesMenuController.getWarningLabel().setText("Something went wrong..." + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBody = response.body().string();
                        Platform.runLater(()->{
                            if (response.code() != 200) {
                                togglesMenuController.getWarningLabel().setVisible(true);
                                togglesMenuController.getWarningLabel().setText("Something went wrong..." + responseBody);
                            } else {
                                String[] targets = new Gson().fromJson(responseBody, String[].class);
                                if (targets.length == 0) {
                                    targetsListView.setPlaceholder(new Label("There are no targets to show"));
                                } else {
                                    ObservableList<String> data = FXCollections.observableArrayList();
                                    data.addAll(targets);
                                    targetsListView.setItems(data);
                                }
                            }

                        });
                    }
                });
            }
        });
    }
}
