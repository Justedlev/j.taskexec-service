package com.justedlev.taskexec.component;

import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.repository.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskComponent {
    List<Task> updateAndSchedule(List<UpdateTaskRequest> request);

    Optional<Task> getByName(String taskName);

    List<Task> getAll();
}
