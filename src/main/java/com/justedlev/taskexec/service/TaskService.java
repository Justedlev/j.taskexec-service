package com.justedlev.taskexec.service;

import com.justedlevhub.api.model.request.ScheduleTaskRequest;
import com.justedlevhub.api.model.request.UpdateTaskRequest;
import com.justedlevhub.api.model.response.TaskResponse;
import com.justedlevhub.api.model.response.TaskResultResponse;

import java.util.List;

public interface TaskService {
    List<TaskResponse> update(List<UpdateTaskRequest> request);

    List<TaskResponse> schedule(List<ScheduleTaskRequest> request);

    List<TaskResponse> getAll();

    TaskResultResponse executeTask(String taskName);
}
