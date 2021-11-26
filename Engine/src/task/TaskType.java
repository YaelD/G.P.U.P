package task;

public enum TaskType {

    SIMULATION_TASK ("Simulation"),
    COMPILATION_TASK("Compilation");

    private String taskType;


    TaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }
}
