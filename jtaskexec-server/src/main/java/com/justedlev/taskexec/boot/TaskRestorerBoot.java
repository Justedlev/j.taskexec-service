package com.justedlev.taskexec.boot;

import com.justedlev.taskexec.enumeration.TaskStatus;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.properties.JTaskExecProperties;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class TaskRestorerBoot implements ApplicationRunner {
    private final TaskManager taskManager;
    private final TaskRepository taskRepository;
    private final TaskScheduler taskScheduler;
    private final JTaskExecProperties properties;
    private final ModelMapper defaultMapper;

    @Override
    public void run(ApplicationArguments args) {
        if (Boolean.TRUE.equals(properties.getRestoreTasks())) {
            var tasks = taskRepository.findAll();
            var taskMap = tasks.stream()
                    .collect(Collectors.groupingBy(Task::getStatus));
            taskMap.forEach(this::handle);
        }
    }

    private void handle(TaskStatus status, List<Task> tasks) {
        var names = tasks.stream()
                .map(Task::getTaskName)
                .toList();

        switch (status) {
            case NEW -> log.warn("Tasks no have cron for schedule {}", names);
            case WORK -> log.warn("Tasks already started {}", names);
            case CLOSED -> restoreTasks(tasks);
        }
    }

    private void restoreTasks(List<Task> tasks) {
        tasks.forEach(current -> {
            restoreTask(current);
            current.setStatus(TaskStatus.WORK);
        });
        var names = taskRepository.saveAll(tasks).stream()
                .map(Task::getTaskName)
                .toList();
        log.info("Scheduled {} tasks {}", tasks.size(), names);
    }

    private void restoreTask(Task task) {
        var context = defaultMapper.map(task, TaskContext.class);
        taskScheduler.schedule(() -> taskManager.assign(context), new CronTrigger(task.getCron()));
    }

    @PreDestroy
    private void closeTasks() {
        var tasks = taskRepository.findAll();
        tasks.stream()
                .filter(current -> TaskStatus.WORK.equals(current.getStatus()))
                .forEach(current -> current.setStatus(TaskStatus.CLOSED));
        var closed = taskRepository.saveAll(tasks)
                .stream()
                .map(Task::getTaskName)
                .toList();

        log.info("Closed {} tasks {}", closed.size(), closed);
    }
}
