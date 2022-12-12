package com.justedlev.taskexec.component.impl;

import com.justedlev.taskexec.component.TaskSchedulerComponent;
import com.justedlev.taskexec.enumeration.TaskStatus;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskSchedulerComponentImpl implements TaskSchedulerComponent {
    private final TaskRepository taskRepository;
    private final ModelMapper defaultMapper;
    private final TaskScheduler taskScheduler;
    private final TaskManager taskManager;

    @Override
    public List<TaskResponse> schedule(List<ScheduleTaskRequest> request) {
        var taskNames = request.stream()
                .map(ScheduleTaskRequest::getTaskName)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        var taskMap = taskRepository.findByTaskNameIn(taskNames)
                .stream()
                .collect(Collectors.groupingBy(Task::getStatus));

        return taskMap.entrySet()
                .stream()
                .map(current -> handle(current.getKey(), current.getValue()))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<TaskResponse> handle(TaskStatus status, List<Task> tasks) {
        switch (status) {
            case NEW:
                return tasks.stream()
                        .map(current -> {
                            var res = defaultMapper.map(current, TaskResponse.class);
                            res.setError(String.format("Task in status %s, need to update cron", status));

                            return res;
                        })
                        .toList();
            case WORK:
                return tasks.stream()
                        .map(current -> {
                            var res = defaultMapper.map(current, TaskResponse.class);
                            res.setError("Task already scheduled");

                            return res;
                        })
                        .toList();
            case CLOSED:
                return scheduleTasks(tasks);
            default:
                return Collections.emptyList();
        }
    }

    private List<TaskResponse> scheduleTasks(List<Task> tasks) {
        tasks.forEach(current -> current.setStatus(TaskStatus.WORK));
        var updated = taskRepository.saveAll(tasks);
        updated.forEach(current -> taskScheduler.schedule(
                () -> taskManager.assign(defaultMapper.map(current, TaskContext.class)),
                new CronTrigger(current.getCron())));

        return List.of(defaultMapper.map(updated, TaskResponse[].class));
    }
}
