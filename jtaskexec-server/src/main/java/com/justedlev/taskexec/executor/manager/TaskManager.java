package com.justedlev.taskexec.executor.manager;

import com.justedlev.taskexec.model.response.TaskResultResponse;

public interface TaskManager {
    TaskResultResponse assign(String taskName);
}
