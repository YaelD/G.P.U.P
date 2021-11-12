import exceptions.*;
import schema.generated.GPUPDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

public class SystemEngine implements Engine{

    private Graph graph;
    private String workingDirectory;
    private boolean isFileLoaded = false;

    @Override
    public boolean readFile(String path) throws DuplicateTargetsException, TargetNotExistException, InvalidDependencyException, DependencyConflictException {
        try {
            //todo: check the path
            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(GPUPDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            GPUPDescriptor gpupDescriptor = (GPUPDescriptor) jaxbUnmarshaller.unmarshal(file);
            String graphName = gpupDescriptor.getGPUPConfiguration().getGPUPGraphName();
            Map<String, Target> map = Graph.buildTargetGraph(gpupDescriptor.getGPUPTargets());
            this.graph = new Graph(map, graphName);
            this.workingDirectory = gpupDescriptor.getGPUPConfiguration().getGPUPWorkingDirectory();
            this.isFileLoaded = true;

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public GraphDTO getGraphDTO() throws NoFileInSystemException {
        if(!this.isFileLoaded){
            throw new NoFileInSystemException();
        }
        return null;
    }

    @Override
    public TargetDTO getTarget(String name) throws TargetNotExistException, NoFileInSystemException {
        if(!this.isFileLoaded){
            throw new NoFileInSystemException();
        }
        return null;
    }

    @Override
    public Collection<List<TargetDTO>> getPaths(String firstTargetName, String secondTargetName, String relation) throws NoFileInSystemException, TargetNotExistException, InvalidDependencyException {
        if(!this.isFileLoaded){
            throw new NoFileInSystemException();
        }
        else{
            if(!this.graph.getTargetGraph().containsKey(firstTargetName)){
                throw new TargetNotExistException(firstTargetName);
            }
            if(!this.graph.getTargetGraph().containsKey(secondTargetName)){
                throw new TargetNotExistException(secondTargetName);
            }
            if((!relation.equals("requiredFor"))&&(!relation.equals("dependsOn"))){
                throw new InvalidDependencyException(relation);
            }
            Collection<List<String>> paths = new ArrayList<>();
            findPaths(firstTargetName, secondTargetName, relation, paths);
        }
        return null;
    }

    private void findPaths(String currTargetName, String destinationTargetName, String relation, Collection<List<String>> paths) {

        /*

        Set<Target> dependencies = this.graph.getTarget(firstTargetName).getDependencies(relation);
        if(dependencies.isEmpty()){
            return;
        }
        else{
            for(Target target: dependencies){
                if(target.getName().equals(secondTargetName)){
                    List<String> path = new ArrayList<>();
                    path.add(target.getName());
                    paths.add(path);
                    return;
                }
                else{
                    findPaths(target.getName(), secondTargetName, relation, paths);
                }
            }
        }
         */
    }

    @Override
    public GraphDTO activateTask() throws NoFileInSystemException {
        if(!this.isFileLoaded){
            throw new NoFileInSystemException();
        }
        return null;
    }

    @Override
    public String toString() {
        return "SystemEngine{" +
                "graph=" + graph +
                '}';
    }
}
