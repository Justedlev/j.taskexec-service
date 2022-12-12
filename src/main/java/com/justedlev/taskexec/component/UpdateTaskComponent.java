package com.justedlev.taskexec.component;

import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.repository.entity.Task;

import java.util.List;

public interface UpdateTaskComponent {
    List<Task> update(List<UpdateTaskRequest> request);
}
