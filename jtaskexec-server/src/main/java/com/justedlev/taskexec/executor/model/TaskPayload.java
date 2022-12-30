package com.justedlev.taskexec.executor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskPayload<R> {
    private String taskName;
    private String cron;
    private R request;
}
