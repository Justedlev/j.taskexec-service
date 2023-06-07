package com.justedlev.taskexec.component;

import com.justedlevhub.api.model.response.TaskResponse;

import java.util.List;
import java.util.Optional;

public interface TaskComponent {
    Optional<TaskResponse> getByName(String taskName);

    List<TaskResponse> getAll();
}
