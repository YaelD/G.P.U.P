package worker_engine;

import dto.SimulationTaskParamsDTO;
import dto.TargetDTO;
import general_enums.RunResults;
import general_enums.RunStatus;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SimulationTaskExecution extends TaskExecution implements Runnable{

    private int processTime;
    private boolean isRandom;
    private double successRate;
    private double successWithWarningsRate;

    public SimulationTaskExecution(SimulationTaskParamsDTO taskParamsDTO, TargetDTO targetDTO){
        super(targetDTO);
        this.processTime = taskParamsDTO.getProcessTime();
        this.successRate = taskParamsDTO.getSuccessRate();
        this.isRandom = taskParamsDTO.isRandom();
        this.successWithWarningsRate = taskParamsDTO.getSuccessWithWarningsRate();
    }


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
            executionTarget.setStartProcessTime(startTime);
            executionTarget.setRunStatus(RunStatus.IN_PROCESS);
            this.sendTarget();
            Thread.sleep(currTargetProcessTime);
            endTime = LocalTime.now();
            executionTarget.setSpecificTaskLog("End time: " + endTime.format(DateTimeFormatter.ofPattern("H:mm:ss")));
            this.sendTarget();
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
            this.sendTarget();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
