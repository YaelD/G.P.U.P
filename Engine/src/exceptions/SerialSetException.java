package exceptions;

public class SerialSetException extends Exception{

    String targetName;

    public SerialSetException(String targetName, String serialSetName) {
        super();
        this.targetName = targetName;
    }
}