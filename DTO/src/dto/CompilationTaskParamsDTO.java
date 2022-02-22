package dto;

import general_enums.RunType;
import general_enums.TaskType;

import java.util.List;

public class CompilationTaskParamsDTO extends TaskParamsDTO{
    private String sourceDir;
    private String destinationDir;

    public CompilationTaskParamsDTO(RunType runType, List<String> targets, String creatorName, String graphName, String taskName, int totalTaskPrice,
                                    String sourceDir, String destinationDir) {
        super(creatorName,TaskType.SIMULATION_TASK, runType, targets, taskName, graphName, totalTaskPrice);
        this.sourceDir = sourceDir;
        this.destinationDir = destinationDir;
    }

    public CompilationTaskParamsDTO(String sourceDir, String destinationDir) {
        this.sourceDir = sourceDir;
        this.destinationDir = destinationDir;
        this.taskType = TaskType.COMPILATION_TASK;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public String getDestinationDir() {
        return destinationDir;
    }

    @Override
    public String toString() {
        return "Task type: Compilation" + "\n" +
                "Source Directory:'" + sourceDir + '\'' + "\n" +
                "Destination Directory:'" + destinationDir + '\'' + "\n";
    }
}
