package runtask;

import dto.GraphDTO;
import dto.TargetDTO;
import engine.Engine;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class ExecuteTaskOnGraphTask extends Task<Void> {

    private Consumer<TargetDTO> updateTargetResultConsumer;
    private Consumer<GraphDTO> updateGraphResultConsumer;
    private Engine engine;

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    protected Void call() throws Exception {


        return null;
    }
}
