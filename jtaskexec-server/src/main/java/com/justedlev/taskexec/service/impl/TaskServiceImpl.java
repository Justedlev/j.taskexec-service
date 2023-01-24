package com.justedlev.taskexec.service.impl;

import com.justedlev.taskexec.component.TaskComponent;
import com.justedlev.taskexec.component.TaskSchedulerComponent;
import com.justedlev.taskexec.component.UpdateTaskComponent;
import com.justedlev.taskexec.executor.manager.TaskManager;
import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;
import com.justedlev.taskexec.model.response.TaskResultResponse;
import com.justedlev.taskexec.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskComponent taskComponent;
    private final UpdateTaskComponent updateTaskComponent;
    private final TaskSchedulerComponent taskSchedulerComponent;
    private final TaskManager taskManager;

    @Override
    public List<TaskResponse> update(List<UpdateTaskRequest> request) {
        return updateTaskComponent.update(request);
    }

    @Override
    public List<TaskResponse> schedule(List<ScheduleTaskRequest> request) {
        return taskSchedulerComponent.schedule(request);
    }

    @Override
    public List<TaskResponse> getAll() {
        return taskComponent.getAll();
    }

    @Override
    public TaskResultResponse executeTask(String taskName) {
        var task = taskComponent.getByName(taskName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Task %s not found", taskName)));

        return taskManager.assign(task.getTaskName());
    }
}
