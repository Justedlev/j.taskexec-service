package com.justedlev.taskexec.service;

import com.justedlev.taskexec.model.response.TaskResultResponse;
import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;

import java.util.List;

public interface TaskService {
    List<TaskResponse> update(List<UpdateTaskRequest> request);

    List<TaskResponse> schedule(List<ScheduleTaskRequest> request);

    List<TaskResponse> getAll();

    TaskResultResponse executeTask(String taskName);
}
