package task;

import dto.*;
import engine.SystemEngine;
import general_enums.TaskType;
import graph.Graph;

import java.util.HashSet;
import java.util.Set;

public class CompilationTask extends Task{

    private final String JAVA_COMPILER = "javac";
    private final String SOURCE_DIR_REF_PARAM = "-cp";
    private final String DESTINATION_DIR_REF_PARAM = "-d";

    private String sourceDir;
    private String destinationDir;

    public CompilationTask(Graph graph, String creatorName, String taskName, int pricePerTarget) {
        super(graph, creatorName, taskName, pricePerTarget);
    }

    public CompilationTask(CompilationTaskParamsDTO compilationTaskParamsDTO, Graph graph) {
        super(graph, compilationTaskParamsDTO.getCreatorName(),
                compilationTaskParamsDTO.getTaskName(), compilationTaskParamsDTO.getTotalTaskPrice());
        this.destinationDir = compilationTaskParamsDTO.getDestinationDir();
        this.sourceDir = compilationTaskParamsDTO.getSourceDir();
    }


//    public CompilationTask(Graph graph, CompilationTaskParamsDTO compilationTaskDTO, SerialSetsContainer serialSetsContainer) {
//        super(graph, "", "");
//        this.sourceDir = compilationTaskDTO.getSourceDir();
//        this.destinationDir = compilationTaskDTO.getDestinationDir();
//    }
/*


    @Override
    protected TargetDTO executeTaskOnTarget(Target target) {
        TargetDTO targetDTO = null;
        try {
            target.setStartingProcessTime(LocalTime.now());
            target.setRunStatus(RunStatus.IN_PROCESS);
            String localSourceDir = buildPaths(target);
            Process process = CompileTarget(target, localSourceDir);
            String processResult = getProcessResult(process);
            int exitCode = process.waitFor();
            target.setEndingProcessTime(LocalTime.now());
            if(exitCode != 0){    //means that the process has failed
                target.setRunResult(RunResults.FAILURE);
                target.setCompilationRunResult("\nJavac output: " + processResult);
            }
            else {
                target.setRunResult(RunResults.SUCCESS);
                target.setCompilationRunResult(processResult);
            }

            target.setRunningTime(Duration.between(target.getStartingProcessTime(), target.getEndingProcessTime()).toMillis());
            target.setRunStatus(RunStatus.FINISHED);
            targetDTO = new TargetDTO(target);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return targetDTO;
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
    //private Process CompileTarget(Target target, String runResult) throws IOException {

    private String buildPaths(Target target){
        String filePath = "/" + target.getInfo().replace('.', '/');
        int indexOfLastSlash = filePath.lastIndexOf('/');
        String dirPath = filePath.substring(0,indexOfLastSlash);
        String fileName= filePath.substring(indexOfLastSlash);
        String localSourceDir = this.sourceDir + dirPath;
//        filePath = localSourceDir +  fileName + ".java";
//        target.setCompilationFileName(filePath);
//        target.setCompilerOperatingLine("Compiler's operating line: "+
//        JAVA_COMPILER+ " "+DESTINATION_DIR_REF_PARAM+ " " + this.destinationDir +
//                " " + SOURCE_DIR_REF_PARAM+ " " + localSourceDir + " " + filePath);
        return localSourceDir;
    }

    private Process CompileTarget(Target target, String localSourceDir) throws IOException {
        target.setStartingCompileTime(LocalTime.now());
        Process process = new ProcessBuilder(JAVA_COMPILER, DESTINATION_DIR_REF_PARAM, this.destinationDir,
                SOURCE_DIR_REF_PARAM ,localSourceDir, target.getCompilationFileName())
                .directory(new File(this.sourceDir))
                .redirectErrorStream(true)
                .start();
        target.setEndingCompileTime(LocalTime.now());
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
 */

    public TaskDTO createTaskDTO(){
        GraphDTO graphDTO = this.graph.makeDTO();
        TaskDTO taskDTO = new TaskDTO(this.taskName, this.creatorName, this.totalTaskPrice,
                this.registeredWorkers.size(), this.status, graphDTO, TaskType.COMPILATION_TASK);
        return taskDTO;
    }


    public static CompilationTask createCompilationTaskFromDTO(CompilationTaskParamsDTO compilationTaskParamsDTO, Graph graphForTask){
        Set<String> selectedTargets = new HashSet<>(compilationTaskParamsDTO.getTargets());
        Graph graph = Graph.buildGraphForRunning(selectedTargets, graphForTask);
        return new CompilationTask(compilationTaskParamsDTO, graph);
    }
}
