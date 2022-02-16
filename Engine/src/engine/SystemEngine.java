package engine;

import exceptions.*;
import graph.Dependency;
import graph.Graph;
import schema.generated.GPUPConfiguration;
import schema.generated.GPUPDescriptor;
import target.Target;
import task.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class SystemEngine implements Engine{

    private final String workingDirectory = "c:\\gpup-working-dir";
    //private static SystemEngine systemEngine = null;

    //members
    private Map<String, Task> tasksInSystem = new HashMap<>();
    private Map<String, Graph> graphsInSystem = new HashMap<>();


    //C'tor + creating working dir
    public SystemEngine() {
        File directory = new File(this.workingDirectory);
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    //Get the instance of the singelton
//    public static Engine getInstance(){
//        if (systemEngine == null)
//            systemEngine = new SystemEngine();
//
//        return systemEngine;
//    }


    public Map<String, Task> getTasksInSystem() {
        return tasksInSystem;
    }

    public Map<String, Graph> getGraphsInSystem() {
        return graphsInSystem;
    }

    @Override
    public Target getTarget(String name, String graphName) throws TargetNotExistException {
        Graph graph = this.graphsInSystem.get(graphName);
        return graph.getTarget(name);
    }


    public boolean isGraphExistsInSystem(String graphName) {
        return (this.graphsInSystem.containsKey(graphName));
    }


    @Override
    public boolean loadFile(InputStream stream, String creatorName) throws
            DuplicateTargetsException, TargetNotExistException, InvalidDependencyException, DependencyConflictException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GPUPDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            GPUPDescriptor gpupDescriptor = (GPUPDescriptor) jaxbUnmarshaller.unmarshal(stream);
            initializeGraphSystem(gpupDescriptor, creatorName);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return false;
    }
//    public boolean loadFile(String path) throws
//            DuplicateTargetsException, TargetNotExistException, InvalidDependencyException, DependencyConflictException, InvalidFileException, SerialSetException, DupSerialSetsNameException {
//        try {
//            InputStream stream;
//            fileValidation(path);
//            File file = new File(path.trim());
//            JAXBContext jaxbContext = JAXBContext.newInstance(GPUPDescriptor.class);
//            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//
//            GPUPDescriptor gpupDescriptor = (GPUPDescriptor) jaxbUnmarshaller.unmarshal(file);
//            initializeSystem(gpupDescriptor);
//
//        } catch (JAXBException | IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    private void initializeGraphSystem(GPUPDescriptor gpupDescriptor, String creatorName) throws DuplicateTargetsException,
            TargetNotExistException, InvalidDependencyException, DependencyConflictException {
        Map<String, Target> map = Graph.buildTargetGraph(gpupDescriptor.getGPUPTargets());
        String graphName = gpupDescriptor.getGPUPConfiguration().getGPUPGraphName();
        Map<TaskType, Integer> taskPricePerTarget = new HashMap<>();
        for(GPUPConfiguration.GPUPPricing.GPUPTask currTask :gpupDescriptor.getGPUPConfiguration().getGPUPPricing().getGPUPTask()) {
            switch (currTask.getName()) {
                case "Simulation":
                    taskPricePerTarget.put(TaskType.SIMULATION_TASK, currTask.getPricePerTarget());
                    break;
                case "Compilation":
                    taskPricePerTarget.put(TaskType.COMPILATION_TASK, currTask.getPricePerTarget());
                    break;

            }
        }

        if(this.graphsInSystem.containsKey(graphName)){
            //TODO: THROW AN EXCEPTION
        }
        this.graphsInSystem.put(graphName, new Graph(map, graphName, taskPricePerTarget, creatorName));
//        for(Target target : this.graph.getTargets()){
//            target.updateWaitForTheseTargetsToBeFinished();
//        }
    }


//    private void fileValidation(String path) throws InvalidFileException, IOException {
//        Path directory = Paths.get(path.trim());
//        if(!Files.exists(directory)) {
//            throw new InvalidFileException(path, "There is no file in this path");
//        }
//        if(Files.probeContentType(directory) == null){
//            throw new InvalidFileException(path, "The file type is not recognized");
//        }
//        if(!Files.probeContentType(directory).equals("text/xml")){
//            throw new InvalidFileException(path,"The file in the current path is not an XML file");
//        }
//    }
/*

 */
    @Override
    public List<String> findCycle(String targetName, String graphName) throws TargetNotExistException{
        try {
            Graph graph = this.graphsInSystem.get(graphName);
            if(graph != null){
                List<Target> lst = Task.topologicalSort(graph);
            }
            return null;
        } catch (CycleException e) {
            Collection<List<String>> cycles = new ArrayList<>();
            cycles = getPaths(targetName, targetName, Dependency.DEPENDS_ON,graphName );
            if (cycles.isEmpty()) {
                return null;
            } else {
                return cycles.iterator().next();
            }
        }
    }


    @Override
    public Set<String> whatIfForRunningTask(String targetName, Dependency dependency, TaskType taskType,
                                            RunType runType, String graphName){
        Graph graph = this.graphsInSystem.get(graphName);
        Target target = null;

        Set<String> targetSet = new HashSet<>();
        if(runType.equals(RunType.INCREMENTAL)){
            if(this.tasksInSystem.containsKey(taskType)){
                target = this.tasksInSystem.get(taskType).getGraph().getTarget(targetName);
            }
        }
        else{
            target = graph.getTarget(targetName);
        }
        if(target != null){
            if(dependency.equals(Dependency.REQUIRED_FOR)){
                target.getRequiredForAncestors(targetSet);
            }
            else{
                target.getDependsOnAncestors(targetSet);
            }
        }
        return targetSet;
    }

    public Set<String> whatIf(String targetName, Dependency dependency, String graphName){
        Target target = null;
        Graph graph = this.graphsInSystem.get(graphName);
        target = graph.getTarget(targetName);
        Set<String> targetSet = new HashSet<>();
        if(target != null) {
            if (dependency.equals(Dependency.REQUIRED_FOR)) {
                target.getRequiredForAncestors(targetSet);
            } else {
                target.getDependsOnAncestors(targetSet);
            }
        }
        return targetSet;
    }


    //The function does validation og the targets and then call to find paths
    public boolean checkTargetsValidation(String firstTargetName, String secondTargetName,
                                          Graph graph) throws TargetNotExistException {

        if(!graph.getTargetGraph().containsKey(firstTargetName)){
            throw new TargetNotExistException(firstTargetName);
        }
        if(!graph.getTargetGraph().containsKey(secondTargetName)){
            throw new TargetNotExistException(secondTargetName);
        }
        return true;
    }

    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName,
                                              Dependency dependency, String graphName) throws TargetNotExistException {
        List<List<String>> paths = new ArrayList<>();
        Graph graph = this.graphsInSystem.get(graphName);
        if(checkTargetsValidation(firstTargetName, secondTargetName, graph)){
            Set<String> visitedTargets = new HashSet<>();
            List<String> currPath = new ArrayList<>();
            findPaths(firstTargetName, secondTargetName, dependency, paths, currPath, visitedTargets, graph);
            if(!paths.isEmpty()){
                for(List<String> path : paths){
                    path.add(0, firstTargetName);
                }
            }
        }
        return paths;
    }

    //Finding paths in graph by DFS
    private void findPaths(String currTargetName, String destinationTargetName, Dependency dependency,
                           List<List<String>> paths, List<String> currentPath, Set<String> visitedTargets, Graph graph) {
        Set<Target> dependencies = graph.getTarget(currTargetName).getDependencies(dependency);

        if(dependencies.isEmpty()){
            return;
        }
        else{
            visitedTargets.add(currTargetName);
            for(Target target : dependencies){
                if(target.getName().equals(destinationTargetName)){
                    currentPath.add(target.getName());
                    List<String> pathToInsert = new ArrayList<>();
                    pathToInsert.addAll(currentPath);
                    paths.add(pathToInsert);
                    currentPath.remove(target.getName());
                }
                else{
                    if(!visitedTargets.contains(target.getName())) {
                        currentPath.add(target.getName());
                        findPaths(target.getName(), destinationTargetName, dependency, paths,
                                currentPath, visitedTargets, graph);
                        currentPath.remove(target.getName());
                    }
                }
            }
            visitedTargets.remove(currTargetName);
        }
    }



    /*
        @Override

    public GraphDTO activateTask(Consumer<TargetDTO> consumerString, Consumer<PausableThreadPoolExecutor> threadPoolConsumer,
                                 TaskParamsDTO taskParams, TaskType taskType, boolean isIncremental,
                                 int threadNumber, Set<String> selectedTargets) {

        List<Consumer<TargetDTO>> outputConsumers = new ArrayList<>();
        this.graphForRunning = Graph.buildGraphForRunning(selectedTargets, this.graph);

        if(taskType.equals(TaskType.COMPILATION_TASK)){
            for(Target target : this.graphForRunning.getTargets()){
                String filePath = "/" + target.getInfo().replace('.', '/');
                int indexOfLastSlash = filePath.lastIndexOf('/');
                String dirPath = filePath.substring(0,indexOfLastSlash);
                String fileName= filePath.substring(indexOfLastSlash);
                CompilationTaskParamsDTO compilationTaskParamsDTO = (CompilationTaskParamsDTO)taskParams;
                String localSourceDir = compilationTaskParamsDTO.getSourceDir() + dirPath;
                filePath = localSourceDir +  fileName + ".java";
                target.setCompilationFileName(filePath);
                target.setCompilerOperatingLine("Compiler's operating line: "+
                        "javac"+ " "+"-d"+ " " + compilationTaskParamsDTO.getDestinationDir() +
                        " " + "-cp"+ " " + localSourceDir + " " + filePath);
            }
        }
        if(this.tasksInSystem.containsKey(taskType)){
            this.tasksInSystem.get(taskType).setGraph(this.graphForRunning);
            this.tasksInSystem.get(taskType).updateParameters(taskParams);
        }
        else{
            switch (taskType){
                case SIMULATION_TASK:
                    if(taskParams instanceof SimulationTaskParamsDTO){
                        this.tasksInSystem.put(taskType, new SimulationTask(this.graphForRunning, (SimulationTaskParamsDTO) taskParams,this.serialSetsContainer));
                    }
                    break;
                case COMPILATION_TASK:
                    if(taskParams instanceof CompilationTaskParamsDTO){
                        this.tasksInSystem.put(taskType, new CompilationTask(this.graphForRunning, (CompilationTaskParamsDTO) taskParams,this.serialSetsContainer));
                    }
                    break;
            }
        }
        outputConsumers.add(consumerString);
        String path = openDirectoryAndFiles(taskType);
        Consumer<TargetDTO> fileWriterConsumer = targetDTO -> { writeToFile(targetDTO, path); };
        outputConsumers.add(fileWriterConsumer);
        GraphDTO runResult = null;

        try {
            runResult = this.tasksInSystem.get(taskType).executeTaskOnGraph(outputConsumers, threadPoolConsumer, threadNumber);
            return runResult;
        } catch (CycleException e) {
            return null;
        }
    }


     */

    private String openDirectoryAndFiles(TaskType taskType) {
        StringBuffer stringBuffer = new StringBuffer();
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        simpleDateFormat.format(now, stringBuffer, new FieldPosition(0));
        String path = this.workingDirectory+ "\\" + taskType.getTaskType() + "-" +
                simpleDateFormat.format(now);
        File directory = new File(path);
        if(!directory.exists()){
            directory.mkdirs();
        }
        return path;
    }

