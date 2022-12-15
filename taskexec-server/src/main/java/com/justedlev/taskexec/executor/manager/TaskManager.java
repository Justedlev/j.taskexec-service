package com.justedlev.taskexec.executor.manager;

import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.executor.model.TaskResultResponse;

public interface TaskManager {
    TaskResultResponse assign(TaskContext context);
}
