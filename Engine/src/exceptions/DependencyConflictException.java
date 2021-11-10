package exceptions;

public class DependencyConflictException extends Exception{
    private String firstTarget;
    private String secondTarget;
    private String dependencyType;

    public DependencyConflictException(String firstTarget, String secondTarget, String dependency) {
        this.firstTarget = firstTarget;
        this.secondTarget = secondTarget;
        dependencyType = dependency;
    }

    public String getFirstTarget() {
        return firstTarget;
    }

    public String getSecondTarget() {
        return secondTarget;
    }

    public String getDependencyType() {
        return dependencyType;
    }
}
