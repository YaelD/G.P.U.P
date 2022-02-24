package worker_engine;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Timer;
import java.util.TimerTask;

public class ExecutionTargetsRefresherTimer extends Timer {

    private ExecutionTargetsRefresherTimer instance;

    private ExecutionTargetsRefresherTimer(){

    }

    private class ExecutionTargetsRefresher extends TimerTask{

        SimpleIntegerProperty numOfThreads;

        public ExecutionTargetsRefresher() {
            this.numOfThreads = new SimpleIntegerProperty();
//            numOfThreads.bind(WorkerEngine.getInstance())
        }

        @Override
        public void run() {

        }
    }


}
