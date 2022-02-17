package general_enums;

public enum Dependency {

    REQUIRED_FOR("requiredFor"),
    DEPENDS_ON("dependsOn");

    private String dependency;

    Dependency(String dependency) {
        this.dependency = dependency;
    }

    public String getDependency() {
        return dependency;
    }
}
