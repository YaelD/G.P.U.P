package engine;

import dto.GraphDTO;
import dto.TargetDTO;
import dto.TaskParamsDTO;
import exceptions.*;
import graph.Dependency;
import task.TaskType;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface Engine {

    public boolean readFile(String path) throws InvalidFileException, DependencyConflictException, DuplicateTargetsException, InvalidDependencyException, TargetNotExistException;

    public GraphDTO getGraphDTO();

    public TargetDTO getTarget(String name) throws TargetNotExistException;

    //public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName, String relation) throws TargetNotExistException, InvalidDependencyException;
    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName, Dependency dependency) throws TargetNotExistException, InvalidDependencyException;

    public GraphDTO activateTask(Consumer<TargetDTO> consumerString, TaskParamsDTO taskParams, TaskType taskType, boolean isIncremental);

    public boolean isFileLoaded();

    public boolean isRunInIncrementalMode(TaskType taskType);

    public boolean isCycleInGraph();

    public List<String> findCycle(String targetName) throws TargetNotExistException;


}
