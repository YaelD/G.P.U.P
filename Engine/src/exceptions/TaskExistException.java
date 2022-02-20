package exceptions;

public class TaskExistException extends Exception{
    private final String taskName;

    public TaskExistException(String taskName) {
        this.taskName = taskName;
    }

    public String getName() {
        return taskName;
    }
}
