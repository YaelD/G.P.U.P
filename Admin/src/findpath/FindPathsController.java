package findpath;

//import engine.Engine;
//import exceptions.InvalidDependencyException;
//import exceptions.TargetNotExistException;
//import graph.Dependency;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

import java.util.Collection;
import java.util.List;

public class FindPathsController {

    //Engine engine;

    @FXML
    private TableView<String> findPathsTable;

    @FXML
    private PathsTableController findPathsTableController;

    @FXML
    private GridPane findPathsToggles;

    @FXML
    private FindPathTogglesController findPathsTogglesController;


//
//    public void setEngine(Engine engine) {
//        this.engine = engine;
//        findPathsTogglesController.initChoiceBoxes(this.engine.getGraphDTO());
//        findPathsTogglesController.setFindPathCallback(new findPathCallback() {
//            @Override
//            public void findPaths(String sourceTargetName, String destinationTargetName, Dependency dependency) {
//                try {
//                    //Collection<List<String>> paths = engine.getPaths(sourceTargetName, destinationTargetName, dependency);
//                    findPathsTogglesController.getWarningLabel().setVisible(false);
//                    findPathsTable.getItems().clear();
//                    if(null){
//                        findPathsTableController.setTableValues(paths);
//                    }
//                    else{
//                        findPathsTable.setPlaceholder(new Label("There is no path between the given Targets"));
//                        //findPathsTogglesController.getWarningLabel().setVisible(true);
//                        //findPathsTogglesController.getWarningLabel().setText("There is no path between the given Targets");
//                    }
//
//                } catch (TargetNotExistException e) {
//                    e.printStackTrace();
//                } catch (InvalidDependencyException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
}
