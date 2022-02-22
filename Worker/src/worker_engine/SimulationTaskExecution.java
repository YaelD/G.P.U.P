package worker_engine;

import dto.SimulationTaskParamsDTO;
import dto.TargetDTO;
import general_enums.RunResults;
import general_enums.RunStatus;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SimulationTaskExecution implements Runnable{

    private int processTime;
    private boolean isRandom;
    private double successRate;
    private double successWithWarningsRate;
    private ExecutionTarget executionTarget;

    public SimulationTaskExecution(SimulationTaskParamsDTO taskParamsDTO, TargetDTO targetDTO){
        this.processTime = taskParamsDTO.getProcessTime();
        this.successRate = taskParamsDTO.getSuccessRate();
        this.isRandom = taskParamsDTO.isRandom();
        this.successWithWarningsRate = taskParamsDTO.getSuccessWithWarningsRate();
        this.executionTarget = new ExecutionTarget(targetDTO);
    }


//    public void executeTaskOnTarget(ExecutionTarget target) {
//        LocalTime startTime, endTime;
//        int currTargetProcessTime = this.processTime;
//
//        if(this.isRandom){
//            currTargetProcessTime = getRandomProcessTime();
//        }
//        try {
//            target.setSpecificTaskLog("Simulation task: ");
//            startTime = LocalTime.now();
//            target.setSpecificTaskLog("Start time: " + startTime.format(DateTimeFormatter.ofPattern("H:mm:ss")));
//            target.setRunStatus(RunStatus.IN_PROCESS);
//            Thread.sleep(currTargetProcessTime);
//            endTime = LocalTime.now();
//            target.setSpecificTaskLog("End time: " + endTime.format(DateTimeFormatter.ofPattern("H:mm:ss")));
//            if(getRandomNumber() <= this.successRate){
//                if(getRandomNumber() <= this.successWithWarningsRate){
//                    target.setRunResult(RunResults.WARNING);
//                }else{
//                    target.setRunResult(RunResults.SUCCESS);
//                }
//            }else{
//                target.setRunResult(RunResults.FAILURE);
//            }
//            target.setSpecificTaskLog("Running time: " + Duration.between(startTime, endTime).toMillis() + "Milliseconds");
//            target.setRunStatus(RunStatus.FINISHED);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    private int getRandomProcessTime(){
        Random random = new Random();
        return random.nextInt(this.processTime + 1);
    }

    private double getRandomNumber(){
        Random random = new Random();
        return random.nextDouble();
    }


    @Override
    public void run() {
        LocalTime startTime, endTime;
        int currTargetProcessTime = this.processTime;

        if(this.isRandom){
            currTargetProcessTime = getRandomProcessTime();
        }
        try {
            executionTarget.setSpecificTaskLog("Simulation task: ");
            startTime = LocalTime.now();
            executionTarget.setSpecificTaskLog("Start time: " + startTime.format(DateTimeFormatter.ofPattern("H:mm:ss")));
            executionTarget.setRunStatus(RunStatus.IN_PROCESS);
            Thread.sleep(currTargetProcessTime);
            endTime = LocalTime.now();
            executionTarget.setSpecificTaskLog("End time: " + endTime.format(DateTimeFormatter.ofPattern("H:mm:ss")));
            if(getRandomNumber() <= this.successRate){
                if(getRandomNumber() <= this.successWithWarningsRate){
                    executionTarget.setRunResult(RunResults.WARNING);
                }else{
                    executionTarget.setRunResult(RunResults.SUCCESS);
                }
            }else{
                executionTarget.setRunResult(RunResults.FAILURE);
            }
            executionTarget.setSpecificTaskLog("Running time: " + Duration.between(startTime, endTime).toMillis() + "Milliseconds");
            executionTarget.setRunStatus(RunStatus.FINISHED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
