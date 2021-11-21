import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SimulationTask extends Task{

    private int processTime;
    private boolean isRandom;
    private double successRate;
    private double successWithWarningsRate;

    public SimulationTask(Graph graph, int processTime, boolean isRandom, double successRate, double successWithWarningsRate) {
        super(graph);
        this.processTime = processTime;
        this.isRandom = isRandom;
        this.successRate = successRate;
        this.successWithWarningsRate = successWithWarningsRate;
    }
    // Map<String, TargetParams> map = new HashMap<>();

    @Override
    protected void executeTaskOnTarget(Target target) {
        int currTargetProcessTime = this.processTime;
        if(this.isRandom){
            currTargetProcessTime = getRandomProcessTime();
        }

    }

    private int getRandomProcessTime(){
        Random random = new Random();
        return random.nextInt(this.processTime + 1);
    }


//    public class TargetParams{
//        private String name;
//
//        public TargetParams(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//    }
}
