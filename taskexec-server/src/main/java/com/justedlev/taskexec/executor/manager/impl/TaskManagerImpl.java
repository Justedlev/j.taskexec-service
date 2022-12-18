package com.justedlev.taskexec.executor.manager.impl;

import com.justedlev.taskexec.executor.manager.AbstractTaskExecutorHandler;
import com.justedlev.taskexec.executor.manager.TaskExecutor;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.executor.model.TaskResultResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TaskManagerImpl implements TaskManager {
    private final Map<String, AbstractTaskExecutorHandler> handlerMap;

    public TaskManagerImpl(Set<AbstractTaskExecutorHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        TaskExecutor::getTaskName,
                        Function.identity()
                ));
    }

    @Override
    public TaskResultResponse assign(@NonNull TaskContext context) {
        return Optional.ofNullable(context.getTaskName())
                .filter(StringUtils::isNotBlank)
                .filter(handlerMap::containsKey)
                .map(handlerMap::get)
                .map(current -> current.execute(context))
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Cannot execute task %s", context.getTaskName())));
    }
}
