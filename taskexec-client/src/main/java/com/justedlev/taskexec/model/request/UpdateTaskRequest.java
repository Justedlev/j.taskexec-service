package com.justedlev.taskexec.model.request;

import lombok.*;

import java.util.HashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {
    @NonNull
    private String taskName;
    @NonNull
    private String cron;
    private HashMap<String, Object> payload;
}
