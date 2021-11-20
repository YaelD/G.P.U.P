import exceptions.*;

import java.util.Collection;
import java.util.List;

public interface Engine {

    public boolean readFile(String path) throws InvalidFileException, DependencyConflictException, DuplicateTargetsException, InvalidDependencyException, TargetNotExistException;

    public GraphDTO getGraphDTO();

    public TargetDTO getTarget(String name) throws TargetNotExistException;

    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName, String relation) throws TargetNotExistException, InvalidDependencyException;

    public GraphDTO activateTask();

    public boolean isFileLoaded();


}
