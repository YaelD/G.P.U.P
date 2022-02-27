package worker_engine;

import dto.CompilationTaskParamsDTO;
import dto.TargetDTO;
import general_enums.RunResults;
import general_enums.RunStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CompilationTaskExecution extends TaskExecution implements Runnable{

    private final String JAVA_COMPILER = "javac";
    private final String SOURCE_DIR_REF_PARAM = "-cp";
    private final String DESTINATION_DIR_REF_PARAM = "-d";

    private String sourceDir;
    private String destinationDir;
    //private ExecutionTarget executionTarget;


    public CompilationTaskExecution(CompilationTaskParamsDTO taskParamsDTO, TargetDTO targetDTO){
        super(targetDTO);
        this.sourceDir = taskParamsDTO.getSourceDir();
        this.destinationDir = taskParamsDTO.getDestinationDir();
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
    private String buildPaths(ExecutionTarget target){
        String filePath = "/" + target.getInfo().replace('.', '/');
        int indexOfLastSlash = filePath.lastIndexOf('/');
        String dirPath = filePath.substring(0,indexOfLastSlash);
        String localSourceDir = this.sourceDir + dirPath;
        return localSourceDir;
    }

    private Process CompileTarget(ExecutionTarget target, String localSourceDir, String filePath) throws IOException {
        target.setSpecificTaskLog("Start compilation time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm:ss")));
        Process process = new ProcessBuilder(JAVA_COMPILER, DESTINATION_DIR_REF_PARAM, this.destinationDir,
                SOURCE_DIR_REF_PARAM ,localSourceDir, filePath)
                .directory(new File(this.sourceDir))
                .redirectErrorStream(true)
                .start();
        target.setSpecificTaskLog("End compilation time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm:ss")));
        return process;

    }


    @Override
    public void run() {
        LocalTime startTime, endTime;
        try {
            executionTarget.setSpecificTaskLog("Compilation task: ");
            startTime = LocalTime.now();
            this.sendTarget();
            executionTarget.setSpecificTaskLog("Start time: " + startTime.format(DateTimeFormatter.ofPattern("H:mm:ss")));
            executionTarget.setRunStatus(RunStatus.IN_PROCESS);
            String localSourceDir = buildPaths(executionTarget);
            String filePath = this.sourceDir+"/" + executionTarget.getInfo().replace('.', '/') +".java";
            Process process = CompileTarget(executionTarget, localSourceDir, filePath);
            String processResult = getProcessResult(process);
            int exitCode = process.waitFor();
            endTime = LocalTime.now();
            executionTarget.setSpecificTaskLog("End time: " + endTime.format(DateTimeFormatter.ofPattern("H:mm:ss")));
            if(exitCode != 0){    //means that the process has failed
                executionTarget.setRunResult(RunResults.FAILURE);
                executionTarget.setSpecificTaskLog("Javac output: " + processResult);
            }
            else {
                executionTarget.setRunResult(RunResults.SUCCESS);
                executionTarget.setSpecificTaskLog( "Compilation run result: "+processResult);
            }

            executionTarget.setSpecificTaskLog("Running time: " + Duration.between(startTime, endTime).toMillis() + "Milliseconds");
            executionTarget.setRunStatus(RunStatus.FINISHED);
            this.sendTarget();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }
}
