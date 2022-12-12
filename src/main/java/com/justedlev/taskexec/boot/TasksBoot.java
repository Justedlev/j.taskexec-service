package com.justedlev.taskexec.boot;

import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.properties.TaskExecProperties;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

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
                    .collect(Collectors.partitioningBy(current -> StringUtils.isNotBlank(current.getCron())));
            restoreTasks(taskMap.get(Boolean.TRUE));

            if (CollectionUtils.isNotEmpty(taskMap.get(Boolean.FALSE))) {
                var res = taskMap.get(Boolean.FALSE).stream()
                        .map(Task::getTaskName)
                        .collect(Collectors.toList());

                log.warn("No cron for schedule tasks : {}", res);
            }
        }
    }

    private void restoreTasks(List<Task> existsWithCron) {
        if (CollectionUtils.isNotEmpty(existsWithCron)) {
            var results = existsWithCron.stream()
                    .map(this::restoreTask)
                    .collect(Collectors.toList());
            log.info("Scheduled {} tasks : {}", results.size(), results);
        }
    }

    private String restoreTask(Task task) {
        var context = TaskContext.builder()
                .taskName(task.getTaskName())
                .build();
        taskScheduler.schedule(() -> taskManager.assign(context), new CronTrigger(task.getCron()));

        return task.getTaskName();
    }
}
