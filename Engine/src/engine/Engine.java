package engine;


import exceptions.*;
import graph.Dependency;
import graph.Graph;
import target.Target;
import task.RunType;
import task.Task;
import task.TaskType;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Engine {

    //public boolean loadFile(String path) throws InvalidFileException, DependencyConflictException, DuplicateTargetsException, InvalidDependencyException, TargetNotExistException, SerialSetException, DupSerialSetsNameException;

    public boolean loadFile(InputStream stream, String creatorName) throws
            DuplicateTargetsException, TargetNotExistException, InvalidDependencyException, DependencyConflictException;

    public Map<String, Task> getTasksInSystem();

    public Map<String, Graph> getGraphsInSystem();

    public Target getTarget(String name, String graphName) throws TargetNotExistException;

    public Collection<List<String>> getPaths(String firstTargetName, String secondTargetName,
                                              Dependency dependency, String graphName) throws TargetNotExistException ;

//    public GraphDTO activateTask(Consumer<TargetDTO> consumerString,
//                                 Consumer<PausableThreadPoolExecutor> threadPoolConsumer,
//                                 TaskParamsDTO taskParams, TaskType taskType, boolean isIncremental,
//                                 int threadNumber, Set<String> selectedTargets);

    //public boolean isFileLoaded();

    //TODO: change according to ex3
    public boolean isRunInIncrementalMode(TaskType taskType, Set<String> selectedTargets);

    public boolean isCycleInGraph(String graphName);

    public List<String> findCycle(String targetName, String graphName) throws TargetNotExistException;

    public Set<String> whatIfForRunningTask(String targetName, Dependency dependency, TaskType taskType,
                                            RunType runType, String graphName);

    //public Target getRunningTarget(String targetName);

    public Set<String> getTaskGraphInSystem(TaskType taskType);

    //public Graph getGraphForRunning();
}
