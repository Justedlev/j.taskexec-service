package com.justedlev.taskexec.controller;

import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;
import com.justedlev.taskexec.model.response.TaskResultResponse;
import com.justedlev.taskexec.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping("/task")
@RestController
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/update")
    public ResponseEntity<List<TaskResponse>> update(@RequestBody List<@Valid UpdateTaskRequest> request) {
        return ResponseEntity.ok(taskService.update(request));
    }

    @PostMapping("/schedule")
    public ResponseEntity<List<TaskResponse>> schedule(@RequestBody List<@Valid ScheduleTaskRequest> request) {
        return ResponseEntity.ok(taskService.schedule(request));
    }

    @PostMapping("/execute/{taskName}")
    public ResponseEntity<TaskResultResponse> executeTask(@PathVariable @NotBlank @NotNull String taskName) {
        return ResponseEntity.ok(taskService.executeTask(taskName));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<TaskResponse>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }
}
