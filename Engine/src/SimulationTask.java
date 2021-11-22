import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

public class SimulationTask extends Task{

    private int processTime;
    private boolean isRandom;
    private double successRate;
    private double successWithWarningsRate;

    public SimulationTask(Graph graph, SimulationTaskParamsDTO simulationTaskDTO) {
        super(graph);
        this.processTime = simulationTaskDTO.getProcessTime();
        this.isRandom = simulationTaskDTO.isRandom();
        this.successRate = simulationTaskDTO.getSuccessRate();
        this.successWithWarningsRate = simulationTaskDTO.getSuccessWithWarningsRate();
    }



    @Override
    protected TargetDTO executeTaskOnTarget(Target target, Consumer<String> consumerString) {
        List<String> taskResults = new ArrayList<>();
        TargetDTO targetDTO = null;
        int currTargetProcessTime = this.processTime;
        if(this.isRandom){
            currTargetProcessTime = getRandomProcessTime();
        }
        try {
            Instant startTime = Instant.now();
            RunResults runResult;
            consumerString.accept("Start processing on Target: " + target.getName());
            Thread.sleep(currTargetProcessTime);
            Instant endTime = Instant.now();
            consumerString.accept("Finish processing on Target: " + target.getName());
            if(getRandomNumber() <= this.successRate){
                if(getRandomNumber() <= this.successWithWarningsRate){
                    consumerString.accept("The Target finished with success with warnings");
                    runResult = RunResults.WARNING;
                } else{
                    consumerString.accept("The Target finished with success");
                    runResult = RunResults.SUCCESS;
                }
            }else {
                consumerString.accept("The Target finished with failure");
                runResult = RunResults.FAILURE;
            }
            Duration diffTime = Duration.between(startTime, endTime);
            targetDTO = new TargetDTO(target, runResult,diffTime.toMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return targetDTO;
    }

    private int getRandomProcessTime(){
        Random random = new Random();
        return random.nextInt(this.processTime + 1);
    }

    private double getRandomNumber(){
        Random random = new Random();
        return random.nextDouble();
    }

}
