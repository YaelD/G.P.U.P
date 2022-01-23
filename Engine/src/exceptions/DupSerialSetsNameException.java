package exceptions;

public class DupSerialSetsNameException extends Exception{
    private String serialSetName;

    public DupSerialSetsNameException(String serialSetName) {
        this.serialSetName = serialSetName;
    }

    public String getSerialSetName() {
        return serialSetName;
    }
}
