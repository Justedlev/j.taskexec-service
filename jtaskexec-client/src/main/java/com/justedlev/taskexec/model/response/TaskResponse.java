package com.justedlev.taskexec.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String cron;
    private String taskName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date modifiedAt;
}
