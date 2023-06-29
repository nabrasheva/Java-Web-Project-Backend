package com.fmi.project.controller;

import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.dto.AddUserToEventDto;
import com.fmi.project.dto.EventDto;
import com.fmi.project.dto.TaskDto;
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

    //TODO: @GetMapping to output tasks from user with username

    //TODO: @DeleteMapping to remove a user from event eventId

    //TODO: @DeleteMapping to remove an assignee from task with taskId

    /**
     *   @param     email
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

        return new ResponseEntity<>(map, HttpStatus.OK);//eventMapper.toDtoCollection(allEvents);
    }

    /**
     *   @param     email
     *   @return    all tasks by the entered email
     */
    @GetMapping("/{email}/tasks")
    public ResponseEntity<Object> getAllTasksByUsername(@PathVariable String email){
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User does not exist!"));

//        List<Task> assignedTasks = taskService.getTasksByAssignee(user);
        List<Task> adminTasks = taskService.getTasksByAdmin(email);

//        List<Task> allTasks = Stream.concat(assignedTasks.stream(), adminTasks.stream())
//                .distinct()
//                .collect(Collectors.toList());

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
     *   @param  name
     *   @return the event with the corresponding name
     */
    @GetMapping("/event/{name}")
    public ResponseEntity<EventDto> getEventById(@PathVariable String name){
        Event event = eventService.getEventByName(name).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        return new ResponseEntity<>(eventMapper.toDto(event), HttpStatus.OK);
    }

    /**
     *
     * @param  name
     * @return all tasks, which are contained in the event with eventName
     */
    @GetMapping("/event/{name}/tasks")
    public ResponseEntity<Object> getAllTasksByEventId(@PathVariable String name){
        List<Task> tasks = eventService.getAllTasksByEvent(name);

        return new ResponseEntity<>(taskMapper.toDtoCollection(tasks), HttpStatus.OK);
    }

    /**
     *
     * @param eventName
     * @param taskName
     * @return the task with corresponding taskName and eventName
     */
    @GetMapping("/event/{eventName}/tasks/{taskName}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable String eventName,
                               @PathVariable String taskName){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event!"));

        Task task = eventService.getTaskByEventNameAndTaskName(eventName, taskName);

        return new ResponseEntity<>(taskMapper.toDto(task), HttpStatus.OK);
    }

    /**
     *
     * @param eventName
     * @param taskName
     * @return assignees, which are assigned to the task with taskName
     */
    @GetMapping("/event/{eventName}/tasks/{taskName}/assignees")
    public ResponseEntity<Object> getAssigneesByTaskId(@PathVariable String eventName,
                                                        @PathVariable String taskName){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event!"));

        return new ResponseEntity<>(taskService.getAssigneesByTaskName(taskName), HttpStatus.OK);
    }

    /**
     *
     * @param  eventDto
     * @param email
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
     * @param eventName
     * @param taskDto
     * @return response, which tell us, that the task is successfully added in the event with eventId
     */
    @PostMapping("/event/{eventName}/newTask") //TaskDto
    public ResponseEntity<String> addTaskByEventId(@PathVariable String eventName,
                                                   @RequestBody TaskDto taskDto){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        Task newTask = taskMapper.toEntity(taskDto);

        taskService.addTask(event, newTask);

        return new ResponseEntity<String>("Successfully added task", HttpStatus.OK);
    }

    /**
     *
     * @param eventName
     * @param addUserToEventDto
     * @return response with message for successfully added user to event with eventId
     */
    @PostMapping("/event/{eventName}/newUser")
    public ResponseEntity<String> addUserToEvent(@PathVariable String eventName,
                                                 @RequestBody AddUserToEventDto addUserToEventDto){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        eventService.addUserToEvent(eventName, addUserToEventDto.getEmail(),
                                    addUserToEventDto.getRole(), addUserToEventDto.getCategory());

        return new ResponseEntity<String>("Successfully added user to event", HttpStatus.OK);
    }

    /**
     *
     * @param eventName
     * @param taskName
     * @return response with message for successfully added assignee to task with taskId
     */
    @PostMapping("/event/{eventName}/tasks/{taskName}/newAssignee")
    public ResponseEntity<String> addAssigneeToTask(@PathVariable String eventName,
                                                    @PathVariable String taskName,
                                                    @RequestBody Map<String, String> json){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        Task task = eventService.getTaskByEventNameAndTaskName(eventName, taskName);

        taskService.addAssigneeForTask(taskName, json.get("username"));

        return new ResponseEntity<>("Successfully added assignee to task", HttpStatus.OK);
    }

    /**
     *
     * @param eventName
     * @param toUpdateEventDto
     * @return eventDto
     */
    @PatchMapping("/event/{eventName}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable String eventName,
                                                @RequestBody EventDto toUpdateEventDto){

        Event event = eventService.updateEventById(eventName, toUpdateEventDto.getDescription(),
                toUpdateEventDto.getLocation(), toUpdateEventDto.getDate());

        return new ResponseEntity<>(eventMapper.toDto(event), HttpStatus.OK);
    }

    /**
     *
     * @param eventName
     * @param taskName
     * @param toUpdateTaskDto
     * @return taskDto
     */
    @PatchMapping("/event/{eventName}/tasks/{taskName}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable String eventName,
                              @PathVariable String taskName,
                              @RequestBody TaskDto toUpdateTaskDto){

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        Task task = taskService.updateTaskById(taskName, toUpdateTaskDto.getDescription(),
                                    toUpdateTaskDto.getDueDate(), toUpdateTaskDto.getStatus());

        return new ResponseEntity<>(taskMapper.toDto(task), HttpStatus.OK);
    }

    /**
     *
     * @param eventName
     * @return response, which tell us, that the event with eventName is successfully deleted
     */
    @DeleteMapping("/event/{eventName}")
    public ResponseEntity<String> deleteEvent(@PathVariable String eventName){
        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));
        eventService.deleteEvent(event);

        return new ResponseEntity<String>("Successfully deleted event", HttpStatus.OK);
    }

    /**
     *
     * @param eventName
     * @param taskName
     * @return response, which tell us, that the task with taskName is successfully deleted in the event with eventName
     */
    @DeleteMapping("/event/{eventName}/tasks/{taskName}")
    public ResponseEntity<String> deleteTask(@PathVariable String eventName,
                                             @PathVariable String taskName) {

        Event event = eventService.getEventByName(eventName).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        taskService.removeTask(event, taskName);

        return new ResponseEntity<String>("Successfully deleted task", HttpStatus.OK);
    }

}
