package com.fmi.project.service;

import com.fmi.project.controller.validation.ObjectFoundException;
import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.dto.*;
import com.fmi.project.enums.Category;
import com.fmi.project.enums.Role;
import com.fmi.project.mapper.EventMapper;
import com.fmi.project.mapper.EventUserMapper;
import com.fmi.project.mapper.TaskMapper;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.SqlReturnType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventUserService eventUserService;
    private final EventUserMapper eventUserMapper;
    private final EventMapper eventMapper;
    private final TaskMapper taskMapper;
    private final TaskService taskService;
    private final UserService userService;


    public List<EventUserDto> getAllUsersByEventAndRole(final String eventName,
                                                        final String role) {
        final Event event = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("No such event!"));
        final List<EventUser> eventUsers;
        if (Objects.equals(role, "planner")) {
            eventUsers = eventUserService.findEventUserByEventAndRole(event, Role.PLANNER);
        } else if (Objects.equals(role, "guest")) {
            eventUsers = eventUserService.findEventUserByEventAndRole(event, Role.GUEST);
        } else throw new ObjectNotFoundException("Could not find such role!");

        return eventUserMapper.toDtoCollection(eventUsers);
    }

    public Map<String, List<EventDto>> getAllEvents(final String email) {
        final User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User does not exist!"));

        final List<EventDto> userAdminEvents = eventMapper.toDtoCollection(getEventsByRoleAndUser(Role.ADMIN, user));
        final List<EventDto> userPlannerEvents = eventMapper.toDtoCollection(getEventsByRoleAndUser(Role.PLANNER, user));
        final List<EventDto> userGuestEvents = eventMapper.toDtoCollection(getEventsByRoleAndUser(Role.GUEST, user));

        final Map<String, List<EventDto>> map = new HashMap<>();

        map.put("admin", userAdminEvents);
        map.put("planner", userPlannerEvents);
        map.put("guest", userGuestEvents);

        return map;
    }


    public List<TaskDto> getAllTasksByEmail(final String email) {
        final User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User does not exist!"));

        final List<Task> adminTasks = taskService.getTasksByAdmin(email);

        final List<Event> userPlannerEvents = getEventsByRoleAndUser(Role.PLANNER, user);
        final List<Task> assignedTasks = userPlannerEvents.stream()
                .flatMap(event -> event.getTasks().stream())
                .filter(task -> task.getAssignees().contains(user))
                .distinct()
                .toList();

        final List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(adminTasks);
        allTasks.addAll(assignedTasks);

        return taskMapper.toDtoCollection(allTasks);
    }

    public EventDto getCurrentEventByName(final String name) {
        final Event event = eventRepository.findFirstByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event"));
        return eventMapper.toDto(event);
    }

    public List<TaskDto> getAllTasksByEvent(final String name) {
        final Event event1 = eventRepository.findFirstByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid event!"));

        final List<Task> tasks = event1.getTasks().stream().toList();

        return taskMapper.toDtoCollection(tasks);
    }

    public TaskDto getTaskByName(final String eventName, final String taskName) {
        final Event event = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event!"));

        final Task task = getTaskByEventNameAndTaskName(eventName, taskName);

        return taskMapper.toDto(task);
    }

    public Map<String, Object> getAssigneesByTaskName(final String eventName, final String taskName) {
        final Event event = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event!"));


        final Map<String, Object> response = new HashMap<>();
        response.put("assignees", taskService.getAssigneesByTaskName(taskName));

        return response;
    }

    public void addEvent(EventDto eventDto, String email) {
        final Event newEvent = eventMapper.toEntity(eventDto);

        if (newEvent == null) {
            throw new IllegalArgumentException("Incorrect data");
        }

        final List<Event> eventList = eventRepository.findAll();
        final User newEventCreator = userService.findUserByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User do not exist!"));

        eventList.forEach(event1 -> {

            final EventUser eventUserForCreator = eventUserService.findFirstByEventAndRoleAdmin(event1)
                    .orElseThrow(() ->
                    new ObjectNotFoundException("EventUser row does not exist!"));

            final User eventCreator = eventUserForCreator.getUser();

            if (eventCreator.getId().equals(newEventCreator.getId()) && newEvent.getName().equals(event1.getName())) {
                throw new ObjectFoundException("Event already exists!");
            }
        });

        eventRepository.save(newEvent);

        final EventUser newEventUser = EventUser.builder()
                .event(newEvent)
                .user(newEventCreator)
                .role(Role.ADMIN)
                .category(Category.NONE)
                .build();

        eventUserService.addEventUser(newEventUser);
    }

    public void addTask(final String eventName, final TaskDto taskDto) {
        final Event event = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        final Task newTask = taskMapper.toEntity(taskDto);
        newTask.setEvent(event);
        newTask.setAssignees(taskDto.getAssignees().stream()
                .map(userService::findUserByEmail)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet()));

        taskService.addTask(event, newTask);

