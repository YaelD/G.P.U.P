package exceptions;

public class InvalidDependencyException extends Exception {
    private String dependency;

    public InvalidDependencyException(String dependency) {
        this.dependency = dependency;
    }

    public String getDependency() {
        return dependency;
    }
}
