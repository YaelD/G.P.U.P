package task;

import dto.CompilationTaskParamsDTO;
import dto.TargetDTO;
import dto.TaskParamsDTO;
import graph.Graph;
import graph.SerialSetsContainer;
import target.RunResults;
import target.RunStatus;
import target.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;

public class CompilationTask extends Task{

    final String JAVA_COMPILER = "javac";
    final String SOURCE_DIR_REF_PARAM = "-cp";
    final String DESTINATION_DIR_REF_PARAM = "-d";
    private String sourceDir;
    private String destinationDir;


    public CompilationTask(Graph graph, CompilationTaskParamsDTO compilationTaskDTO, SerialSetsContainer serialSetsContainer) {
        super(graph, serialSetsContainer);
        this.sourceDir = compilationTaskDTO.getSourceDir();
        this.destinationDir = compilationTaskDTO.getDestinationDir();
    }

    @Override
    protected TargetDTO executeTaskOnTarget(Target target) {
        TargetDTO targetDTO = null;
        String runResult = "";
        //LocalTime startTime, endTime;
        try {
            target.setStartingProcessTime(LocalTime.now());
            target.setRunStatus(RunStatus.IN_PROCESS);
            //startTime = LocalTime.now();
            Process process = CompileTarget(target, runResult);
            String processResult = getProcessResult(process);
            int exitCode = process.waitFor();
            target.setEndingProcessTime(LocalTime.now());
            //endTime = LocalTime.now();
            if(exitCode != 0){    //means that the process has failed
                target.setRunResult(RunResults.FAILURE);
                runResult += "\nJavac output: ";
                runResult += processResult;
            }
            else {
                target.setRunResult(RunResults.SUCCESS);
                runResult += processResult;
            }


            target.setRunningTime(Duration.between(target.getStartingProcessTime(), target.getEndingProcessTime()).toMillis());
            target.setRunStatus(RunStatus.FINISHED);
            targetDTO = new TargetDTO(target, runResult);

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

    private Process CompileTarget(Target target, String runResult) throws IOException {

        String filePath = "/" + target.getInfo().replace('.', '/');
        int indexOfLastSlash = filePath.lastIndexOf('/');
        String dirPath = filePath.substring(0,indexOfLastSlash);
        String fileName= filePath.substring(indexOfLastSlash);
        String sourceDir = this.sourceDir + dirPath;
        filePath = sourceDir + fileName + ".java";
        runResult += "Compiled file: " + filePath + "\n Compiler's operating line: "+
                JAVA_COMPILER+ " "+DESTINATION_DIR_REF_PARAM+ " " + this.destinationDir +
                " " + SOURCE_DIR_REF_PARAM+ " " + sourceDir + " " + filePath;
        Process process = new ProcessBuilder(JAVA_COMPILER, DESTINATION_DIR_REF_PARAM, this.destinationDir, SOURCE_DIR_REF_PARAM ,sourceDir, filePath)
                .directory(new File(this.sourceDir))
                .redirectErrorStream(true)
                .start();
        return process;


//        String filePath = "/" + target.getInfo().replace('.', '/');
//        int indexOfLastSlash = filePath.lastIndexOf('/');
//        String dirPath = filePath.substring(0,indexOfLastSlash);
//        String fileName= filePath.substring(indexOfLastSlash);
//        String destinationDir = this.DestinationDir + dirPath;
//        String sourceDir = this.sourceDir + dirPath;
//        filePath = sourceDir + fileName + ".java";
//        Process process = new ProcessBuilder(JAVA_COMPILER, DESTINATION_DIR_REF_PARAM, destinationDir, SOURCE_DIR_REF_PARAM ,sourceDir, filePath)
//                .directory(new File(this.workingDirectory))
//                .redirectErrorStream(true)
//                .start();
//        return process;

    }

    @Override
    public void updateParameters(TaskParamsDTO taskParamsDTO) {
        if(taskParamsDTO instanceof CompilationTaskParamsDTO){
            CompilationTaskParamsDTO compilationTaskParamsDTO = (CompilationTaskParamsDTO) taskParamsDTO;
            this.sourceDir = compilationTaskParamsDTO.getSourceDir();
            this.destinationDir = compilationTaskParamsDTO.getDestinationDir();
        }
    }
}
