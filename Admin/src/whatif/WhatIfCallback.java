package whatif;


import general_enums.Dependency;

public interface WhatIfCallback {
    void findWhatIf(String targetName, Dependency dependency);

}
