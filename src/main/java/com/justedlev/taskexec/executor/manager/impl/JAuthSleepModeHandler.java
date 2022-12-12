package com.justedlev.taskexec.executor.manager.impl;

import com.justedlev.taskexec.executor.manager.AbstractTaskExecutor;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.executor.model.TaskResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JAuthSleepModeHandler extends AbstractTaskExecutor {
    @Override
    protected TaskResultResponse assign(TaskContext context) {
        return TaskResultResponse.builder()
                .taskName(this.getTaskName())
                .build();
    }

    @Override
    public String getTaskName() {
        return this.getClass().getSimpleName();
    }
}
