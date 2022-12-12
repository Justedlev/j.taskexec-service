package com.justedlev.taskexec.component;

import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
import com.justedlev.taskexec.repository.entity.Task;

import java.util.List;

public interface TaskSchedulerComponent {
    List<Task> schedule(List<ScheduleTaskRequest> request);
}
