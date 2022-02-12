package execution_details;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
//import target.RunResults;
//import target.RunStatus;

import java.util.HashMap;
import java.util.Map;

public class TargetsTableButtonsHandler {
//    private String name;
//
//    private SimpleObjectProperty<RunStatus> runStatus;
//    private SimpleObjectProperty<RunResults> runResults;
//
//    private Button frozenBtn;
//    private Button waitingBtn;
//    private Button inProcessBtn;
//    private Button finishedBtn;
//    private Button skippedBtn;
//
//    private Map<RunStatus, Button> buttonsMap = new HashMap<>();
//
//
//    public TargetsTableButtonsHandler(String name) {
//        runResults = new SimpleObjectProperty<>(RunResults.SUCCESS);
//        runStatus = new SimpleObjectProperty<>(RunStatus.FROZEN);
//        runStatus.addListener(new ChangeListener<RunStatus>() {
//            @Override
//            public void changed(ObservableValue<? extends RunStatus> observable, RunStatus oldValue, RunStatus newValue) {
//                if(!oldValue.equals(newValue)){
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            buttonsMap.get(oldValue).setVisible(false);
//                            buttonsMap.get(newValue).setVisible(true);
//                            if(newValue.equals(RunStatus.FINISHED)){
//                                switch (runResults.getValue()){
//                                    case SUCCESS:
//                                        buttonsMap.get(RunStatus.FINISHED).setStyle("-fx-background-color: #63ff46; ");
//                                        break;
//                                    case FAILURE:
//                                        buttonsMap.get(RunStatus.FINISHED).setStyle("-fx-background-color: #ff3d3d; ");
//                                        break;
//                                    case WARNING:
//                                        buttonsMap.get(RunStatus.FINISHED).setStyle("-fx-background-color: #ff8337; ");
//                                        break;
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        this.name = name;
//        frozenBtn = new Button(name);
//        waitingBtn = new Button(name);
//        inProcessBtn = new Button(name);
//        finishedBtn = new Button(name);
//        skippedBtn = new Button(name);
//        buttonsMap.put(RunStatus.FINISHED, finishedBtn);
//        buttonsMap.put(RunStatus.FROZEN, frozenBtn);
//        buttonsMap.put(RunStatus.WAITING, waitingBtn);
//        buttonsMap.put(RunStatus.IN_PROCESS, inProcessBtn);
//        buttonsMap.put(RunStatus.SKIPPED, skippedBtn);
//        waitingBtn.setVisible(false);
//        inProcessBtn.setVisible(false);
//        finishedBtn.setVisible(false);
//        skippedBtn.setVisible(false);
//    }
//
//    public Map<RunStatus, Button> getButtonsMap() {
//        return buttonsMap;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Button getFrozenBtn() { return frozenBtn; }
//
//    public Button getWaitingBtn() {
//        return waitingBtn;
//    }
//
//    public Button getInProcessBtn() {
//        return inProcessBtn;
//    }
//
//    public Button getFinishedBtn() {
//        return finishedBtn;
//    }
//
//    public Button getSkippedBtn() {
//        return skippedBtn;
//    }
//
//    public void setRunResults(RunResults runResults) {
//        this.runResults.set(runResults);
//    }
//
//    public void setRunStatus(RunStatus runStatus) {
//        this.runStatus.set(runStatus);
//    }


}
