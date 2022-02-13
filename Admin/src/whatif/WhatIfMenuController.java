package whatif;

import engine.Engine;
import graph.Dependency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import task.RunType;

import java.util.Set;

public class WhatIfMenuController {

    Engine engine;

    @FXML
    private ListView<String> targetsListView;

    @FXML
    private GridPane togglesMenu;

    @FXML
    private WhatIfToggleController togglesMenuController;


    public void setEngine(Engine engine) {
        this.engine = engine;
        //togglesMenuController.initChoiceBoxes(this.engine.getGraphDTO());
        togglesMenuController.setWhatIfCallback(new WhatIfCallback() {
            @Override
            public void findWhatIf(String targetName, Dependency dependency) {
                targetsListView.getItems().clear();
//                Set<String> targets = engine.whatIfForRunningTask(targetName, dependency, null, RunType.FROM_SCRATCH);
//                if(targets.isEmpty()){
//                    targetsListView.setPlaceholder(new Label("There are no targets to show"));
//                }
//                else{
//                    ObservableList<String> data = FXCollections.observableArrayList();
//                    data.addAll(targets);
//                    targetsListView.setItems(data);
//                }
            }
        });
    }




}
