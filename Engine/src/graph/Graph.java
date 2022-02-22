package graph;

import dto.GraphDTO;
import dto.TargetDTO;
import engine.ExceptionMessages;
import general_enums.Dependency;
import schema.generated.GPUPTarget;
import schema.generated.GPUPTargetDependencies;
import schema.generated.GPUPTargets;
import general_enums.PlaceInGraph;
import target.Target;
import general_enums.TaskType;

import java.util.*;

public class Graph implements Cloneable {

    private Map<String, Target> targetGraph = new HashMap<>();
    private String name;
    private String creatorName;
    private Map<TaskType, Integer> taskPricePerTarget = new HashMap<>();

    public Graph(Map<String, Target> targetGraph, String name, Map<TaskType, Integer> taskPricePerTarget,
                 String creatorName) {
        this.name = name;
        this.targetGraph = targetGraph;
        this.taskPricePerTarget = taskPricePerTarget;
        this.creatorName = creatorName;
    }


    public String getCreatorName() {
        return creatorName;
    }

    public Map<TaskType, Integer> getTaskPricePerTarget() {
        return taskPricePerTarget;
    }

    @Override
    public Graph clone(){
        try{
            Graph newGraph = (Graph) super.clone();
            newGraph.name = this.name;
            newGraph.targetGraph = new HashMap<>();
            for(Map.Entry<String, Target> entry: this.targetGraph.entrySet()){
                newGraph.targetGraph.put(entry.getKey(), entry.getValue().clone());
            }
            updateGraphTargets(newGraph);
            return newGraph;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static void updateGraphTargets(Graph newGraph){
        for(Target currTarget: newGraph.getTargets()) {
            Set<Target> newRequiredFor = new HashSet<>();
            for (Target requiredTarget : currTarget.getRequiredFor()) {
                if(newGraph.targetGraph.containsKey(requiredTarget.getName())){
                    newRequiredFor.add(newGraph.getTarget(requiredTarget.getName()));
                }
            }
            currTarget.setRequiredFor(newRequiredFor);

            Set<Target> newDependsOn = new HashSet<>();
            for (Target dependTarget : currTarget.getDependsOn()) {
                if(newGraph.targetGraph.containsKey(dependTarget.getName())){
                    newDependsOn.add(newGraph.getTarget(dependTarget.getName()));
                }
            }
            currTarget.setDependsOn(newDependsOn);
        }

    }

    public static Map<String, Target> buildTargetGraph(GPUPTargets gpupTargets) throws Exception{
        Map<String, Target> graph = new HashMap<>();

        for(GPUPTarget gpupTarget : gpupTargets.getGPUPTarget()){
            if(graph.containsKey(gpupTarget.getName())){
                throw new Exception(ExceptionMessages.TARGET + gpupTarget.getName()+ ExceptionMessages.ALREADY_EXIST);
            }
            graph.put(gpupTarget.getName(), new Target(gpupTarget));
        }
        checkDuplicateTargets(gpupTargets, graph);
        addTargetsPlaceInGraph(graph);

        return graph;
    }

    public static Graph buildGraphForRunning(Set<String> targetsForRunning, Graph systemGraph){
        Graph graphForRunning = systemGraph.clone();
        for(String selectedTarget : targetsForRunning){
            for(Target target : systemGraph.getTargets()){
                if(!targetsForRunning.contains(target.getName())){
                    graphForRunning.getTargetGraph().remove(target.getName());
                }
            }
            Set<Target> requiredFor = graphForRunning.getTarget(selectedTarget).getRequiredFor();
            Set<Target> dependsOn = graphForRunning.getTarget(selectedTarget).getDependsOn();
            requiredFor.removeIf(target -> !(targetsForRunning.contains(target.getName())));
            dependsOn.removeIf(target -> !(targetsForRunning.contains(target.getName())));
        }
        addTargetsPlaceInGraph(graphForRunning.getTargetGraph());
        return graphForRunning;
    }

    private static void checkDuplicateTargets(GPUPTargets gpupTargets, Map<String, Target> graph)
            throws Exception{
        for(GPUPTarget gpupTarget : gpupTargets.getGPUPTarget()){
            if(gpupTarget.getGPUPTargetDependencies() != null){
                for(GPUPTargetDependencies.GPUGDependency dependency:
                        gpupTarget.getGPUPTargetDependencies().getGPUGDependency()){
                    if(!graph.containsKey(dependency.getValue())){
                        throw new Exception(ExceptionMessages.TARGET + dependency.getValue() +
                                ExceptionMessages.NOT_EXIST);
                    }
                    Target currTarget = graph.get(gpupTarget.getName());
                    Target checkTarget = graph.get(dependency.getValue());
                    String currDependency = dependency.getType();//requiredFor, DependsOn
                    checkDependencies(currTarget, checkTarget, currDependency);
                }
            }
        }
    }

    private static void checkDependencies(Target currTarget, Target checkTarget, String currDependency)
    throws Exception{
        if(currDependency.equals(Dependency.REQUIRED_FOR.getDependency()))
        {
            if(checkTarget.getRequiredFor().contains(currTarget)) {
                throw new Exception(ExceptionMessages.DEPENDENCY_CONFLICT + currTarget.getName() + ", " +
                        checkTarget.getName());
            }
            currTarget.getRequiredFor().add(checkTarget);
            checkTarget.getDependsOn().add(currTarget);
        }
        else if(currDependency.equals(Dependency.DEPENDS_ON.getDependency())) {
            if(checkTarget.getDependsOn().contains(currTarget)) {
                throw new Exception(ExceptionMessages.DEPENDENCY_CONFLICT + currTarget.getName()+ ", " +
                        checkTarget.getName());
            }
            currTarget.getDependsOn().add(checkTarget);
            checkTarget.getRequiredFor().add(currTarget);
        }
        else
        {
            throw new Exception(ExceptionMessages.DEPENDENCY + currDependency + ExceptionMessages.INVALID);
        }
    }

    private static void addTargetsPlaceInGraph(Map<String, Target> graph){
        for(Target target: graph.values()){
            if(target.getRequiredFor().isEmpty() && target.getDependsOn().isEmpty()) {
                target.setPlace(PlaceInGraph.INDEPENDENT);
            }
            else if(target.getRequiredFor().isEmpty()){
                target.setPlace(PlaceInGraph.ROOT);
            }
            else if(target.getDependsOn().isEmpty()){
                target.setPlace(PlaceInGraph.LEAF);
            }
            else{
                target.setPlace(PlaceInGraph.MIDDLE);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Map<String, Target> getTargetGraph() {
        return targetGraph;
    }

    public Collection<Target> getTargets(){
        return targetGraph.values();
    }

    public Target getTarget(String name){
        return targetGraph.get(name);
    }


    public GraphDTO makeDTO(){
        Map<String, TargetDTO> targetsDTOMap = new HashMap<>();
        int numOfTargets = this.getTargets().size();
        int numOfIndependents = 0, numOfRoots = 0, numOfMiddles = 0, numOfLeaves = 0;
        for(Target target: this.getTargets()){
            targetsDTOMap.put(target.getName(), target.makeDTO());
            switch(target.getPlace()){
                case INDEPENDENT:
                    numOfIndependents++;
                  break;
                case ROOT:
                    numOfRoots++;
                    break;
                case MIDDLE:
                    numOfMiddles++;
                    break;
                case LEAF:
                    numOfLeaves++;
                    break;
            }
        }
        int priceSimulation = this.taskPricePerTarget.containsKey(TaskType.SIMULATION_TASK) ? this.taskPricePerTarget.get(TaskType.SIMULATION_TASK) : 0;
        int priceCompilation = this.taskPricePerTarget.containsKey(TaskType.COMPILATION_TASK) ? this.taskPricePerTarget.get(TaskType.COMPILATION_TASK) : 0;

        return new GraphDTO(this.name, targetsDTOMap, this.creatorName, numOfTargets, numOfLeaves, numOfRoots,
                numOfIndependents, numOfMiddles, priceSimulation, priceCompilation);


    }

    //todo: maybe it will be better if the graph will also contains the number of targets, root, middle..
    public static Graph createGraphFromGraphDTO(GraphDTO graphDTO){
        Map<String, Target> targetGraph = new HashMap<>();
        Map<String, TargetDTO> targetsGraphDTO = graphDTO.getTargets();
        for(TargetDTO targetDTO: targetsGraphDTO.values()){
            Target target = Target.createTargetFromTargetDTO(targetDTO);
            targetGraph.put(targetDTO.getName(), target);
        }
        Map<TaskType, Integer> taskPricePerTarget = new HashMap<>();
        taskPricePerTarget.put(TaskType.SIMULATION_TASK, graphDTO.getPriceOfSimulationTask());
        taskPricePerTarget.put(TaskType.COMPILATION_TASK, graphDTO.getPriceOfCompilationTask());

        return new Graph(targetGraph, graphDTO.getName(), taskPricePerTarget, graphDTO.getCreatorName());
    }
}
