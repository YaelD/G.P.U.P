package tables;


import dto.GraphDTO;
import dto.TargetDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
//import target.PlaceInGraph;

import java.util.List;

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
        this.NumOfIndependents_Label.setText(String.valueOf(graphInfo.getNumOfIndependents()));
        this.NumOfLeaves_Label.setText(String.valueOf(graphInfo.getNumOfLeaves()));
        this.NumOfMiddles_Label.setText(String.valueOf(graphInfo.getNumOfMiddles()));
        this.NumOfRoots_Label.setText(String.valueOf(graphInfo.getNumOfRoots()));
        this.NumOfTargets_Label.setText(String.valueOf(graphInfo.getTotalNumOfTargets()));
        targetsTableViewController.setTargets(graphInfo);
    }


}
