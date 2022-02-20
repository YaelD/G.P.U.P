package dto;

import general_enums.RunType;
import general_enums.TaskType;

import java.util.List;

public class CompilationTaskParamsDTO extends TaskParamsDTO{
    private String sourceDir;
    private String DestinationDir;

    public CompilationTaskParamsDTO(RunType runType, List<String> targets, String creatorName, String graphName, String taskName, int totalTaskPrice,
                                    String sourceDir, String destinationDir) {
        super(creatorName,TaskType.SIMULATION_TASK, runType, targets, taskName, graphName, totalTaskPrice);
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
