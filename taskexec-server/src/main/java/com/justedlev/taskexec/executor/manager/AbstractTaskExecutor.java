package com.justedlev.taskexec.executor.manager;

import com.justedlev.taskexec.executor.model.TaskResultResponse;
import com.justedlev.taskexec.executor.model.TaskContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTaskExecutor implements TaskExecutor {
    @Override
    public TaskResultResponse execute(TaskContext context) {
        try {
            var start = System.currentTimeMillis();
            var res = this.assign(context);
            log.info("Task {} was executed in {} ms", context.getTaskName(), System.currentTimeMillis() - start);

            return res;
        } catch (Exception e) {
            log.error("Failed to execute task {}: {}", context.getTaskName(), e.getMessage());

            return TaskResultResponse.builder()
                    .taskName(context.getTaskName())
                    .error(e.getMessage())
                    .build();
        }
    }

    protected abstract TaskResultResponse assign(TaskContext context);
}
