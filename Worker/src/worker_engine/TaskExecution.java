package worker_engine;

import dto.TargetDTO;

public class TaskExecution {

    ExecutionTarget executionTarget;

    public TaskExecution(TargetDTO targetDTO) {
        executionTarget = new ExecutionTarget(targetDTO);
        SendExecutionTargetRefresherTimer.getInstance().addTarget(executionTarget);
    }
}
