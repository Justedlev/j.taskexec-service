package com.justedlev.taskexec.service;

import com.justedlev.taskexec.executor.model.TaskResultResponse;
import com.justedlev.taskexec.model.response.TaskResponse;
import com.justedlev.taskexec.model.request.UpdateTaskRequest;

import java.util.List;

public interface TaskService {
    List<TaskResponse> update(List<UpdateTaskRequest> request);

    List<TaskResponse> getAll();

    TaskResultResponse executeTask(String taskName);
}
