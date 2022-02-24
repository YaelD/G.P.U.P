package worker_engine;

import dto.TargetDTO;
import dto.TaskParamsDTO;

public class TaskExecution {

    ExecutionTarget executionTarget;

    public TaskExecution(TargetDTO targetDTO) {
        executionTarget = new ExecutionTarget(targetDTO);
    }
}
