package com.justedlev.taskexec.executor.manager.impl;

import com.justedlev.taskexec.executor.manager.AbstractTaskExecutor;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.executor.model.TaskResultResponse;
import com.justedlev.taskexec.restclient.jauth.JAuthApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JAuthSleepModeHandler extends AbstractTaskExecutor {
    private final JAuthApiClient jAuthApiClient;

    @Override
    protected TaskResultResponse assign(TaskContext context) {
        return TaskResultResponse.builder()
                .taskName(this.getTaskName())
                .build();
    }

    @Override
    public String getTaskName() {
        return "j.auth/sleep-mode";
    }
}
