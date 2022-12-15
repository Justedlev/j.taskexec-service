package com.justedlev.taskexec.component;

import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;

import java.util.List;

public interface TaskSchedulerComponent {
    List<TaskResponse> schedule(List<ScheduleTaskRequest> request);
}
