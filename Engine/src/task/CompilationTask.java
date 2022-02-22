package task;

import dto.*;
import general_enums.RunResults;
import general_enums.RunStatus;
import general_enums.TaskType;
import graph.Graph;
import target.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class CompilationTask extends Task{

    private final String JAVA_COMPILER = "javac";
    private final String SOURCE_DIR_REF_PARAM = "-cp";
    private final String DESTINATION_DIR_REF_PARAM = "-d";

    private String sourceDir;
    private String destinationDir;

    public CompilationTask(CompilationTaskParamsDTO compilationTaskParamsDTO, Graph graph) {
        super(graph, compilationTaskParamsDTO.getCreatorName(),
                compilationTaskParamsDTO.getTaskName(), compilationTaskParamsDTO.getTotalTaskPrice());
        this.destinationDir = compilationTaskParamsDTO.getDestinationDir();
        this.sourceDir = compilationTaskParamsDTO.getSourceDir();
    }

    @Override
    protected void executeTaskOnTarget(Target target) {
        LocalTime startTime, endTime;
        try {
            target.setTaskSpecificLogs("Compilation task: ");
            startTime = LocalTime.now();
            target.setTaskSpecificLogs("Start time: " + startTime.format(DateTimeFormatter.ofPattern("H:mm:ss")));
            target.setRunStatus(RunStatus.IN_PROCESS);
            String localSourceDir = buildPaths(target);
            String filePath = "/" + target.getInfo().replace('.', '/');
            Process process = CompileTarget(target, localSourceDir, filePath);
            String processResult = getProcessResult(process);
            int exitCode = process.waitFor();
            endTime = LocalTime.now();
            target.setTaskSpecificLogs("End time: " + endTime.format(DateTimeFormatter.ofPattern("H:mm:ss")));
            if(exitCode != 0){    //means that the process has failed
                target.setRunResult(RunResults.FAILURE);

                target.setTaskSpecificLogs("Javac output: " + processResult);
            }
            else {
                target.setRunResult(RunResults.SUCCESS);
                target.setTaskSpecificLogs( "Compilation run result: "+processResult);
            }

            target.setTaskSpecificLogs("Running time: " + Duration.between(startTime, endTime).toMillis() + "Milliseconds");
            target.setRunStatus(RunStatus.FINISHED);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getProcessResult(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        String result = builder.toString();

        return result;
    }
    private String buildPaths(Target target){
        String filePath = "/" + target.getInfo().replace('.', '/');
        int indexOfLastSlash = filePath.lastIndexOf('/');
        String dirPath = filePath.substring(0,indexOfLastSlash);
        String localSourceDir = this.sourceDir + dirPath;
        return localSourceDir;
    }

    private Process CompileTarget(Target target, String localSourceDir, String filePath) throws IOException {
        target.setTaskSpecificLogs("Start compilation time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm:ss")));
        Process process = new ProcessBuilder(JAVA_COMPILER, DESTINATION_DIR_REF_PARAM, this.destinationDir,
                SOURCE_DIR_REF_PARAM ,localSourceDir, filePath)
                .directory(new File(this.sourceDir))
                .redirectErrorStream(true)
                .start();
        target.setTaskSpecificLogs("End compilation time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm:ss")));
        return process;

    }

    @Override
    public void updateParameters(TaskParamsDTO taskParamsDTO) {
        if(taskParamsDTO instanceof CompilationTaskParamsDTO){
            CompilationTaskParamsDTO compilationTaskParamsDTO = (CompilationTaskParamsDTO) taskParamsDTO;
            this.sourceDir = compilationTaskParamsDTO.getSourceDir();
            this.destinationDir = compilationTaskParamsDTO.getDestinationDir();
        }
    }

    public TaskDTO createTaskDTO(){
        GraphDTO graphDTO = this.graph.makeDTO(this.taskName);
        TaskDTO taskDTO = new TaskDTO(this.taskName, this.creatorName, this.totalTaskPrice,
                this.registeredWorkers.size(), this.status, graphDTO, TaskType.COMPILATION_TASK);
        return taskDTO;
    }


    public static CompilationTask createCompilationTaskFromDTO(CompilationTaskParamsDTO compilationTaskParamsDTO, Graph graphForTask){
        Set<String> selectedTargets = new HashSet<>(compilationTaskParamsDTO.getTargets());
        Graph graph = Graph.buildGraphForRunning(selectedTargets, graphForTask);
        return new CompilationTask(compilationTaskParamsDTO, graph);
    }

    public CompilationTaskParamsDTO createCompilationTaskParamsDTO(CompilationTask compilationTask){
        return new CompilationTaskParamsDTO(this.sourceDir, this.destinationDir);
    }
}
