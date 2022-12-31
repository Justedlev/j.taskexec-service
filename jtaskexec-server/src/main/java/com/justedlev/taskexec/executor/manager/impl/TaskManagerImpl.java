package com.justedlev.taskexec.executor.manager.impl;

import com.justedlev.taskexec.executor.manager.TaskHandler;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.model.response.TaskResultResponse;
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
    private final Map<String, TaskHandler<?>> handlerMap;

    public TaskManagerImpl(Set<TaskHandler<?>> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        TaskHandler::taskName,
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
                        String.format("Task '%s' not exists", context.getTaskName())));
    }
}
