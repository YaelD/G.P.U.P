package exceptions;

public class TaskNotExistException extends Exception{

    private final String taskName;

    public TaskNotExistException(String taskName) {
        this.taskName = taskName;
    }

    public String getName() {
        return taskName;
    }
}
