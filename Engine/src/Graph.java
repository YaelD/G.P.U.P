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

    public Map<String, Target> buildTargetGraph(GPUPTargets gpupTargets) throws Exception{
        Map<String, Target> graph = new HashMap<>();

        for(GPUPTarget gpupTarget : gpupTargets.getGPUPTarget()){
            if(graph.containsKey(gpupTarget.getName())){
                throw new Exception("Duplicate Target's name");
            }
            graph.put(gpupTarget.getName(), new Target(gpupTarget));
        }
        for(GPUPTarget gpupTarget : gpupTargets.getGPUPTarget()){
            for(GPUPTargetDependencies.GPUGDependency dependency:
                    gpupTarget.getGPUPTargetDependencies().getGPUGDependency()){
                if(!graph.containsKey(dependency.getValue())){
                    throw new Exception("Target is not exist in graph");
                }
                Target currTarget = graph.get(gpupTarget.getName());
                Target checkTarget = graph.get(dependency.getValue());
                String currDependency = dependency.getType();//requiredFor, DependsOn
                if(currDependency.equals("requiredFor"))
                {
                    if(currTarget.getRequiredFor().contains(checkTarget))
                    {
                        throw new Exception("Dependencies collusion");
                    }
                    currTarget.getRequiredFor().add(checkTarget);
                }
                else if(currDependency.equals("dependsOn"))
                {
                    if(currTarget.getDependsOn().contains(checkTarget))
                    {
                        throw new Exception("Dependencies collusion");
                    }
                    currTarget.getDependsOn().add(checkTarget);
                }
                else
                {
                    throw new Exception("Dependency type is not supported");
                }
            }
        }


        return graph;
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
