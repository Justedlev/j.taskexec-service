package com.justedlev.taskexec.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {
    @NotNull
    @NotBlank
    private String taskName;
    @NotNull
    @NotBlank
    private String cron;
}
