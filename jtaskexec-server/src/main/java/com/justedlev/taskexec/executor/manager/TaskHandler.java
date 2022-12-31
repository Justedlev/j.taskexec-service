package com.justedlev.taskexec.executor.manager;

import com.justedlev.taskexec.model.response.TaskResultResponse;

public interface TaskHandler<P> {
    TaskResultResponse execute();

    String taskName();

    P payload();
}
