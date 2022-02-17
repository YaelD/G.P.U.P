package findpath;

//import graph.Dependency;

import general_enums.Dependency;

public interface findPathCallback {
    void findPaths(String sourceTargetName, String destinationTargetName, Dependency dependency);
}
