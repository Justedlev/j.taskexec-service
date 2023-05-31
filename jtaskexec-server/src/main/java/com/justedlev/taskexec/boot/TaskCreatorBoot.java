package com.justedlev.taskexec.boot;

import com.justedlev.taskexec.executor.manager.TaskHandler;
import com.justedlev.taskexec.properties.JTaskExecProperties;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class TaskCreatorBoot implements ApplicationRunner {
    private final Set<TaskHandler<?>> handlers;
    private final TaskRepository taskRepository;
    private final JTaskExecProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        if (Boolean.TRUE.equals(properties.getRegisterTasks())) {
            var taskNames = getTaskNames();
            var existTaskMap = getExistsTasks(taskNames);
            var notExistTasks = getNotExistTasks(existTaskMap);

            if (CollectionUtils.isNotEmpty(notExistTasks)) {
                var saved = taskRepository.saveAll(notExistTasks)
                        .stream()
                        .map(Task::getId)
                        .toList();
                log.info("Created {} tasks {}", saved.size(), saved);
            }
        }
    }

    private List<Task> getNotExistTasks(Map<String, Task> existTaskMap) {
        return handlers.stream()
                .filter(current -> StringUtils.isNotBlank(current.taskName()))
                .filter(current -> !existTaskMap.containsKey(current.taskName()))
                .map(current -> Task.builder()
                        .id(current.taskName())
                        .build())
                .toList();
    }

    private Map<String, Task> getExistsTasks(Set<String> taskNames) {
        return taskRepository.findByTaskNameIn(taskNames)
                .stream()
                .collect(Collectors.toMap(Task::getId, Function.identity()));
    }

    private Set<String> getTaskNames() {
        return handlers.stream()
                .map(TaskHandler::taskName)
                .collect(Collectors.toSet());
    }
}
