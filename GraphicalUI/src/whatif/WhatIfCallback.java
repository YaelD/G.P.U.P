package whatif;

import graph.Dependency;

public interface WhatIfCallback {
    void findWhatIf(String targetName, Dependency dependency);

}