/*
    private void writeToFile(TargetDTO targetDTO, String path) {
        Writer out = null;
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(path+"\\" + targetDTO.getName() + ".log")));
            out.write("Target name: " + targetDTO.getName()+ "\n"); ;
            out.write("Process result: " + targetDTO.getRunResult().getStatus() + "\n");
            if(!targetDTO.getSerialSetNames().isEmpty()){
                out.write("SerialSets: " + targetDTO.getSerialSetNames() + "\n") ;
            }
            if(targetDTO.getInfo() != null){
                out.write("Target info:" + targetDTO.getInfo() + "\n");
            }
            if(!targetDTO.getCompilationFileName().equals("")){
                out.write("Compilation file name: " + targetDTO.getCompilationFileName() + "\n");
            }
            if(!targetDTO.getCompilerOperatingLine().equals("")){
                out.write("Compilation operation line: " + targetDTO.getCompilerOperatingLine()+ "\n");

            }
            if(!targetDTO.getRunResult().equals(RunResults.SKIPPED)){
                out.write("Process Start time:" + targetDTO.getStartingProcessTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
                out.write("Process End time:" + targetDTO.getEndingProcessTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
                if(!targetDTO.getTargetsThatCanBeRun().isEmpty()){
                    out.write("The dependent Targets that were opened:\n" + targetDTO.getTargetsThatCanBeRun() + "\n");
                }
                if(targetDTO.getRunResult().equals(RunResults.FAILURE)){
                    if(!targetDTO.getSkippedFathers().isEmpty()){
                        out.write("The targets that won't be able to process are: \n" + targetDTO.getSkippedFathers() + "\n");
                    }
                    if(!targetDTO.getCompilationRunResult().equals("")){
                        out.write("The Run result: \n" + targetDTO.getCompilationRunResult());
                    }
                }
                if(targetDTO.getTaskRunResult() != null && !targetDTO.getTaskRunResult().isEmpty()){
                    out.write("Task Run results: " + targetDTO.getTaskRunResult() + "\n");
                }
            }
            else{
                out.write("The target was skipped because of: " + targetDTO.getFailedChildTargets());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored){
                }
            }
        }
    }

 */

    @Override
    public boolean isRunInIncrementalMode(TaskType taskType, Set<String> selectedTargets) {
        if(!this.tasksInSystem.containsKey(taskType)){
            return false;
        }
        if(this.tasksInSystem.get(taskType).getGraph().getTargets().isEmpty()){
            return false;
        }
        for(String target : selectedTargets){
            if(this.tasksInSystem.get(taskType).getGraph().getTarget(target) == null){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isCycleInGraph(String graphName) {
        try {
            Graph graph = this.graphsInSystem.get(graphName);
            List<Target> lst = Task.topologicalSort(graph);
            return false;
        } catch (CycleException e) {
            return true;

        }
    }

//    @Override
//    public TargetDTO getRunningTarget(String targetName) {
//        TargetDTO targetDTO = null;
//        if(this.graphForRunning != null){
//            if(graphForRunning.getTarget(targetName) != null){
//                targetDTO = new TargetDTO(graphForRunning.getTarget(targetName));
//                targetDTO.updateRunningTargetStatus(targetDTO.getRunStatus());
//            }
//        }
//        return targetDTO;
//    }

    @Override
    public Set<String> getTaskGraphInSystem(TaskType taskType) {
        Set<String> targetNamesInTaskGraph = new HashSet<>();
        if(this.tasksInSystem.containsKey(taskType)){
            for(Target target : this.tasksInSystem.get(taskType).getGraph().getTargets()){
                targetNamesInTaskGraph.add(target.getName());
            }
        }
        return targetNamesInTaskGraph;
    }


}
