package com.justedlev.taskexec.model.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {
    @NonNull
    private String taskName;
    @NonNull
    private String cron;
}
