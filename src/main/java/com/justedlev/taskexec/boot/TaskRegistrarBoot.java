package com.justedlev.taskexec.boot;

import com.justedlev.taskexec.executor.manager.AbstractTaskExecutor;
import com.justedlev.taskexec.executor.manager.TaskExecutor;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class TaskRegistrarBoot implements ApplicationRunner {
    private final Set<AbstractTaskExecutor> executors;
    private final TaskRepository taskRepository;

    @Override
    public void run(ApplicationArguments args) {
        var taskNames = executors.stream()
                .map(TaskExecutor::getTaskName)
                .collect(Collectors.toSet());
        var exists = taskRepository.findByTaskNameIn(taskNames)
                .stream()
                .collect(Collectors.toMap(Task::getTaskName, Function.identity()));
        var tasks = executors.stream()
                .filter(current -> !exists.containsKey(current.getTaskName()))
                .map(current -> Task.builder()
                        .taskName(current.getTaskName())
                        .build())
                .collect(Collectors.toList());
        var saved = taskRepository.saveAll(tasks)
                .stream()
                .map(Task::getTaskName)
                .collect(Collectors.toList());
        log.info("Created {} tasks : {}", saved.size(), saved);
    }
}
