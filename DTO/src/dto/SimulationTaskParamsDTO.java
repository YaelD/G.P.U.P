package dto;

import general_enums.RunType;
import general_enums.TaskType;

import java.util.List;

public class SimulationTaskParamsDTO extends TaskParamsDTO {

    private int processTime;
    private boolean isRandom;
    private double successRate;
    private double successWithWarningsRate;

    public SimulationTaskParamsDTO(RunType runType, List<String> targets, int processTime, boolean isRandom, double successRate, double successWithWarningsRate) {
        super(TaskType.SIMULATION_TASK, runType, targets);
        this.processTime = processTime;
        this.isRandom = isRandom;
        this.successRate = successRate;
        this.successWithWarningsRate = successWithWarningsRate;
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
}
