package exceptions;

public class DuplicateTargetsException extends Exception {
    private String targetName;

    public DuplicateTargetsException(String targetName) {
        this.targetName = targetName;
    }


    public String getTargetName() {
        return targetName;
    }
}
