package exceptions;

public class CycleException extends Exception{

    private String sourceTargetName;

    public CycleException(String targetName) {
        this.sourceTargetName = targetName;
    }

    public CycleException(){

    }

    public String getSourceTargetName() {
        return sourceTargetName;
    }



}
