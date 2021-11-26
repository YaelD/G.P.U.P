package exceptions;

public class CycleException extends Exception{

    String targetName;

    public CycleException(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }
}
