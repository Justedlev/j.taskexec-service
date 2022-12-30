package com.justedlev.taskexec.component.impl;

import com.justedlev.taskexec.component.TaskSchedulerComponent;
import com.justedlev.taskexec.enumeration.TaskMode;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
                .collect(Collectors.groupingBy(Task::getMode));

        return taskMap.entrySet()
                .stream()
                .map(current -> handle(current.getKey(), current.getValue()))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<TaskResponse> handle(TaskMode status, List<Task> tasks) {
        return switch (status) {
            case NONE, SCHEDULED -> tasks.stream()
                    .map(current -> defaultMapper.map(current, TaskResponse.class))
                    .toList();
            case STOPPED -> scheduleTasks(tasks);
        };
    }

    private List<TaskResponse> scheduleTasks(List<Task> tasks) {
        tasks.forEach(current -> {
            current.setMode(TaskMode.SCHEDULED);
            taskScheduler.schedule(
                    () -> taskManager.assign(defaultMapper.map(current, TaskContext.class)),
                    new CronTrigger(current.getCron()));
        });
        var updated = taskRepository.saveAll(tasks);
        var taskNames = updated.stream()
                .map(Task::getTaskName)
                .toList();
        log.info("Scheduled {} tasks : {}", updated.size(), taskNames);

        return Arrays.asList(defaultMapper.map(updated, TaskResponse[].class));
    }
}
