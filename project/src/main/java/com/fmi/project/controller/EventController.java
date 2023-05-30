package com.fmi.project.controller;

import com.fmi.project.controller.validation.ApiBadRequest;
import com.fmi.project.dto.EventDto;
import com.fmi.project.dto.TaskDto;
import com.fmi.project.mapper.EventMapper;
import com.fmi.project.mapper.TaskMapper;
import com.fmi.project.model.Event;
import com.fmi.project.model.Task;
import com.fmi.project.service.EventService;
import com.fmi.project.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final TaskService taskService;
    private final TaskMapper taskMapper;

//    @GetMapping
//    public List<EventDto> getAllEvents(Authentication authentication){ // Authentication obejct -> to retrieve the information from the logged user
//                                            // How to test that in Postman???
//        User currentUser = authentication.getPrincipal();
//
//        List<Event> events =
//        return eventMapper.toDtoCollection(events);
//    }

    //.........................................
//    @GetMapping("/{eventId}/tasks")
//    public List<TaskDto> getAllTasks(@PathVariable(name = "eventId") Long eventId){
//        List<Task> tasks = eventService.getAllTasksByEventId(eventId);
//        return taskMapper.toDtoCollection(tasks);
//    }

//    @GetMapping
//    public List<Event> getEvents(){
//        final List<Event> events = eventService.getEvents();
//
//        return mapper.toDtoCollection(events);
//    }
    //............................................

    //@PostMapping("/{eventId}/newEvent")
    //public EventDto addEvent(@PathVariable(name = "eventId") Long eventId, @RequestBody EventDto eventDto){
    //    // in this case I also have to get the User data from the session
    //    //in order to get his username and to use it as a parameter in the addEvent function
//
    //    //...
//
    //    //return eventService.addEvent(eventMapper.toEntity(eventDto));
    //}

    @PostMapping("/newEvent") //EventDto
    public ResponseEntity<String> addEvent(@RequestBody EventDto eventDto){
        // TODO:in this case I also have to get the User data from the session in order to get his username and to use it as a parameter in the addEvent function
        //User user = userService.findUserByUsername("Niya123").orElse(null);

        Event newEvent = eventMapper.toEntity(eventDto);
        //...
        eventService.addEvent(newEvent, "Tsvetina");

        return new ResponseEntity<String>("Successfully added", HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable(value = "eventId") Long eventId){
        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }
        return eventMapper.toDto(event);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable(name = "eventId") Long eventId, @RequestBody EventDto updatedEventDto){
        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }

        eventService.updateEventById(event.getId(), updatedEventDto.getDescription(),
                updatedEventDto.getLocation(), updatedEventDto.getDate());

        return eventMapper.toDto(event);
    }

    @DeleteMapping("/{id}") //EventDto
    public ResponseEntity<String> deleteEvent(@PathVariable(value = "id") Long eventId){
        Event event = eventService.getEventById(eventId).orElse(null);

        if(event == null){
            throw new ApiBadRequest("There is no such event");
        }

        eventService.deleteEvent(event);

        //TODO: to output a message, not DTO object
        //return eventMapper.toDto(event);
        return new ResponseEntity<String>("Successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/{eventId}/tasks")
    public List<TaskDto> getAllTasks(@PathVariable(name = "eventId") Long eventId){
        final Event event = eventService.getEventById(eventId).orElseThrow();
        List<Task> tasks = taskService.getAllTasksByEventId(event);
        return taskMapper.toDtoCollection(tasks);
    }


    @DeleteMapping("{event_id}/tasks/{id}") //EventDto
    public ResponseEntity<String> deleteTask(@PathVariable(value = "event_id") Long eventId,
                                             @PathVariable(value = "id") Long taskId) {
        final Event event = eventService.getEventById(eventId).orElse(null);

        if (event == null) {
            throw new ApiBadRequest("There is no such event or task");
        }

        taskService.removeTask(event, taskId);
        //TODO: to output a message, not DTO object
        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }

//    @GetMapping("/{eventId}/{taskId}")
//    public TaskDto getTaskById(@PathVariable(name = "eventId") Long eventId, @PathVariable(name = "taskId") Long taskId){
//        //method for getting a task by taskId and eventId
//    }

//    @PostMapping("/{eventId}/newTask")
//    public TaskDto addTaskById(@PathVariable(name = "eventId") Long eventId, @RequestBody TaskDto taskDto){
//        Event event = eventService.getEventById(eventId).orElse(null);
//        Task task = eventService.getTaskById(taskId).orElse(null);
//
//        if(event == null || task == null){
//            throw new ApiBadRequest("There is no such event or task");
//        }
//
//        taskService.addTask(event, task);
//
//        return taskMapper.toDto(task);
//    }



}
