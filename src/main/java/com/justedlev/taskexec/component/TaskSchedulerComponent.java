package com.justedlev.taskexec.component;

import com.justedlevhub.api.model.request.ScheduleTaskRequest;
import com.justedlevhub.api.model.response.TaskResponse;

import java.util.List;

public interface TaskSchedulerComponent {
    List<TaskResponse> schedule(List<ScheduleTaskRequest> request);
}
