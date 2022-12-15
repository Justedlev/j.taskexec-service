package com.justedlev.taskexec.controller;

import com.justedlev.taskexec.executor.model.TaskResultResponse;
import com.justedlev.taskexec.model.request.ScheduleTaskRequest;
import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;
import com.justedlev.taskexec.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/task")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/update")
    public ResponseEntity<List<TaskResponse>> update(@RequestBody List<UpdateTaskRequest> request) {
        return ResponseEntity.ok(taskService.update(request));
    }

    @PostMapping("/schedule")
    public ResponseEntity<List<TaskResponse>> schedule(@RequestBody List<ScheduleTaskRequest> request) {
        return ResponseEntity.ok(taskService.schedule(request));
    }

    @PostMapping("/execute/{taskName}")
    public ResponseEntity<TaskResultResponse> executeTask(@PathVariable String taskName) {
        return ResponseEntity.ok(taskService.executeTask(taskName));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<TaskResponse>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }
}
