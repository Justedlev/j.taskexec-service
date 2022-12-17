package com.justedlev.taskexec.executor.model;

import com.justedlev.taskexec.model.TaskPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskContext {
    private String taskName;
    private TaskPayload payload;
}
