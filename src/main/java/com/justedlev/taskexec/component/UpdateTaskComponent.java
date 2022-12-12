package com.justedlev.taskexec.component;

import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;

import java.util.List;

public interface UpdateTaskComponent {
    List<TaskResponse> update(List<UpdateTaskRequest> request);
}
