package com.justedlev.taskexec.component.impl;

import com.justedlev.taskexec.component.TaskComponent;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TaskComponentImpl implements TaskComponent {
    private final TaskRepository taskRepository;
    private final ModelMapper defaultMapper;
    private final TaskScheduler taskScheduler;
    private final TaskManager taskManager;

    @Override
    public List<Task> updateAndSchedule(List<UpdateTaskRequest> request) {
        var requestMap = request.stream()
                .filter(current -> StringUtils.isNotBlank(current.getTaskName()))
                .filter(current -> StringUtils.isNotBlank(current.getCron()))
                .collect(Collectors.toMap(UpdateTaskRequest::getTaskName, UpdateTaskRequest::getCron));
        var tasks = taskRepository.findByTaskNameIn(requestMap.keySet());

        tasks.forEach(current -> {
            var context = defaultMapper.map(current, TaskContext.class);
            var cron = requestMap.get(current.getTaskName());
            taskScheduler.schedule(() -> taskManager.assign(context), new CronTrigger(cron));
            current.setCron(cron);
        });

        return taskRepository.saveAll(tasks);
    }

    @Override
    public Optional<Task> getByName(String taskName) {
        return Optional.ofNullable(taskName)
                .filter(StringUtils::isNotEmpty)
                .map(List::of)
                .map(taskRepository::findByTaskNameIn)
                .map(Collection::stream)
                .flatMap(Stream::findFirst);
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }
}
