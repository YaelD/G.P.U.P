package findpath;

//import engine.Engine;
//import exceptions.InvalidDependencyException;
//import exceptions.TargetNotExistException;
//import graph.Dependency;
import com.google.gson.Gson;
import constants.Constants;
import dto.GraphDTO;
import general_enums.Dependency;
import http_utils.HttpUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class FindPathsController {

    @FXML
    private TableView<String> findPathsTable;

    @FXML
    private PathsTableController findPathsTableController;

    @FXML
    private GridPane findPathsToggles;

    @FXML
    private FindPathTogglesController findPathsTogglesController;


    public void setGraph(GraphDTO graphDTO) {
        findPathsTogglesController.initChoiceBoxes(graphDTO);
        findPathsTogglesController.setFindPathCallback(new findPathCallback() {
            @Override
            public void findPaths(String sourceTargetName, String destinationTargetName, Dependency dependency) {
                String finalUrl = HttpUrl
                        .parse(Constants.LOGIN_PAGE)
                        .newBuilder()
                        .addQueryParameter(Constants.SOURCE_TARGET, sourceTargetName)
                        .addQueryParameter(Constants.DESTINATION_TARGET, destinationTargetName)
                        .addQueryParameter(Constants.DEPENDENCY, dependency.getDependency())
                        .build()
                        .toString();
                HttpUtils.runAsync(finalUrl, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        findPathsTogglesController.getWarningLabel().setVisible(true);
                        findPathsTogglesController.getWarningLabel().setText("Something went wrong..." + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(response.code() != 200){
                            findPathsTogglesController.getWarningLabel().setVisible(true);
                            findPathsTogglesController.getWarningLabel().setText("Something went wrong..." + response.body().string());
                        }
                        else{
                            String json = response.body().string();
                            String[][] paths = new Gson().fromJson(json, String[][].class);
                            findPathsTableController.setTableValues(paths);
                        }
                    }
                });

                findPathsTogglesController.getWarningLabel().setVisible(false);
                findPathsTable.getItems().clear();
//                if(null){
//                    findPathsTableController.setTableValues(paths);
//                }
//                else{
//                    findPathsTable.setPlaceholder(new Label("There is no path between the given Targets"));
//                    //findPathsTogglesController.getWarningLabel().setVisible(true);
//                    //findPathsTogglesController.getWarningLabel().setText("There is no path between the given Targets");
//                }
            }
        });
    }

}
