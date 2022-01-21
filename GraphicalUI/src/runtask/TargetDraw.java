package runtask;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class TargetDraw extends Button{
    //private SimpleObjectProperty<Button> button;
//    private StackPane stackPane;
    private SimpleStringProperty name;

    public TargetDraw(String name) {
        super();
        //this.getChildren().add(new Label(name));
        this.setText(name);
        this.name = new SimpleStringProperty(name);
        //this.name = new SimpleStringProperty(name);
        //button = new SimpleObjectProperty<>(new Button(name));
    }

    public String getName() {
        return name.get();
    }
}
