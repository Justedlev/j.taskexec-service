package com.justedlev.taskexec.executor.manager;

import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.model.response.TaskResultResponse;

public interface TaskHandler<P> {
    TaskResultResponse execute(TaskContext context);

    String taskName();

    P payload();
}
