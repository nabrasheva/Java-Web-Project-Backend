package com.fmi.project.controller;

import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.dto.*;
import com.fmi.project.enums.Role;
import com.fmi.project.mapper.EventMapper;
import com.fmi.project.mapper.TaskMapper;
import com.fmi.project.model.Event;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.service.EventService;
import com.fmi.project.service.TaskService;
import com.fmi.project.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/events")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    private final UserService userService;

    //TODO: @DeleteMapping to remove a user from event eventId

    //TODO: @DeleteMapping to remove an assignee from task with taskId

    /**
     *   @param     email email of the user
     *   @return    all events, in which the user with this username participate
     *              regardless of the role
     */
    @GetMapping("/{email}")
    public ResponseEntity<Object> getAllEvents(@PathVariable String email){
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User does not exist!"));

        List<EventDto> userAdminEvents = eventMapper.toDtoCollection(eventService.getEventsByRoleAndUser(Role.ADMIN, user));
        List<EventDto> userPlannerEvents = eventMapper.toDtoCollection(eventService.getEventsByRoleAndUser(Role.PLANNER, user));
        List<EventDto> userGuestEvents = eventMapper.toDtoCollection(eventService.getEventsByRoleAndUser(Role.GUEST, user));

        Map<String, List<EventDto>> map = new HashMap<>();

        map.put("admin", userAdminEvents);
        map.put("planner", userPlannerEvents);
        map.put("guest", userGuestEvents);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     *   @param     email email of the user
     *   @return    all tasks by the entered email
     */
    @GetMapping("/{email}/tasks")
    public ResponseEntity<Object> getAllTasksByEmail(@PathVariable String email){
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User does not exist!"));

        List<Task> adminTasks = taskService.getTasksByAdmin(email);

        List<Event> userPlannerEvents = eventService.getEventsByRoleAndUser(Role.PLANNER, user);
        List<Task> assignedTasks = userPlannerEvents.stream()
                                    .flatMap(event -> event.getTasks().stream())
                                    .filter(task -> task.getAssignees().contains(user))
                                    .distinct()
                                    .toList();

        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(adminTasks);
        allTasks.addAll(assignedTasks);

        return new ResponseEntity<>(taskMapper.toDtoCollection(allTasks), HttpStatus.OK);
    }

    /**
     *   @param  name name of the event
     *   @return the event with the corresponding name
     */
    @GetMapping("/event/{name}")
    public ResponseEntity<EventDto> getEventByName(@PathVariable String name){
        Event event = eventService.getEventByName(name).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        return new ResponseEntity<>(eventMapper.toDto(event), HttpStatus.OK);
    }

    /**
     *
     * @param  name name of the event
     * @return all tasks, which are contained in the event with eventName
     */
    @GetMapping("/event/{name}/tasks")
    public ResponseEntity<Object> getAllTasksByEventName(@PathVariable String name){
        List<Task> tasks = eventService.getAllTasksByEvent(name);

        return new ResponseEntity<>(taskMapper.toDtoCollection(tasks), HttpStatus.OK);
    }

    /**
     *
     * @param eventName name of the event
     * @param taskName name of the task
     * @return the task with corresponding taskName and eventName
     */
    @GetMapping("/event/{eventName}/tasks/{taskName}")
    public ResponseEntity<TaskDto> getTaskByName(@PathVariable String eventName,
                               @PathVariable String taskName){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event!"));

        Task task = eventService.getTaskByEventNameAndTaskName(eventName, taskName);

        return new ResponseEntity<>(taskMapper.toDto(task), HttpStatus.OK);
    }

    /**
     *
     * @param eventName name of the event
     * @param taskName name of the task
     * @return assignees, which are assigned to the task with taskName
     */
    @GetMapping("/event/{eventName}/tasks/{taskName}/assignees")
    public ResponseEntity<Object> getAssigneesByTaskName(@PathVariable String eventName,
                                                        @PathVariable String taskName){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event!"));

        Map<String, Object> response = new HashMap<>();
        response.put("assignees", taskService.getAssigneesByTaskName(taskName));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param  eventDto request, that contains information about the event, which will be added
     * @param email email of the user
     * @return response, which tell us, that the event is successfully added
     */
    @PostMapping("/newEvent/{email}")
    public ResponseEntity<Object> addEvent(@PathVariable String email, @RequestBody EventDto eventDto){
        Event newEvent = eventMapper.toEntity(eventDto);
        eventService.addEvent(newEvent, email);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully added event");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param eventName name of the event
     * @param taskDto request, that contains information about the task, which will be added
     * @return response, which tell us, that the task is successfully added in the event with eventName
     */
    @PostMapping("/event/{eventName}/newTask") //TaskDto
    public ResponseEntity<Object> addTaskByEventName(@PathVariable String eventName,
                                                   @RequestBody TaskDto taskDto){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        Task newTask = taskMapper.toEntity(taskDto);
        taskService.addTask(event, newTask);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully added task");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param eventName name of the event
     * @param addUserToEventDto request, that contains information about the user, who will be added to this event
     * @return response with message for successfully added user to event with eventId
     */
    @PostMapping("/event/{eventName}/newUser")
    public ResponseEntity<Object> addUserToEvent(@PathVariable String eventName,
                                                 @RequestBody AddUserToEventDto addUserToEventDto){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        eventService.addUserToEvent(eventName, addUserToEventDto.getEmail(),
                                    addUserToEventDto.getRole(), addUserToEventDto.getCategory());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully added user to event");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param eventName name of the event
     * @param taskName name of the task
     * @param emailDto request
     * @return response with message for successfully added assignee to task with taskId
     */
    @PostMapping("/event/{eventName}/tasks/{taskName}/newAssignee")
    public ResponseEntity<Object> addAssigneeToTask(@PathVariable String eventName,
                                                    @PathVariable String taskName,
                                                    @RequestBody EmailDto emailDto){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        Task task = eventService.getTaskByEventNameAndTaskName(eventName, taskName);
        taskService.addAssigneeForTask(taskName, emailDto.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully added assignee to task");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param eventName name of the event
     * @param toUpdateEventDto request, that contains information about the event with new data for this event
     * @return eventDto
     */
    @PatchMapping("/event/{eventName}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable String eventName,
                                                @RequestBody EventDto toUpdateEventDto){

        Event event = eventService.updateEventByName(eventName, toUpdateEventDto.getDescription(),
                toUpdateEventDto.getLocation(), toUpdateEventDto.getDate());

        return new ResponseEntity<>(eventMapper.toDto(event), HttpStatus.OK);
    }

    /**
     *
     * @param eventName name of the event
     * @param taskName name of the task
     * @param toUpdateTaskDto request, that contains information about the task with new data for this task
     * @return taskDto
     */
    @PatchMapping("/event/{eventName}/tasks/{taskName}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable String eventName,
                              @PathVariable String taskName,
                              @RequestBody UpdateTaskDto toUpdateTaskDto){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        Task task = taskService.updateTaskByName(taskName, toUpdateTaskDto.getDescription(),
                                    toUpdateTaskDto.getDueDate(), toUpdateTaskDto.getStatus());

        return new ResponseEntity<>(taskMapper.toDto(task), HttpStatus.OK);
    }

    /**
     *
     * @param eventName name of the event
     * @return response, which tell us, that the event with eventName is successfully deleted
     */
    @DeleteMapping("/event/{eventName}")
    public ResponseEntity<Object> deleteEvent(@PathVariable String eventName){
        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        eventService.deleteEvent(event);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully deleted event");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param eventName name of the event
     * @param taskName name of the task
     * @return response, which tell us, that the task with taskName is successfully deleted in the event with eventName
     */
    @DeleteMapping("/event/{eventName}/tasks/{taskName}")
    public ResponseEntity<Object> deleteTask(@PathVariable String eventName,
                                             @PathVariable String taskName) {

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        taskService.removeTask(event, taskName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully deleted task");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
