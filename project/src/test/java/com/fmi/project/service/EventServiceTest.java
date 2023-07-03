package com.fmi.project.service;

import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.dto.EventDto;
import com.fmi.project.dto.EventUserDto;
import com.fmi.project.dto.TaskDto;
import com.fmi.project.dto.UpdateTaskDto;
import com.fmi.project.enums.Role;
import com.fmi.project.mapper.EventMapper;
import com.fmi.project.mapper.EventUserMapper;
import com.fmi.project.mapper.TaskMapper;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.repository.EventRepository;
import com.fmi.project.repository.UserRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private EventMapper eventMapper;
    @Mock
    private UserService userService;
    @Mock
    private EventUserMapper eventUserMapper;

    @Mock
    private EventUserService eventUserService;

    @Mock
    private TaskService taskService;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void getAllUsersByEventAndRole_whenNonExistingEvent() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.getAllUsersByEventAndRole("Test", "TEST");
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"planner", "guest"})
    void getAllUsersByEventAndRole_whenRoleIsPlanner(final String role) {
        //Given
        final Event event = new Event();
        event.setName("TEST EVENT");

        final EventUser eventUser = new EventUser();
        eventUser.setRole(Role.valueOf(role.toUpperCase()));
        eventUser.setEvent(event);

        final EventUserDto eventUserDto = new EventUserDto();
        eventUserDto.setRole(Role.valueOf(role.toUpperCase()));
        eventUserDto.setEvent_name("TEST EVENT");
        final List<EventUserDto> expectedResult = List.of(eventUserDto);

        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.of(new Event()));
        when(eventUserService.findEventUserByEventAndRole(any(), any()))
                .thenReturn(List.of(eventUser));
        when(eventUserMapper.toDtoCollection(any())).thenReturn(expectedResult);

        //When
        final List<EventUserDto> result = eventService
                .getAllUsersByEventAndRole("TEST EVENT", role);

        //Then
        verify(eventRepository).findFirstByName(anyString());
        verify(eventUserService).findEventUserByEventAndRole(any(), any());
        assertEquals(expectedResult, result);
    }

    @Test
    void getAllEvents_whenNonExistingUser() {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.getAllEvents("testEmail");
        });
    }


    @Test
    void getAllEvents() {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(eventUserService.getAllEventUsersByUserAndRole(any(), any())).thenReturn(new ArrayList<>());
        when(eventMapper.toDtoCollection(any())).thenReturn(new ArrayList<>());

        final Map<String, List<EventDto>> result = eventService.getAllEvents("TEST");

        verify(eventMapper, times(3)).toDtoCollection(any());
        assertEquals(new ArrayList<>(), result.get("admin"));
        assertEquals(new ArrayList<>(), result.get("planner"));
        assertEquals(new ArrayList<>(), result.get("guest"));
    }


    @Test
    void getAllTasksByEmail_whenNotFoundEvent() {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.getAllTasksByEmail("Test");
        });
    }

    @Test
    void getAllTasksByEmail() {
        //Given
        final Task task = new Task();
        task.setName("Task1");

        final Event event = new Event();
        event.setName("TEST");
        event.setTasks(Set.of(task));

        final EventUser eventUser = new EventUser();
        eventUser.setEvent(event);

        final TaskDto taskDto = new TaskDto();
        task.setName("Task1");
        final List<TaskDto> taskDtoList = List.of(taskDto);

        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(taskService.getTasksByAdmin(anyString())).thenReturn(List.of(task));
        when(eventUserService.getAllEventUsersByUserAndRole(any(), any()))
                .thenReturn(List.of(eventUser));
        when(taskMapper.toDtoCollection(any())).thenReturn(taskDtoList);

        //When
        final List<TaskDto> result = eventService.getAllTasksByEmail("test@test.com");

        //Then
        verify(userService).findUserByEmail(anyString());
        verify(taskService).getTasksByAdmin(anyString());
        verify(eventUserService).getAllEventUsersByUserAndRole(any(), any());
        verify(taskMapper).toDtoCollection(any());
    }

    @Test
    void getCurrentEventByName_whenNotFoundEvent() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.getCurrentEventByName("Test");
        });
    }

    @Test
    void getCurrentEventByName() {
        //Given
        final Event event = new Event();
        event.setName("TEST");
        event.setDescription("TEST DESC");

        final EventDto expectedDto = new EventDto();
        expectedDto.setName("TEST");
        expectedDto.setDescription("TEST DESC");
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.of(event));
        when(eventMapper.toDto(any())).thenReturn(expectedDto);

        assertEquals(eventService.getCurrentEventByName("TEST"), expectedDto);
    }

    @Test
    void getAllTasksByEvent_whenNonValidName() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.getAllTasksByEvent("Test");
        });
    }

    @Test
    void getAllTasksByEvent() {
        //Given
        final Task task = new Task();
        task.setName("TEST TASK");

        final Event event = new Event();
        event.setName("TEST Event");
        event.setTasks(Set.of(task));

        final TaskDto taskDto = new TaskDto();
        task.setName("TEST TASK");

        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.of(event));
        when(taskMapper.toDtoCollection(any())).thenReturn(List.of(taskDto));
        //When
        final List<TaskDto> result = eventService.getAllTasksByEvent("TEST Event");

        //Then
        assertEquals(List.of(taskDto), result);
        verify(eventRepository).findFirstByName(anyString());
        verify(taskMapper).toDtoCollection(any());
    }

    @Test
    void getTaskByName() {
    }

    @Test
    void getAssigneesByTaskName() {
    }

    @Test
    void addEvent() {
    }

    @Test
    void addTask() {
    }

    @Test
    void addUserToEvent() {

    }

    @Test
    void addAssigneeToTask_whenNonExistingName() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.addAssigneeToTask("Test", "Task", "Test email");
        });
    }


    @Test
    void addAssigneeToTask() {
        //Given
        final Task task = new Task();
        task.setName("TEST TASK");

        final Event event = new Event();
        event.setName("TEST EVENT");
        event.setTasks(Set.of(task));

        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.of(event));
        doNothing().when(taskService).addAssigneeForTask(anyString(), anyString());

        //When
        eventService.addAssigneeToTask("TEST Event", "TEST TASK", "TEST EMAIL");

        //Then
        verify(eventRepository, times(2)).findFirstByName(anyString());
        verify(taskService).addAssigneeForTask(anyString(), anyString());
    }

    @Test
    void updateEventByName_whenNonExistingName() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.updateEventByName("Test", "Task", "Test", any());
        });
    }

    @Test
    void updateEventByName_whenNullData() {
        //Given
        final Event event = new Event();
        event.setName("TEST");

        final EventDto eventDto = new EventDto();
        event.setName("TEST");

        when(eventRepository.findFirstByName(anyString()))
                .thenReturn(Optional.of(event));
        when(eventMapper.toDto(any())).thenReturn(eventDto);

        //When
        final EventDto result = eventService.updateEventByName("Test", null, null, LocalDate.now());

        //Then
        verify(eventRepository).findFirstByName(any());
        verify(eventRepository).save(any());
        assertEquals(eventDto, result);
    }

    @Test
    void updateEventByName() {
        //Given
        final Event event = new Event();
        event.setName("TEST");
        event.setDescription("TEST DESC");
        event.setLocation("LOCATION");

        final EventDto eventDto = new EventDto();
        eventDto.setName("TEST");
        eventDto.setDescription("UPDATED DESC");
        eventDto.setLocation("NEW LOC");

        when(eventRepository.findFirstByName(anyString()))
                .thenReturn(Optional.of(event));
        when(eventMapper.toDto(any())).thenReturn(eventDto);

        //When
        final EventDto result = eventService.updateEventByName("TEST", "UPDATED DESC", "NEW LOC", LocalDate.now());

        //Then
        verify(eventRepository).findFirstByName(any());
        verify(eventRepository).save(any());
        assertEquals(eventDto, result);
    }

    @Test
    void updateTask_whenNonExistingName() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.updateTask("Test", "Task", any());
        });
    }

    @Test
    @Disabled
    void updateTask() {
//Given
        final Task task = new Task();
        task.setName("TASK");
        final TaskDto taskDto = new TaskDto();
        taskDto.setName("TASK");
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.of(new Event()));
        when(taskService.updateTaskByName(anyString(), anyString(), any(), any(), anyList()))
                .thenReturn(task);
        when(taskMapper.toDto(any())).thenReturn(taskDto);

        //When
        final TaskDto result = eventService.updateTask("Test", "Task", new UpdateTaskDto());

        //Then
        verify(eventRepository).findFirstByName(any());
        verify(taskService).updateTaskByName(anyString(), anyString(), any(), any(), anyList());
        verify(taskMapper).toDto(any());
        assertEquals(taskDto, result);
    }

    @Test
    void deleteEvent_whenNonExistingName() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.deleteEvent("Test");
        });
    }

    @Test
    void deleteEvent() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.of(new Event()));
        doNothing().when(eventRepository).delete(any());

        eventService.deleteEvent("TEST");

        verify(eventRepository).findFirstByName(anyString());
        verify(eventRepository).delete(any());
    }

    @Test
    void deleteTask_whenNonExistingName() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.deleteTask("Test", "task");
        });
    }

    @Test
    void deleteTask() {
        when(eventRepository.findFirstByName(anyString())).thenReturn(Optional.of(new Event()));
        doNothing().when(taskService).removeTask(any(), anyString());

        eventService.deleteTask("test event", "test task");

        verify(eventRepository).findFirstByName(anyString());
        verify(taskService).removeTask(any(), anyString());
    }

}