//        taskDto.getAssignees().forEach(assigneeEmail->{
//            taskService.addAssigneeForTask(newTask.getName(),assigneeEmail);
//        });
    }

    public void addUserToEvent(final String eventName, final AddUserToEventDto addUserToEventDto) {

        final Event event = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        addUserToEvent(eventName, addUserToEventDto.getEmail(),
                addUserToEventDto.getRole(), addUserToEventDto.getCategory());
    }

    public void addAssigneeToTask(final String eventName, final String taskName, final String email) {
        final Event event = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        final Task task = getTaskByEventNameAndTaskName(eventName, taskName);

        taskService.addAssigneeForTask(taskName, email);
    }

    public EventDto updateEventByName(String name, String description, String location, LocalDate date) {
        final Event event = eventRepository.findFirstByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        if (nonNull(description)) event.setDescription(description);

        if (nonNull(location)) event.setLocation(location);

        if (nonNull(date)) event.setDate(date);

        eventRepository.save(event);

        return eventMapper.toDto(event);
    }

    public TaskDto updateTask(final String eventName, final String taskName, final UpdateTaskDto toUpdateTaskDto) {
        final Event event = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        final Task task = taskService.updateTaskByName(taskName, toUpdateTaskDto.getDescription(),
                toUpdateTaskDto.getDueDate(), toUpdateTaskDto.getStatus(), toUpdateTaskDto.getAssignees());

        return taskMapper.toDto(task);
    }

    public void deleteEvent(final String eventName) {

        final Event event = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        if (event == null) {
            throw new IllegalArgumentException("Incorrect data");
        }
        try {
            eventRepository.delete(event);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void deleteTask(final String eventName, final String taskName) {
        final Event event = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        taskService.removeTask(event, taskName);
    }

    private Task getTaskByEventNameAndTaskName(String eventName, String taskName) {
        final Event event1 = eventRepository.findFirstByName(eventName)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid event!"));

        final Task task1 = event1.getTasks().stream()
                .filter(task -> task.getName().equals(taskName))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Invalid task for this event"));

        return task1;
    }

    private void addUserToEvent(String eventName, String email, Role role, Category category) {
        final Event event = eventRepository.findFirstByName(eventName).orElse(null);
        final User user = userService.findUserByEmail(email).orElse(null);

        if (nonNull(event) && nonNull(user)) {
            final EventUser eventUser = eventUserService.findFirstByEventAndUser(event, user).orElse(null);
            if (nonNull(eventUser)) throw new ObjectFoundException("User is already added to event!");
            EventUser createdEventUser;
            if (role == Role.GUEST) {
                createdEventUser = EventUser.builder()
                        .event(event)
                        .role(Role.GUEST)
                        .category(category)
                        .user(user)
                        .build();
            } else {
                createdEventUser = EventUser.builder()
                        .event(event)
                        .role(role)
                        .user(user)
                        .build();
            }

            eventUserService.addEventUser(createdEventUser);
            event.getEventUsers().add(createdEventUser);
            eventRepository.save(event);

        }
    }

    private List<Event> getEventsByRoleAndUser(Role role, User user) {
        final List<EventUser> list = eventUserService.getAllEventUsersByUserAndRole(user, role);

        final List<Event> eventList = new ArrayList<>();
        list.forEach(eventUser -> eventList.add(eventUser.getEvent()));

        return eventList;
    }


}
