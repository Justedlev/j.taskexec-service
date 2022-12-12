package com.justedlev.taskexec.service.impl;

import com.justedlev.taskexec.component.TaskComponent;
import com.justedlev.taskexec.component.TaskSchedulerComponent;
import com.justedlev.taskexec.component.UpdateTaskComponent;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.executor.model.TaskResultResponse;
import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;
import com.justedlev.taskexec.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskComponent taskComponent;
    private final UpdateTaskComponent updateTaskComponent;
    private final TaskSchedulerComponent taskSchedulerComponent;
    private final TaskManager taskManager;
    private final ModelMapper defaultMapper;

    @Override
    public List<TaskResponse> update(List<UpdateTaskRequest> request) {
        return updateTaskComponent.update(request)
                .stream()
                .map(current -> defaultMapper.map(current, TaskResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> schedule(List<ScheduleTaskRequest> request) {
        return taskSchedulerComponent.schedule(request)
                .stream()
                .map(current -> defaultMapper.map(current, TaskResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getAll() {
        return taskComponent.getAll()
                .stream()
                .map(current -> defaultMapper.map(current, TaskResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public TaskResultResponse executeTask(String taskName) {
        var task = taskComponent.getByName(taskName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Task %s not found", taskName)));
        var context = defaultMapper.map(task, TaskContext.class);

        return taskManager.assign(context);
    }
}
