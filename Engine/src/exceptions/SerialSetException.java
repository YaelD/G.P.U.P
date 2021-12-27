package exceptions;

public class SerialSetException extends Exception{

    String targetName;
    String serialSetName;

    public SerialSetException(String targetName, String serialSetName) {
        this.targetName = targetName;
        this.serialSetName = serialSetName;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getSerialSetName() {
        return serialSetName;
    }
}