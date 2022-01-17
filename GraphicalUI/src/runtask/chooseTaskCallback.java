package runtask;

import graph.Dependency;
import task.TaskType;

import java.util.List;

public interface chooseTaskCallback {
    void loadSpecificTaskToggles(List<String> targetsName, TaskType taskType);
}
