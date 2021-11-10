package exceptions;

public class TargetNotExistException extends Exception{

    private String name;

    public TargetNotExistException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
