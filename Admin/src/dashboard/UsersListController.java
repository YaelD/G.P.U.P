package dashboard;

import RefreshingItems.UserListRefresherTimer;
import dto.UserDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UsersListController {

    private Timer timer;
    private TimerTask listRefresher;

    @FXML
    private TableView<UserDTO> users_list_table;

    @FXML
    private TableColumn<UserDTO, String> users_name_column;

    @FXML
    private TableColumn<UserDTO, String> rule_users_column;

    @FXML
    private void initialize(){
        loadUserTableColumns();
        startUserListRefresher();
    }

    private void loadUserTableColumns(){
        users_name_column.setCellValueFactory(new PropertyValueFactory<>("userName"));
        rule_users_column.setCellValueFactory(new PropertyValueFactory<>("userType"));
    }

    private void updateUsersList(List<UserDTO> users) {
        Platform.runLater(() -> {
            ObservableList<UserDTO> userDTOS = users_list_table.getItems();
            userDTOS.clear();
            userDTOS.addAll(users);
        });
    }

    public void startUserListRefresher() {

        UserListRefresherTimer.getInstance().addConsumer(this::updateUsersList);

//        listRefresher = new UserListRefresher(
//                this::updateUsersList);
//        timer = new Timer(true);
//        timer.schedule(listRefresher, 15000, 15000);
    }


}
