package com.justedlev.taskexec.executor.manager;

import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.model.response.TaskResultResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTaskExecutorHandler implements TaskExecutor {
    @Override
    public TaskResultResponse execute(TaskContext context) {
        var start = System.currentTimeMillis();
        var res = this.handle(context);
        log.info("Task {} was executed in {} ms: {}",
                context.getTaskName(), System.currentTimeMillis() - start, res.getMessage());

        return res;
    }

    protected abstract TaskResultResponse handle(TaskContext context);
}
