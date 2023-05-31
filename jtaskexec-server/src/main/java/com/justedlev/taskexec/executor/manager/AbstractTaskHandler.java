package com.justedlev.taskexec.executor.manager;

import com.justedlev.taskexec.model.response.TaskResultResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTaskHandler<P> implements TaskHandler<P> {
    @Override
    public TaskResultResponse execute() {
        var start = System.currentTimeMillis();
        var res = this.handle();
        var end = System.currentTimeMillis() - start;
        log.info("Task '{}' was executed in {} ms: {}", this.taskName(), end, res.getMessage());

        return res;
    }

    @Override
    public String taskName() {
        return this.getClass().getSimpleName();
    }

    protected abstract TaskResultResponse handle();

    @Override
    public void run() {
        execute();
    }
}
