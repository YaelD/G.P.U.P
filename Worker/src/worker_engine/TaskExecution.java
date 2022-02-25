package worker_engine;

import dto.TargetDTO;

public class TaskExecution {

    ExecutionTarget executionTarget;

    public TaskExecution(TargetDTO targetDTO) {
        executionTarget = new ExecutionTarget(targetDTO);
        WorkerEngine.getInstance().addWorkerTarget(executionTarget);
        SendExecutionTargetRefresherTimer.getInstance().addTarget(executionTarget);
    }
}
