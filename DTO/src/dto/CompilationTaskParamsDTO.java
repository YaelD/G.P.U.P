package dto;

import general_enums.RunType;
import general_enums.TaskType;

import java.util.List;

public class CompilationTaskParamsDTO extends TaskParamsDTO{
    private String sourceDir;
    private String DestinationDir;

    public CompilationTaskParamsDTO(RunType runType, List<String> targets, String sourceDir, String destinationDir) {
        super(TaskType.COMPILATION_TASK, runType, targets);
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
