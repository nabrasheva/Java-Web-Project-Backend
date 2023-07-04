package com.fmi.project.service;

import com.fmi.project.controller.validation.ObjectFoundException;
import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.model.Event;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void addTask_whenNullEvent() {
        final Task task = new Task();
        task.setName("TASK");
        when(taskRepository.findFirstByName(anyString()))
                .thenReturn(Optional.of(new Task()));

        assertThrows(ObjectNotFoundException.class, () -> {
            taskService.addTask(null, task);
        });
    }

    @Test
    void addTask_whenExistingTask() {
        final Task task = new Task();
        task.setName("TASK");
        when(taskRepository.findFirstByName(anyString()))
                .thenReturn(Optional.of(task));

        assertThrows(ObjectFoundException.class, () -> {
            taskService.addTask(new Event(), task);
        });
    }

    @Test
    void addTask_whenNonExistingTask() {
        //Given
        final Task task = new Task();
        task.setName("TASK");

        final Event event = new Event();
        event.setTasks(new HashSet<>());
        when(taskRepository.findFirstByName(anyString())).thenReturn(Optional.empty());

        //When
        taskService.addTask(event, task);

        //Then
        verify(taskRepository).findFirstByName(anyString());
        verify(taskRepository).save(any());
    }

    @Test
    void removeTask_whenNonExistingTask() {
        when(taskRepository.findFirstByName(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            taskService.removeTask(null, "TASK");
        });
    }

    @Test
    void removeTask_whenExistingTask() {
        final Task task = new Task();
        task.setEvent(new Event());
        task.getEvent().setId(1L);
        task.setName("TASK");
        final Event event = new Event();
        event.setId(2L);

        when(taskRepository.findFirstByName(anyString()))
                .thenReturn(Optional.of(task));

        assertThrows(ObjectNotFoundException.class, () -> {
            taskService.removeTask(event, "TASK");
        });
    }

    @Test
    void removeTask() {
        //Given
        final Task task = new Task();
        task.setEvent(new Event());
        task.getEvent().setId(1L);
        task.setName("TASK");
        final Event event = new Event();
        event.setId(1L);
        event.setTasks(new HashSet<>());

        when(taskRepository.findFirstByName(anyString()))
                .thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(any());

        //When
        taskService.removeTask(event, "TASK");

        //Then
        verify(taskRepository).findFirstByName(anyString());
        verify(taskRepository).delete(any());
    }

    @Test
    void getAssigneesByTaskName_whenNonExistingTask() {
        when(taskRepository.findFirstByName(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            taskService.getAssigneesByTaskName("TASK");
        });
    }

    @Test
    void getAssigneesByTaskName() {
        //Given
        final User user1 = new User();
        user1.setEmail("georgy@gmail.com");
        final User user2 = new User();
        user2.setEmail("niya@gmail.com");

        final Task task = new Task();
        task.setName("TASK");
        task.setAssignees(Set.of(user1, user2));

        when(taskRepository.findFirstByName(anyString())).thenReturn(Optional.of(task));

        //When
        final List<String> result =
                taskService.getAssigneesByTaskName("TASK").stream().sorted()
                        .toList();

        //Then
        verify(taskRepository).findFirstByName(anyString());
        assertAll("Check for correct emails",
                () -> assertEquals("georgy@gmail.com", result.get(0)),
                () -> assertEquals("niya@gmail.com", result.get(1)));
    }

    @Test
    void getTasksByAdmin() {
        //Given
        when(taskRepository.findByCreatorEmail(anyString()))
                .thenReturn(new ArrayList<>());

        //When
        taskService.getTasksByAdmin("test@gmail.com");

        //Then
        verify(taskRepository).findByCreatorEmail(anyString());
    }

}