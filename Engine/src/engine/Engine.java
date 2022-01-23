package engine;

import dto.GraphDTO;
import dto.SerialSetDTO;
import dto.TargetDTO;
import dto.TaskParamsDTO;
import exceptions.*;
import graph.Dependency;
import graph.Graph;
import task.PausableThreadPoolExecutor;
import task.TaskType;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface Engine {

    public boolean loadFile(String path) throws InvalidFileException, DependencyConflictException, DuplicateTargetsException, InvalidDependencyException, TargetNotExistException, SerialSetException;

    public GraphDTO getGraphDTO();

    public TargetDTO getTarget(String name) throws TargetNotExistException;

    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName, Dependency dependency) throws TargetNotExistException, InvalidDependencyException;

    public GraphDTO activateTask(Consumer<TargetDTO> consumerString,
                                 Consumer<PausableThreadPoolExecutor> threadPoolConsumer,
                                 TaskParamsDTO taskParams, TaskType taskType, boolean isIncremental,
                                 int threadNumber, Set<String> selectedTargets);

    public boolean isFileLoaded();

    public boolean isRunInIncrementalMode(TaskType taskType, Set<String> selectedTargets);

    public boolean isCycleInGraph();

    public List<String> findCycle(String targetName) throws TargetNotExistException;

    public List<SerialSetDTO> getSerialSetsInfo();

    public Set<String> whatIf(String targetName, Dependency dependency);

    public TargetDTO getRunningTarget(String targetName);

    public int getMaxNumOfThreads();

    public Set<String> getTaskGraphInSystem(TaskType taskType);

    public Graph getGraphForRunning();
}
