package com.fmi.project.controller;

import com.fmi.project.controller.validation.ApiBadRequest;
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
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/events")
@AllArgsConstructor
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
     *   @param     username
     *   @return    all events, in which the user with this username participate
     *              regardless of the role
     */
    @GetMapping("/{username}")
    public List<EventDto> getAllEvents(@PathVariable(name = "username") String username){
        User user = userService.findUserByUsername(username).orElse(null);

        if(user == null){
            throw new ApiBadRequest("There is no such user");
        }

        List<Event> userAdminEvents = eventService.getEventsByRoleAndUser(Role.ADMIN, user);
        List<Event> userPlannerEvents = eventService.getEventsByRoleAndUser(Role.PLANNER, user);
        List<Event> userGuestEvents = eventService.getEventsByRoleAndUser(Role.GUEST, user);

        List<Event> allEvents = new ArrayList<>();
        allEvents.addAll(userAdminEvents);
        allEvents.addAll(userPlannerEvents);
        allEvents.addAll(userGuestEvents);

        return eventMapper.toDtoCollection(allEvents);
    }

    @GetMapping("/{username}/tasks")
    public List<TaskDto> getAllTasksByUsername(@PathVariable(name = "username")String username){
        User user = userService.findUserByUsername(username).orElse(null);

        if(user == null){
            throw new ApiBadRequest("There is no such user");
        }

//        List<Task> assignedTasks = taskService.getTasksByAssignee(user);
        List<Task> adminTasks = taskService.getTasksByAdmin(username);

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

        return taskMapper.toDtoCollection(allTasks);
    }

    /**
     *   @param  eventId
     *   @return the event with the corresponding eventId
     */
    @GetMapping("/event/{eventId}")
    public EventDto getEventById(@PathVariable(value = "eventId") Long eventId){
        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }

        return eventMapper.toDto(event);
    }

    /**
     *
     * @param  eventId
     * @return all tasks, which are contained in the event with eventID
     */
    @GetMapping("/event/{eventId}/tasks")
    public List<TaskDto> getAllTasksByEventId(@PathVariable(name = "eventId") Long eventId){
        List<Task> tasks = eventService.getAllTasksByEvent(eventId);
        return taskMapper.toDtoCollection(tasks);
    }

    /**
     *
     * @param eventId
     * @param taskId
     * @return the task with corresponding taskId and eventId
     */
    @GetMapping("/event/{eventId}/tasks/{taskId}")
    public TaskDto getTaskById(@PathVariable(name = "eventId") Long eventId,
                               @PathVariable(name = "taskId") Long taskId){

        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event!");
        }

        Task task = eventService.getTaskByEventIdAndTaskId(eventId, taskId);

        return taskMapper.toDto(task);
    }

    /**
     *
     * @param eventId
     * @param taskId
     * @return assignees, which are assigned to the task with taskId
     */
    @GetMapping("/event/{eventId}/tasks/{taskId}/assignees")
    public List<String> getAssigneesByTaskId(@PathVariable(name = "eventId") Long eventId,
                                             @PathVariable(name = "taskId") Long taskId){

        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event!");
        }

        return taskService.getAssigneesByTaskId(taskId);
    }

    /**
     *
     * @param  eventDto
     * @return response, which tell us, that the event is successfully added
     */
    @PostMapping("/newEvent") //EventDto
    public ResponseEntity<String> addEvent(@RequestBody EventDto eventDto){
        Event newEvent = eventMapper.toEntity(eventDto);
        eventService.addEvent(newEvent, "Tsvetina");

        return new ResponseEntity<String>("Successfully added event", HttpStatus.OK);
    }

    /**
     *
     * @param eventId
     * @param taskDto
     * @return response, which tell us, that the task is successfully added in the event with eventId
     */
    @PostMapping("/event/{eventId}/newTask") //TaskDto
    public ResponseEntity<String> addTaskByEventId(@PathVariable(name = "eventId") Long eventId,
                                                   @RequestBody TaskDto taskDto){

        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }

        Task newTask = taskMapper.toEntity(taskDto);

        taskService.addTask(event, newTask);

        return new ResponseEntity<String>("Successfully added task", HttpStatus.OK);
    }

    /**
     *
     * @param eventId
     * @param addUserToEventDto
     * @return response with message for successfully added user to event with eventId
     */
    @PostMapping("/event/{eventId}/newUser")
    public ResponseEntity<String> addUserToEvent(@PathVariable(name = "eventId") Long eventId,
                                                 @RequestBody AddUserToEventDto addUserToEventDto){

        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }

        eventService.addUserToEvent(eventId, addUserToEventDto.getUsername(),
                                    addUserToEventDto.getRole(), addUserToEventDto.getCategory());

        return new ResponseEntity<String>("Successfully added user to event", HttpStatus.OK);
    }

    /**
     *
     * @param eventId
     * @param taskId
     * @return response with message for successfully added asignee to task with taskId
     */
    @PostMapping("/event/{eventId}/tasks/{taskId}/newAssignee")
    public ResponseEntity<String> addAssigneeToTask(@PathVariable(name = "eventId") Long eventId,
                                                    @PathVariable(name = "taskId") Long taskId,
                                                    @RequestBody Map<String, String> json){

        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }

        Task task = eventService.getTaskByEventIdAndTaskId(eventId, taskId);

        taskService.addAssigneeForTask(taskId, json.get("username"));

        return new ResponseEntity<String>("Successfully added assignee to task", HttpStatus.OK);
    }

    /**
     *
     * @param eventId
     * @param toUpdateEventDto
     * @return eventDto
     */
    @PatchMapping("/event/{eventId}")
    public EventDto updateEvent(@PathVariable(name = "eventId") Long eventId,
                                @RequestBody EventDto toUpdateEventDto){

        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }

        eventService.updateEventById(event.getId(), toUpdateEventDto.getDescription(),
                toUpdateEventDto.getLocation(), toUpdateEventDto.getDate());

        return eventMapper.toDto(event);
    }

    /**
     *
     * @param eventId
     * @param taskId
     * @param toUpdateTaskDto
     * @return taskDto
     */
    @PatchMapping("/event/{eventId}/tasks/{taskId}")
    public TaskDto updateTask(@PathVariable(name = "eventId") Long eventId,
                              @PathVariable(name = "taskId") Long taskId,
                              @RequestBody TaskDto toUpdateTaskDto){

        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }

        Task task = taskService.findByTaskId(taskId).orElse(null);

        if(task == null){
            throw new ApiBadRequest("There is no such event");
        }

        taskService.updateTaskById(task.getId(), toUpdateTaskDto.getName(), toUpdateTaskDto.getDescription(),
                                    toUpdateTaskDto.getDueDate(), toUpdateTaskDto.getStatus());

        return taskMapper.toDto(task);
    }

    /**
     *
     * @param eventId
     * @return response, which tell us, that the event with eventId is successfully deleted
     */
    @DeleteMapping("/event/{eventId}") //EventDto
    public ResponseEntity<String> deleteEvent(@PathVariable(value = "eventId") Long eventId){
        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }

        eventService.deleteEvent(event);

        return new ResponseEntity<String>("Successfully deleted event", HttpStatus.OK);
    }

    /**
     *
     * @param eventId
     * @param taskId
     * @return response, which tell us, that the task with taskId is successfully deleted in the event with eventId
     */
    @DeleteMapping("/event/{eventId}/tasks/{taskId}") //TaskDto
    public ResponseEntity<String> deleteTask(@PathVariable(value = "eventId") Long eventId,
                                             @PathVariable(value = "taskId") Long taskId) {

        Event event = eventService.getEventById(eventId).orElse(null);

        if (event == null) {
            throw new ApiBadRequest("There is no such event");
        }

        taskService.removeTask(event, taskId);

        return new ResponseEntity<String>("Successfully deleted task", HttpStatus.OK);
    }

}
