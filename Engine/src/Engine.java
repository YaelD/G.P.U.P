import exceptions.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface Engine {

    public boolean readFile(String path) throws InvalidFileException, DependencyConflictException, DuplicateTargetsException, InvalidDependencyException, TargetNotExistException;

    public GraphDTO getGraphDTO();

    public TargetDTO getTarget(String name) throws TargetNotExistException;

    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName, String relation) throws TargetNotExistException, InvalidDependencyException;

    public GraphDTO activateTask(Consumer<String> consumerString, TaskParamsDTO taskParams, TaskType taskType);

    public boolean isFileLoaded();


}
