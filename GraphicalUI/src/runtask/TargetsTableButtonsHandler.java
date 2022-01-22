package runtask;

import dto.TargetDTO;
import engine.Engine;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import target.RunResults;
import target.RunStatus;

import java.util.HashMap;
import java.util.Map;

public class TargetsTableButtonsHandler {
    private String name;

    private RunStatus runStatus;

    private Button frozenBtn;
    private Button waitingBtn;
    private Button inProcessBtn;
    private Button finishedBtn;
    private Button skippedBtn;

    private Map<RunStatus, Button> buttonsMap = new HashMap<>();


    public TargetsTableButtonsHandler(String name) {
        runStatus = RunStatus.FROZEN;
        this.name = name;
        frozenBtn = new Button(name);
        waitingBtn = new Button(name);
        inProcessBtn = new Button(name);
        finishedBtn = new Button(name);
        skippedBtn = new Button(name);
        buttonsMap.put(RunStatus.FINISHED, finishedBtn);
        buttonsMap.put(RunStatus.FROZEN, frozenBtn);
        buttonsMap.put(RunStatus.WAITING, waitingBtn);
        buttonsMap.put(RunStatus.IN_PROCESS, inProcessBtn);
        buttonsMap.put(RunStatus.SKIPPED, skippedBtn);
        waitingBtn.setVisible(false);
        inProcessBtn.setVisible(false);
        finishedBtn.setVisible(false);
        skippedBtn.setVisible(false);
    }



    public Map<RunStatus, Button> getButtonsMap() {
        return buttonsMap;
    }

    public String getName() {
        return name;
    }

    public Button getFrozenBtn() {
        return frozenBtn;
    }

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

    public void updateButtons(RunStatus runStatusToCheck) {
        if(!runStatus.equals(runStatusToCheck)){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    buttonsMap.get(runStatus).setVisible(false);
                    buttonsMap.get(runStatusToCheck).setVisible(true);
                    runStatus = runStatusToCheck;
                }
            });
        }
    }
}
