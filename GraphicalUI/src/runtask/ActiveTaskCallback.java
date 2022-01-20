package runtask;

import dto.TaskParamsDTO;

public interface ActiveTaskCallback {
    public void activeTask(TaskParamsDTO taskParams);
}
