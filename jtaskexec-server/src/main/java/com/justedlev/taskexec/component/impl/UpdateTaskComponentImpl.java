package com.justedlev.taskexec.component.impl;

import com.justedlev.taskexec.component.UpdateTaskComponent;
import com.justedlev.taskexec.enumeration.TaskMode;
import com.justedlev.taskexec.model.request.UpdateTaskRequest;
import com.justedlev.taskexec.model.response.TaskResponse;
import com.justedlev.taskexec.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateTaskComponentImpl implements UpdateTaskComponent {
    private final TaskRepository taskRepository;
    private final ModelMapper defaultMapper;

    @Override
    public List<TaskResponse> update(List<UpdateTaskRequest> request) {
        var requestMap = request.stream()
                .filter(current -> StringUtils.isNotBlank(current.getTaskName()))
                .filter(current -> StringUtils.isNotBlank(current.getCron()))
                .collect(Collectors.toMap(UpdateTaskRequest::getTaskName, Function.identity()));
        var tasks = taskRepository.findByTaskNameIn(requestMap.keySet());
        tasks.forEach(current -> Optional.ofNullable(requestMap.get(current.getTaskName()))
                .ifPresent(that -> {
                    current.setCron(that.getCron());
                    current.setMode(TaskMode.STOPPED);
                }));
        var updated = taskRepository.saveAll(tasks);

        return List.of(defaultMapper.map(updated, TaskResponse[].class));
    }
}
