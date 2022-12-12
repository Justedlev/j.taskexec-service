package com.justedlev.taskexec.component.impl;

import com.justedlev.taskexec.component.TaskSchedulerComponent;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
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

@Component
@RequiredArgsConstructor
public class TaskSchedulerComponentImpl implements TaskSchedulerComponent {
    private final TaskRepository taskRepository;
    private final ModelMapper defaultMapper;
    private final TaskScheduler taskScheduler;
    private final TaskManager taskManager;

    @Override
    public List<Task> schedule(List<ScheduleTaskRequest> request) {
        var taskNames = request.stream()
                .map(ScheduleTaskRequest::getTaskName)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        var tasks = taskRepository.findByTaskNameIn(taskNames)
                .stream()
                .filter(current -> Boolean.FALSE.equals(current.getIsScheduled()))
                .collect(Collectors.toList());
        tasks.forEach(current -> current.setIsScheduled(Boolean.TRUE));
        var updated = taskRepository.saveAll(tasks);
        updated.forEach(current -> taskScheduler.schedule(
                () -> taskManager.assign(defaultMapper.map(current, TaskContext.class)),
                new CronTrigger(current.getCron())
        ));

        return updated;
    }
}
