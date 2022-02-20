package runtask.menu;

public interface SimulationParamsCallBack {
    public void sendSimulationTaskParams(int processTime ,boolean isRandom, double successRate, double successRateWithWarnings);
}
