package com.justedlev.taskexec.model.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTaskRequest {
    @NonNull
    private String taskName;
}
