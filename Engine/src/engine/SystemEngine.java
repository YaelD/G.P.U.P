package engine;

import dto.GraphDTO;
import dto.SimulationTaskParamsDTO;
import dto.TargetDTO;
import dto.TaskParamsDTO;
import exceptions.*;
import graph.Dependency;
import graph.Graph;
import schema.generated.GPUPDescriptor;
import target.RunResults;
import target.Target;
import task.SimulationTask;
import task.Task;
import task.TaskType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class SystemEngine implements Engine{

    private Graph graph;
    private Map<TaskType, Task> tasksInSystem = new HashMap<>();
    private Task task;
    private String workingDirectory;
    private boolean isFileLoaded = false;

    @Override
    public boolean readFile(String path) throws
            DuplicateTargetsException, TargetNotExistException, InvalidDependencyException, DependencyConflictException, InvalidFileException{
        try {
            fileValidation(path);
            File file = new File(path.trim());
            JAXBContext jaxbContext = JAXBContext.newInstance(GPUPDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            GPUPDescriptor gpupDescriptor = (GPUPDescriptor) jaxbUnmarshaller.unmarshal(file);
            String graphName = gpupDescriptor.getGPUPConfiguration().getGPUPGraphName();
            Map<String, Target> map = Graph.buildTargetGraph(gpupDescriptor.getGPUPTargets());
            this.graph = new Graph(map, graphName);
            this.workingDirectory = gpupDescriptor.getGPUPConfiguration().getGPUPWorkingDirectory();
            this.tasksInSystem = new HashMap<>();
            this.isFileLoaded = true;
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void fileValidation(String path) throws InvalidFileException, IOException {
        Path directory = Paths.get(path.trim());
        if(!Files.exists(directory)) {
            throw new InvalidFileException(path, "There is no file in this path");
        }
        //System.out.println(Files.probeContentType(directory));
        if(!Files.probeContentType(directory).equals("text/xml")){
            throw new InvalidFileException(path,"The file in the current path is not an XML file");
        }
    }

    @Override
    public List<String> findCycle(String targetName) throws TargetNotExistException{
        try {
            getTarget(targetName);
            List<Target> lst = Task.topologicalSort(this.graph);
            return null;
        } catch (CycleException e) {
            List<List<String>> cycles = new ArrayList<>();
            findPaths(targetName, targetName,
                    Dependency.DEPENDS_ON, cycles);
            if (cycles.isEmpty()) {
                return null;
            } else {
                return cycles.iterator().next();
            }
        }
    }

    @Override
    public GraphDTO getGraphDTO() {
        GraphDTO graphDTO = new GraphDTO(this.graph);
        return graphDTO;
    }

    @Override
    public TargetDTO getTarget(String name) throws TargetNotExistException {
        if(!this.graph.getTargetGraph().containsKey(name)){
            throw new TargetNotExistException(name);
        }
        TargetDTO targetDTO = new TargetDTO(this.graph.getTarget(name));
        return targetDTO;
    }

    /*
    @Override
    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName, String relation) throws TargetNotExistException, InvalidDependencyException {
        Collection<List<String>> paths = new ArrayList<>();
        if(!this.graph.getTargetGraph().containsKey(firstTargetName)){
            throw new TargetNotExistException(firstTargetName);
        }
        if(!this.graph.getTargetGraph().containsKey(secondTargetName)){
            throw new TargetNotExistException(secondTargetName);
        }
        if((!relation.equals(Dependency.REQUIRED_FOR.getDependency()))&&(!relation.equals(Dependency.DEPENDS_ON.getDependency()))){
            throw new InvalidDependencyException(relation);
        }
        findPaths(firstTargetName, secondTargetName, relation, paths);
        return paths;
    }


     */

    @Override
    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName, Dependency dependency) throws TargetNotExistException, InvalidDependencyException {
        List<List<String>> paths = new ArrayList<>();
        if(!this.graph.getTargetGraph().containsKey(firstTargetName)){
            throw new TargetNotExistException(firstTargetName);
        }
        if(!this.graph.getTargetGraph().containsKey(secondTargetName)){
            throw new TargetNotExistException(secondTargetName);
        }
        findPaths(firstTargetName, secondTargetName, dependency, paths);
        return paths;

    }

    private void findPaths(String currTargetName, String destinationTargetName, Dependency dependency, List<List<String>> paths) {
        Set<Target> dependencies = this.graph.getTarget(currTargetName).getDependencies(dependency);

        if(dependencies.isEmpty()){
            return;
        }
        else{
            for(Target target: dependencies){
                if(target.getName().equals(destinationTargetName)){
                    List<String> path = new ArrayList<>();
                    path.add(0,target.getName());
                    path.add(0,currTargetName);
                    paths.add(path);
                } else{
                    int currPathsSize = paths.size();
                    findPaths(target.getName(), destinationTargetName, dependency, paths);
                    int newSize = paths.size();
                    for(int i = currPathsSize;i< newSize; ++i){
                        paths.get(i).add(0,currTargetName);
                    }
//                    if(!paths.isEmpty()){
//                        for(List<String> path : paths){
//                            if(!path.get(0).equals(currTargetName)){
//                                path.add(0,currTargetName);
//                            }
//                        }
//                    }
                }
            }
        }
    }


    /*
        private void findPaths(String currTargetName, String destinationTargetName, String relation, Collection<List<String>> paths) {
        Set<Target> dependencies = this.graph.getTarget(currTargetName).getDependencies(relation);

        if(dependencies.isEmpty()){
            return;
        }
        else{
            for(Target target: dependencies){
                if(target.getName().equals(destinationTargetName)){
                    List<String> path = new ArrayList<>();
                    path.add(0,target.getName());
                    path.add(0,currTargetName);
                    paths.add(path);
                } else{
                    findPaths(target.getName(), destinationTargetName, relation, paths);
                    if(!paths.isEmpty()){
                        for(List<String> path : paths){
                            if(!path.get(0).equals(currTargetName)){
                                path.add(0,currTargetName);
                            }
                        }
                    }
                }
            }
        }
    }

     */

    @Override
    public GraphDTO activateTask(Consumer<TargetDTO> consumerString, TaskParamsDTO taskParams, TaskType taskType, boolean isIncremental) {
        List<Consumer<TargetDTO>> outputConsumers = new ArrayList<>();

        if(this.tasksInSystem.containsKey(taskType)){
            this.tasksInSystem.get(taskType).updateParameters(taskParams);
            if(isIncremental){
                if(this.tasksInSystem.get(taskType).getGraph().getTargets().isEmpty()){
                    this.tasksInSystem.get(taskType).setGraph(this.graph.clone());
                }
            }
            else{
                this.tasksInSystem.get(taskType).setGraph(this.graph.clone());
            }
        }
        else{
            switch (taskType){
                case SIMULATION_TASK:
                    if(taskParams instanceof SimulationTaskParamsDTO){
                        this.tasksInSystem.put(taskType, new SimulationTask(this.graph.clone(), (SimulationTaskParamsDTO) taskParams));
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
            runResult = this.tasksInSystem.get(taskType).executeTaskOnGraph(outputConsumers);
            return runResult;
        } catch (CycleException e) {
            return null;
        }
    }

    private String openDirectoryAndFiles(TaskType taskType) {
        StringBuffer stringBuffer = new StringBuffer();
        Date now = new Date();
        //File workDirectory = new File(this.workingDirectory);
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


    private void writeToFile(TargetDTO targetDTO, String path) {
        Writer out = null;
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(path+"\\" + targetDTO.getName() + ".log")));
            out.write("Target name: " + targetDTO.getName()+ "\n"); ;
            out.write("Process result: " + targetDTO.getRunResult().getStatus() + "\n");
            if(targetDTO.getInfo() != null){
                out.write("Target info:" + targetDTO.getInfo() + "\n");
            }
            if(!targetDTO.getRunResult().equals(RunResults.SKIPPED)){
                out.write("Process Start time:" + targetDTO.getStartingTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
                out.write("Process End time:" + targetDTO.getEndingTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
                if(!targetDTO.getTargetsThatCanBeRun().isEmpty()){
                    out.write("The dependent Targets that were opened:\n" + targetDTO.getTargetsThatCanBeRun() + "\n");
                }
                if(targetDTO.getRunResult().equals(RunResults.FAILURE)){
                    if(!targetDTO.getSkippedFathers().isEmpty()){
                        out.write("The targets that won't be able to process are: \n" + targetDTO.getSkippedFathers() + "\n");
                    }
                }
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

    @Override
    public boolean isFileLoaded() {
        return this.isFileLoaded;
    }

    @Override
    public boolean isRunInIncrementalMode(TaskType taskType) {
        if(!this.tasksInSystem.containsKey(taskType)){
            return false;
        }
        if(this.tasksInSystem.get(taskType).getGraph().getTargets().isEmpty()){
            return false;
        }

        return true;
    }

    @Override
    public boolean isCycleInGraph() {
        try {
            List<Target> lst = Task.topologicalSort(graph);
            return false;
        } catch (CycleException e) {
            return true;

        }
    }

    @Override
    public String toString() {
        return "engine.SystemEngine{" +
                "graph=" + graph +
                '}';
    }
}
