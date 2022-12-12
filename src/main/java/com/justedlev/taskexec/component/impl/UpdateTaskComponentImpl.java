package com.justedlev.taskexec.component.impl;

import com.justedlev.taskexec.component.UpdateTaskComponent;
import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateTaskComponentImpl implements UpdateTaskComponent {
    private final TaskRepository taskRepository;

    @Override
    public List<Task> update(List<UpdateTaskRequest> request) {
        var requestMap = request.stream()
                .filter(current -> StringUtils.isNotBlank(current.getTaskName()))
                .filter(current -> StringUtils.isNotBlank(current.getCron()))
                .collect(Collectors.toMap(UpdateTaskRequest::getTaskName, UpdateTaskRequest::getCron));
        var tasks = taskRepository.findByTaskNameIn(requestMap.keySet());
        tasks.forEach(current -> current.setCron(requestMap.get(current.getTaskName())));

        return taskRepository.saveAll(tasks);
    }
}
