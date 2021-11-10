package exceptions;

public class InvalidFileException extends Exception{
    private String path;
    private String reason;

    public InvalidFileException(String path, String reason) {
        this.path = path;
        this.reason = reason;
    }

    public String getPath() {
        return path;
    }

    public String getReason() {
        return reason;
    }
}
