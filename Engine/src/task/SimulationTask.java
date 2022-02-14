package task;

import graph.Graph;
import graph.SerialSetsContainer;
import target.RunResults;
import target.RunStatus;
import target.Target;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class SimulationTask extends Task{

    private int processTime;
    private boolean isRandom;
    private double successRate;
    private double successWithWarningsRate;

    public SimulationTask(Graph graph, String creatorName, String taskName) {
        super(graph, creatorName, taskName);
    }

//    public SimulationTask(Graph graph, SimulationTaskParamsDTO simulationTaskDTO, SerialSetsContainer serialSetsContainer) {
//        super(graph, "", "");
//        this.processTime = simulationTaskDTO.getProcessTime();
//        this.isRandom = simulationTaskDTO.isRandom();
//        this.successRate = simulationTaskDTO.getSuccessRate();
//        this.successWithWarningsRate = simulationTaskDTO.getSuccessWithWarningsRate();
//    }
/*



    @Override
    public void updateParameters(TaskParamsDTO taskParamsDTO) {
        if(taskParamsDTO instanceof SimulationTaskParamsDTO){
            SimulationTaskParamsDTO simulationTaskParamsDTO = (SimulationTaskParamsDTO) taskParamsDTO;
            this.processTime = simulationTaskParamsDTO.getProcessTime();
            this.isRandom = simulationTaskParamsDTO.isRandom();
            this.successRate = simulationTaskParamsDTO.getSuccessRate();
            this.successWithWarningsRate = simulationTaskParamsDTO.getSuccessWithWarningsRate();
        }
    }

    @Override
    protected TargetDTO executeTaskOnTarget(Target target) {
        TargetDTO targetDTO = null;
        //LocalTime startTime, endTime;
        int currTargetProcessTime = this.processTime;

        if(this.isRandom){
            currTargetProcessTime = getRandomProcessTime();
        }
        try {
            target.setStartingProcessTime(LocalTime.now());
            target.setRunStatus(RunStatus.IN_PROCESS);
            //startTime = LocalTime.now();
            Thread.sleep(currTargetProcessTime);
            //endTime = LocalTime.now();
            target.setEndingProcessTime(LocalTime.now());
            if(getRandomNumber() <= this.successRate){
                if(getRandomNumber() <= this.successWithWarningsRate){
                    target.setRunResult(RunResults.WARNING);
                }else{
                    target.setRunResult(RunResults.SUCCESS);
                }
            }else{
                target.setRunResult(RunResults.FAILURE);
            }
            target.setRunningTime(Duration.between(target.getStartingProcessTime(), target.getEndingProcessTime()).toMillis());
            target.setRunStatus(RunStatus.FINISHED);
            targetDTO = new TargetDTO(target, null);

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

 */
}
