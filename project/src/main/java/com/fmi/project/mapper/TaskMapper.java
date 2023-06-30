package com.fmi.project.mapper;

import com.fmi.project.dto.TaskDto;
import com.fmi.project.model.Event;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.service.EventService;
import com.fmi.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TaskMapper {

    private final EventService eventService;
    private final UserService userService;

    public TaskDto toDto(Task task){
        return TaskDto.builder()
                .name(task.getName())
                .description(task.getDescription())
                .dueDate(task.getDue_date())
                .status(task.getStatus())
                .creatorEmail(task.getCreatorEmail())
                .eventName(task.getEvent().getName())
                .assignees(task.getAssignees().stream()
                            .map(User::getEmail).collect(Collectors.toList()))
                .build();
    }

    public Task toEntity(TaskDto taskDto){
        return Task.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .due_date(taskDto.getDueDate())
                .status(taskDto.getStatus())
                .creatorEmail(taskDto.getCreatorEmail())
                .assignees(taskDto.getAssignees().stream()
                                .map(userService::findUserByEmail)
                                .flatMap(Optional::stream)
                                .collect(Collectors.toSet()))
                .event(eventService.getEventByName(taskDto.getEventName()).orElse(Event.builder().build()))
                .build();
    }

    public List<TaskDto> toDtoCollection(List<Task> tasks){
        if(tasks == null){
            return Collections.emptyList();
        }

        return tasks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
