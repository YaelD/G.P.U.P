package runtask;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class TargetRepresenter {
    private SimpleObjectProperty<Button> button;
    private SimpleStringProperty name;

    public TargetRepresenter(String name) {
        this.name = new SimpleStringProperty(name);
        button = new SimpleObjectProperty<>(new Button(name));
    }

    public Button getButton() {
        return button.get();
    }

    public SimpleObjectProperty<Button> buttonProperty() {
        return button;
    }
}
