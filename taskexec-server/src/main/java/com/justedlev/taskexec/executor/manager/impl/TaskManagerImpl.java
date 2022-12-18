package com.justedlev.taskexec.executor.manager.impl;

import com.justedlev.taskexec.executor.manager.AbstractTaskExecutorHandler;
import com.justedlev.taskexec.executor.manager.TaskExecutor;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.executor.model.TaskResultResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TaskManagerImpl implements TaskManager {
    private final Map<String, AbstractTaskExecutorHandler> executorMap;

    public TaskManagerImpl(Set<AbstractTaskExecutorHandler> executors) {
        this.executorMap = executors.stream()
                .collect(Collectors.toMap(
                        TaskExecutor::getTaskName,
                        Function.identity()
                ));
    }

    @Override
    public TaskResultResponse assign(TaskContext context) {
        return executorMap.get(context.getTaskName()).execute(context);
    }
}
