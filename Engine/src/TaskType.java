public enum TaskType {

    SIMULATION_TASK ("Simulation");

    private String taskType;

    TaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }
}
