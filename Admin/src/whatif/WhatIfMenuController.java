package whatif;

import dto.GraphDTO;
import general_enums.Dependency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

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
                //TODO: make Whatif system call
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
