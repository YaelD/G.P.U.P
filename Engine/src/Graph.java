import exceptions.DependencyConflictException;
import exceptions.DuplicateTargetsException;
import exceptions.InvalidDependencyException;
import exceptions.TargetNotExistException;
import schema.generated.GPUPTarget;
import schema.generated.GPUPTargetDependencies;
import schema.generated.GPUPTargets;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Graph {

    private Map<String, Target> targetGraph = new HashMap<>();
    private String name;

    public Graph(Map<String, Target> targetGraph, String name) {
        this.name = name;
        this.targetGraph = targetGraph;
    }

    public static Map<String, Target> buildTargetGraph(GPUPTargets gpupTargets)
            throws TargetNotExistException, DependencyConflictException, InvalidDependencyException, DuplicateTargetsException {
        Map<String, Target> graph = new HashMap<>();

        for(GPUPTarget gpupTarget : gpupTargets.getGPUPTarget()){
            if(graph.containsKey(gpupTarget.getName())){
                throw new DuplicateTargetsException(gpupTarget.getName());
            }
            graph.put(gpupTarget.getName(), new Target(gpupTarget));
        }
        checkDuplicateTargets(gpupTargets, graph);
        addTargetsPlaceInGraph(graph);

        return graph;
    }

    private static void checkDuplicateTargets(GPUPTargets gpupTargets, Map<String, Target> graph)
            throws TargetNotExistException, DependencyConflictException, InvalidDependencyException {
        for(GPUPTarget gpupTarget : gpupTargets.getGPUPTarget()){
            for(GPUPTargetDependencies.GPUGDependency dependency:
                    gpupTarget.getGPUPTargetDependencies().getGPUGDependency()){
                if(!graph.containsKey(dependency.getValue())){
                    throw new TargetNotExistException(dependency.getValue());
                }
                Target currTarget = graph.get(gpupTarget.getName());
                Target checkTarget = graph.get(dependency.getValue());
                String currDependency = dependency.getType();//requiredFor, DependsOn
                checkDependencies(currTarget, checkTarget, currDependency);
            }
        }
    }

    //TODO: add place in graph
    private static void checkDependencies(Target currTarget, Target checkTarget, String currDependency)
    throws DependencyConflictException, InvalidDependencyException{
        if(currDependency.equals(Dependency.REQUIRED_FOR.getDependency()))
        {
            if(currTarget.getRequiredFor().contains(checkTarget))
            {
                throw new DependencyConflictException(currTarget.getName(), checkTarget.getName(),currDependency);
            }
            currTarget.getRequiredFor().add(checkTarget);
        }
        else if(currDependency.equals(Dependency.DEPENDS_ON.getDependency()))
        {
            if(currTarget.getDependsOn().contains(checkTarget))
            {
                throw new DependencyConflictException(currTarget.getName(), checkTarget.getName(),currDependency);
            }
            currTarget.getDependsOn().add(checkTarget);
        }
        else
        {
            throw new InvalidDependencyException(currDependency);
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

    public Collection<Target> getTargets(){
        return targetGraph.values();
    }

    public Target getTarget(String name){
        return targetGraph.get(name);
    }
}
