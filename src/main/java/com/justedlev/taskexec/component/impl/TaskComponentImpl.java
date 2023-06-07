package com.justedlev.taskexec.component.impl;

import com.justedlev.taskexec.component.TaskComponent;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlevhub.api.model.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TaskComponentImpl implements TaskComponent {
    private final TaskRepository taskRepository;
    private final ModelMapper defaultMapper;

    @Override
    public Optional<TaskResponse> getByName(String taskName) {
        return Optional.ofNullable(taskName)
                .filter(StringUtils::isNotEmpty)
                .map(List::of)
                .map(taskRepository::findByTaskNameIn)
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .map(current -> defaultMapper.map(current, TaskResponse.class));
    }

    @Override
    public List<TaskResponse> getAll() {
        var tasks = taskRepository.findAll();

        return List.of(defaultMapper.map(tasks, TaskResponse[].class));
    }
}
