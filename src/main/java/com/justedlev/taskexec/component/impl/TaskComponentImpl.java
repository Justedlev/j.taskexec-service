package com.justedlev.taskexec.component.impl;

import com.justedlev.taskexec.component.TaskComponent;
import com.justedlev.taskexec.repository.TaskRepository;
import com.justedlev.taskexec.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskComponentImpl implements TaskComponent {
    private final TaskRepository taskRepository;

    @Override
    public Optional<Task> getByName(String taskName) {
        return Optional.ofNullable(taskName)
                .filter(StringUtils::isNotEmpty)
                .map(List::of)
                .map(taskRepository::findByTaskNameIn)
                .map(Collection::stream)
                .flatMap(Stream::findFirst);
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }
}
