package com.justedlev.taskexec.controller;

import com.justedlev.taskexec.service.TaskService;
import com.justedlevhub.api.model.request.ScheduleTaskRequest;
import com.justedlevhub.api.model.request.UpdateTaskRequest;
import com.justedlevhub.api.model.response.TaskResponse;
import com.justedlevhub.api.model.response.TaskResultResponse;
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
