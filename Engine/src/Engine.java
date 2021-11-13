import exceptions.*;

import java.util.Collection;
import java.util.List;

public interface Engine {

    public boolean readFile(String path) throws InvalidFileException, DependencyConflictException, DuplicateTargetsException, InvalidDependencyException, TargetNotExistException;

    public GraphDTO getGraphDTO() throws NoFileInSystemException;

    public TargetDTO getTarget(String name) throws TargetNotExistException, NoFileInSystemException;

    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName, String relation) throws NoFileInSystemException, TargetNotExistException, InvalidDependencyException;

    public GraphDTO activateTask() throws NoFileInSystemException;


}
