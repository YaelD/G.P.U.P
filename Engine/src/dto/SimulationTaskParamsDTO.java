package dto;

public class SimulationTaskParamsDTO implements TaskParamsDTO {

    private int processTime;
    private boolean isRandom;
    private double successRate;
    private double successWithWarningsRate;

    public SimulationTaskParamsDTO(int processTime, boolean isRandom, double successRate, double successWithWarningsRate) {
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
