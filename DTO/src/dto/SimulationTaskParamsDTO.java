package dto;

import general_enums.RunType;
import general_enums.TaskType;

import java.util.List;

public class SimulationTaskParamsDTO extends TaskParamsDTO {

    private int processTime;
    private boolean isRandom;
    private double successRate;
    private double successWithWarningsRate;




    public SimulationTaskParamsDTO(RunType runType, List<String> targets, String creatorName, String graphName, String taskName, int totalTaskPrice,
                                   int processTime, boolean isRandom, double successRate, double successWithWarningsRate) {
        super(creatorName,TaskType.SIMULATION_TASK, runType, targets, taskName, graphName, totalTaskPrice);
        this.processTime = processTime;
        this.isRandom = isRandom;
        this.successRate = successRate;
        this.successWithWarningsRate = successWithWarningsRate;
    }

    public SimulationTaskParamsDTO(String taskName,int processTime, boolean isRandom, double successRate, double successWithWarningsRate) {
        this.taskName = taskName;
        this.processTime = processTime;
        this.isRandom = isRandom;
        this.successRate = successRate;
        this.successWithWarningsRate = successWithWarningsRate;
        this.taskType = TaskType.SIMULATION_TASK;
    }

    public int getProcessTime() {
        return processTime;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public double getSuccessWithWarningsRate() {
        return successWithWarningsRate;
    }

    @Override
    public String toString() {
        return "Task type: Simulation" + "\n" +
                "Process Time:" + processTime + "\n" +
                "Is Random:" + isRandom + "\n" +
                "Success Rate:" + successRate + "\n" +
                "Success With Warnings Rate:" + successWithWarningsRate + "\n";
    }
}
