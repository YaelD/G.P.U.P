package dto;

public class CompilationTaskParamsDTO implements TaskParamsDTO{
    private String sourceDir;
    private String DestinationDir;

    public CompilationTaskParamsDTO(String sourceDir, String destinationDir) {
        this.sourceDir = sourceDir;
        DestinationDir = destinationDir;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public String getDestinationDir() {
        return DestinationDir;
    }
}
