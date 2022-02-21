package engine;

import exceptions.*;
import general_enums.Dependency;
import general_enums.RunType;
import general_enums.TaskType;
import graph.Graph;
import schema.generated.GPUPConfiguration;
import schema.generated.GPUPDescriptor;
import target.Target;
import task.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.*;

public class GraphsManager {

    private Map<String, Graph> graphsInSystem = new HashMap<>();

    public Map<String, Graph> getGraphsInSystem() {
        return graphsInSystem;
    }

    public Target getTarget(String name, String graphName) throws TargetNotExistException {
        Graph graph = this.graphsInSystem.get(graphName);
        return graph.getTarget(name);
    }

    public boolean isGraphExistsInSystem(String graphName) throws GraphNotExistException {
        if(!this.graphsInSystem.containsKey(graphName)){
            throw new GraphNotExistException(graphName);
        }
        return true;
    }

    public boolean isTargetsExistsInGraph(List<String> targetsName, String graphName) throws TargetNotExistException {
        for(String currTarget : targetsName){
            checkIfTargetExistInGraph(currTarget, graphName);
        }
        return true;
    }

    private boolean checkIfTargetExistInGraph(String targetName, String graphName) throws TargetNotExistException {
        if(!this.graphsInSystem.get(graphName).getTargetGraph().containsKey(targetName)){
            throw new TargetNotExistException(targetName);
        }
        return true;
    }

    private boolean checkIfValidDependency(Dependency dependency) throws InvalidDependencyException {
        if(!dependency.equals(Dependency.DEPENDS_ON) && !dependency.equals(Dependency.REQUIRED_FOR)){
            throw new InvalidDependencyException(dependency.getDependency());
        }
        return true;
    }

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

    public List<String> findCycle(String targetName, String graphName) throws TargetNotExistException, GraphNotExistException, InvalidDependencyException {
        if(checkFindCycleParamsValidation(targetName, graphName)){
            try {
                Graph graph = this.graphsInSystem.get(graphName);
                if(graph != null){
                    List<Target> lst = Task.topologicalSort(graph);
                }
                return null;
            } catch (CycleException e) {
                Collection<List<String>> cycles = new ArrayList<>();
                cycles = getPaths(targetName, targetName, Dependency.DEPENDS_ON, graphName);
                if (cycles.isEmpty()) {
                    return null;
                } else {
                    return cycles.iterator().next();
                }
            }
        }
        return null;
    }

    private boolean checkFindCycleParamsValidation(String targetName, String graphName) throws TargetNotExistException, GraphNotExistException {
        return isGraphExistsInSystem(graphName) && checkIfTargetExistInGraph(targetName, graphName);
    }


//    public Set<String> whatIfForRunningTask(String targetName, Dependency dependency, TaskType taskType,
//                                            RunType runType, String graphName){
//        Graph graph = this.graphsInSystem.get(graphName);
//        Target target = null;
//
//        Set<String> targetSet = new HashSet<>();
//        if(runType.equals(RunType.INCREMENTAL)){
//            if(this.tasksInSystem.containsKey(taskType)){
//                target = this.tasksInSystem.get(taskType).getGraph().getTarget(targetName);
//            }
//        }
//        else{
//            target = graph.getTarget(targetName);
//        }
//        if(target != null){
//            if(dependency.equals(Dependency.REQUIRED_FOR)){
//                target.getRequiredForAncestors(targetSet);
//            }
//            else{
//                target.getDependsOnAncestors(targetSet);
//            }
//        }
//        return targetSet;
//    }

    public Set<String> whatIf(String targetName, Dependency dependency, String graphName)
            throws GraphNotExistException, InvalidDependencyException, TargetNotExistException {
        Target target = null;
        Set<String> targetSet = new HashSet<>();
        if(checkValidationWhatIfParams(targetName, dependency, graphName)){
            Graph graph = this.graphsInSystem.get(graphName);
            target = graph.getTarget(targetName);
            if(target != null) {
                if (dependency.equals(Dependency.REQUIRED_FOR)) {
                    target.getRequiredForAncestors(targetSet);
                } else {
                    target.getDependsOnAncestors(targetSet);
                }
            }
        }
        return targetSet;
    }

    private boolean checkValidationWhatIfParams(String targetName, Dependency dependency, String graphName)
            throws GraphNotExistException, InvalidDependencyException, TargetNotExistException {

        return isGraphExistsInSystem(graphName) && checkIfTargetExistInGraph(targetName, graphName)
                && checkIfValidDependency(dependency);
    }


    //The function does validation of the targets and then call to find paths
    public boolean checkValidationOfGetPathsParams(String firstTargetName, String secondTargetName,
                                                   Dependency dependency, Graph graph) throws TargetNotExistException, GraphNotExistException, InvalidDependencyException {

        return isGraphExistsInSystem(graph.getName()) && checkIfTargetExistInGraph(firstTargetName, graph.getName()) &&
                checkIfTargetExistInGraph(secondTargetName, graph.getName()) &&
                checkIfValidDependency(dependency);
    }

    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName,
                                             Dependency dependency, String graphName)
            throws TargetNotExistException, GraphNotExistException, InvalidDependencyException {
        List<List<String>> paths = new ArrayList<>();
        Graph graph = this.graphsInSystem.get(graphName);
        if(checkValidationOfGetPathsParams(firstTargetName, secondTargetName, dependency, graph)){
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


    public boolean isCycleInGraph(String graphName) {
        try {
            Graph graph = this.graphsInSystem.get(graphName);
            List<Target> lst = Task.topologicalSort(graph);
            return false;
        } catch (CycleException e) {
            return true;

        }
    }
}
