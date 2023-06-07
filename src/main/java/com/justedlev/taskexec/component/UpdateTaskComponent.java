package com.justedlev.taskexec.component;

import com.justedlevhub.api.model.request.UpdateTaskRequest;
import com.justedlevhub.api.model.response.TaskResponse;

import java.util.List;

public interface UpdateTaskComponent {
    List<TaskResponse> update(List<UpdateTaskRequest> request);
}
