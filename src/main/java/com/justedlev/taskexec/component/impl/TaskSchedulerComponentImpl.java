package com.justedlev.taskexec.component.impl;

import com.justedlev.taskexec.component.TaskSchedulerComponent;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .collect(Collectors.partitioningBy(Task::getIsScheduled));
        var scheduleTasks = scheduleTasks(taskMap.get(Boolean.FALSE));
        var failedToSchedule = getScheduleFail(taskMap.get(Boolean.TRUE));

        return Stream.concat(scheduleTasks.stream(), failedToSchedule.stream())
                .collect(Collectors.toList());
    }

    private List<TaskResponse> getScheduleFail(List<Task> tasks) {
        var res = List.of(defaultMapper.map(tasks, TaskResponse[].class));
        res.forEach(current -> current.setError("Task already scheduled"));

        return res;
    }

    private List<TaskResponse> scheduleTasks(List<Task> tasks) {
        tasks.forEach(current -> current.setIsScheduled(Boolean.TRUE));
        var updated = taskRepository.saveAll(tasks);
        updated.forEach(current -> taskScheduler.schedule(
                () -> taskManager.assign(defaultMapper.map(current, TaskContext.class)),
                new CronTrigger(current.getCron())
        ));

        return List.of(defaultMapper.map(updated, TaskResponse[].class));
    }
}
