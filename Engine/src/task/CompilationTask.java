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
    private String DestinationDir;


    public CompilationTask(Graph graph, CompilationTaskParamsDTO compilationTaskDTO, SerialSetsContainer serialSetsContainer) {
        super(graph, serialSetsContainer);
        this.sourceDir = compilationTaskDTO.getSourceDir();
        this.DestinationDir = compilationTaskDTO.getDestinationDir();
    }

    @Override
    protected TargetDTO executeTaskOnTarget(Target target) {
        TargetDTO targetDTO = null;
        LocalTime startTime, endTime;
        try {
            target.setRunStatus(RunStatus.IN_PROCESS); //todo: block
            startTime = LocalTime.now();
            Process process = CompileTarget(target);
            String processResult = getProcessResult(process);
            int exitCode = process.waitFor();
            endTime = LocalTime.now();
            if(exitCode != 0){    //means that the process has failed
                System.out.println("The compilation of this target has failed");
                target.setRunResult(RunResults.FAILURE);
            }
            else {
                System.out.println("The process ended successfully");
                target.setRunResult(RunResults.SUCCESS);
            }
            System.out.println(processResult);

            //Todo: target.setRunResult(RunResults.WARNING); //todo: block

            target.setRunningTime(Duration.between(startTime, endTime).toMillis()); //todo: block
            target.setRunStatus(RunStatus.FINISHED); //todo: block
            targetDTO = new TargetDTO(target, startTime, endTime);

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

    private Process CompileTarget(Target target) throws IOException {
        String fileName = "./" + target.getInfo().replace('.', '/');
        Process process = new ProcessBuilder(JAVA_COMPILER, DESTINATION_DIR_REF_PARAM, this.DestinationDir, SOURCE_DIR_REF_PARAM ,this.sourceDir, fileName)
                .directory(new File(this.sourceDir))
                .redirectErrorStream(true)
                .start();
        return process;
    }

    @Override
    public void updateParameters(TaskParamsDTO taskParamsDTO) {
        if(taskParamsDTO instanceof CompilationTaskParamsDTO){
            CompilationTaskParamsDTO compilationTaskParamsDTO = (CompilationTaskParamsDTO) taskParamsDTO;
            this.sourceDir = compilationTaskParamsDTO.getSourceDir();
            this.DestinationDir = compilationTaskParamsDTO.getDestinationDir();
        }
    }
}
