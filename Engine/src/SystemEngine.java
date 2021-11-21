import exceptions.*;
import schema.generated.GPUPDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SystemEngine implements Engine{

    private Graph graph;
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

    @Override
    public GraphDTO activateTask() {
        return null;
    }

    @Override
    public boolean isFileLoaded() {
        return this.isFileLoaded;
    }

    @Override
    public String toString() {
        return "SystemEngine{" +
                "graph=" + graph +
                '}';
    }
}
