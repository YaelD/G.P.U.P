package tables;

import dto.GraphDTO;
import dto.TargetDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import tables.TargetsTableController;
import target.PlaceInGraph;

public class GraphInfoController {

    @FXML
    private TableView<TargetDTO> targetsTableView;
    @FXML
    private TargetsTableController targetsTableViewController;

    @FXML
    private TableView<TargetDTO> serialSetTableView;

    @FXML
    private Label NumOfTargets_Label;


    @FXML
    private Label NumOfLeaves_Label;

    @FXML
    private Label NumOfMiddles_Label;

    @FXML
    private Label NumOfRoots_Label;

    @FXML
    private Label NumOfIndependents_Label;


    public void setGraphInfo(GraphDTO graphInfo){
        this.NumOfIndependents_Label.setText(String.valueOf(graphInfo.getNumOfTargetsInPlace(PlaceInGraph.INDEPENDENT)));
        this.NumOfLeaves_Label.setText(String.valueOf(graphInfo.getNumOfTargetsInPlace(PlaceInGraph.LEAF)));
        this.NumOfMiddles_Label.setText(String.valueOf(graphInfo.getNumOfTargetsInPlace(PlaceInGraph.MIDDLE)));
        this.NumOfRoots_Label.setText(String.valueOf(graphInfo.getNumOfTargetsInPlace(PlaceInGraph.ROOT)));
        this.NumOfTargets_Label.setText(String.valueOf(graphInfo.getNumOfTargets()));
        targetsTableViewController.setTargets(graphInfo);
    }

}
