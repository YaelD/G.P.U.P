package execution_details;

import dto.TargetDTO;
import general_enums.RunResults;
import general_enums.RunStatus;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
//import target.RunResults;
//import target.RunStatus;

import java.util.*;

public class TargetsTableButtonsHandler {


    private String name;

    private Button frozenBtn;
    private Button waitingBtn;
    private Button inProcessBtn;
    private Button finishedBtn;
    private Button skippedBtn;

    Set<Button> ButtonSet;

    public TargetsTableButtonsHandler(TargetDTO targetDTO){
        ButtonSet = new HashSet<>();
        this.name = targetDTO.getName();
        frozenBtn = new Button(name);
        waitingBtn = new Button(name);
        inProcessBtn = new Button(name);
        finishedBtn = new Button(name);
        skippedBtn = new Button(name);
        ButtonSet.addAll(Arrays.asList(finishedBtn, frozenBtn, waitingBtn, inProcessBtn, skippedBtn));
        frozenBtn.setVisible(targetDTO.getRunStatus() == RunStatus.FROZEN);
        waitingBtn.setVisible(targetDTO.getRunStatus() == RunStatus.WAITING);
        inProcessBtn.setVisible(targetDTO.getRunStatus() == RunStatus.IN_PROCESS);
        finishedBtn.setVisible(targetDTO.getRunStatus() == RunStatus.FINISHED);
        skippedBtn.setVisible(targetDTO.getRunStatus() == RunStatus.SKIPPED);
    }

    public Set<Button> getButtonSet() {
        return ButtonSet;
    }

    public String getName() {
        return name;
    }

    public Button getFrozenBtn() { return frozenBtn; }

    public Button getWaitingBtn() {
        return waitingBtn;
    }

    public Button getInProcessBtn() {
        return inProcessBtn;
    }

    public Button getFinishedBtn() {
        return finishedBtn;
    }

    public Button getSkippedBtn() {
        return skippedBtn;
    }


}
