package execution_details;

import dto.TargetDTO;
import dto.TaskDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.Map;

public class RunningTargetsTableController {

    SimpleObjectProperty<TaskDTO> taskDTO;

    @FXML
    private TableView<TargetsTableButtonsHandler> targetsTable;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> frozenColumn;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> waitingColumn;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> inProcessColumn;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> finishedColumn;

    @FXML
    private TableColumn<TargetsTableButtonsHandler, Button> skippedColumn;


    public RunningTargetsTableController() {
        this.taskDTO = new SimpleObjectProperty<>();
    }

    @FXML
    private void initialize(){
        taskDTO.addListener(new ChangeListener<TaskDTO>() {
            @Override
            public void changed(ObservableValue<? extends TaskDTO> observable, TaskDTO oldValue, TaskDTO newValue) {
                Map<String, TargetDTO> targetsMap  = newValue.getGraphDTO().getTargets();
                ObservableList<TargetsTableButtonsHandler> data = FXCollections.observableArrayList();
                for(TargetDTO currTarget: targetsMap.values()){
                    TargetsTableButtonsHandler targetDraw = new TargetsTableButtonsHandler(currTarget);
                    for(Button currButton: targetDraw.getButtonsMap().values()){
                        currButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                TargetDTO targetDTO = newValue.getGraphDTO().getTargets().get(targetDraw.getName());
                                String str = targetDTO.getRunningTargetStatus();
                                if(str.equals("")){
//                                    str = createRunResultString(targetDTO);
                                }
//                                targetInfoConsole.setText(str);

                            }
                        });
                    }
                }
                Platform.runLater(()->{
                    targetsTable.setItems(data);
                });
            }
        });

        frozenColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("frozenBtn"));
        waitingColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("waitingBtn"));
        inProcessColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("inProcessBtn"));
        finishedColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("finishedBtn"));
        skippedColumn.setCellValueFactory(new PropertyValueFactory<TargetsTableButtonsHandler, Button>("skippedBtn"));

    }

    public void setItems(SimpleObjectProperty<TaskDTO> taskProperty){
        this.taskDTO.bind(taskProperty);
    }

}
