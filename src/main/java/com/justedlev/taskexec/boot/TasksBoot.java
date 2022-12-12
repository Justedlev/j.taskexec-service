package com.justedlev.taskexec.boot;

import com.justedlev.taskexec.enumeration.TaskStatus;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.properties.TaskExecProperties;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
public class TasksBoot implements ApplicationRunner {
    private final TaskManager taskManager;
    private final TaskRepository taskRepository;
    private final TaskScheduler taskScheduler;
    private final TaskExecProperties properties;

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
        switch (status) {
            case NEW: {
                var names = tasks.stream()
                        .map(Task::getTaskName)
                        .collect(Collectors.toList());
                log.warn("Tasks in status {} with empty cron : {}", status, names);
                break;
            }
            case WORK: {
                var names = tasks.stream()
                        .map(Task::getTaskName)
                        .collect(Collectors.toList());
                log.warn("Tasks in status {} : {}", status, names);
                break;
            }
            case CLOSED: {
                restoreTasks(tasks);
                break;
            }
        }
    }

    private void restoreTasks(List<Task> existsWithCron) {
        if (CollectionUtils.isNotEmpty(existsWithCron)) {
            var tasks = existsWithCron.stream()
                    .map(this::restoreTask)
                    .collect(Collectors.toList());
            tasks.forEach(current -> current.setStatus(TaskStatus.WORK));
            var names = taskRepository.saveAll(tasks).stream()
                    .map(Task::getTaskName)
                    .collect(Collectors.toList());
            log.info("Scheduled {} tasks : {}", tasks.size(), names);
        }
    }

    private Task restoreTask(Task task) {
        var context = TaskContext.builder()
                .taskName(task.getTaskName())
                .build();
        taskScheduler.schedule(() -> taskManager.assign(context), new CronTrigger(task.getCron()));

        return task;
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
                .collect(Collectors.toList());

        log.info("Closed {} tasks : {}", closed.size(), closed);
    }
}
