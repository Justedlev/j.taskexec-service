package com.justedlev.taskexec.executor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskContext {
    private String taskName;
    private HashMap<String, Object> payload;
}
