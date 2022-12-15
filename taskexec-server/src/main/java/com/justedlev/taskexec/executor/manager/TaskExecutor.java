package com.justedlev.taskexec.executor.manager;

import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.executor.model.TaskResultResponse;

public interface TaskExecutor {
    TaskResultResponse execute(TaskContext context);

    String getTaskName();
}
