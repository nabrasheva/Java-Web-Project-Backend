package com.fmi.project.controller;

import com.fmi.project.dto.TaskDto;
import com.fmi.project.enums.Role;
import com.fmi.project.model.Task;
import com.fmi.project.service.EventService;
import com.fmi.project.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("events")
@AllArgsConstructor
public class EventController {
    private final TaskService taskService;
    private final EventService eventService;

    @GetMapping("/tasks")
    public List<String> getTasks() {
        return taskService.test().stream().map(item -> item.getId() + " " + item.getName() + "\n")
                .collect(Collectors.toList());
    }

    @PostMapping("/getEvents")
    public List<String> getEvents(@RequestBody Role test) {
        System.out.println(test);
        return eventService.testEvents().stream().map(item -> item.getId() + " " + item.getName() + "\n")
                .collect(Collectors.toList());
    }
    private final EventService eventService;
    //private final

    @Autowired
    public EventController(EventService eventService){
        this.eventService = eventService;
    }

//    @GetMapping
//    public List<Event> getEvents(){
//        final List<Event> events = eventService.getEvents();
//
//        return mapper.toDtoCollection(events);
//    }


//    @PostMapping("/newEvent")
//    public Long addEvent(@RequestBody EventDto eventDto){
//        //return eventService.addEvent(mapper.toEntity(eventDto));
//    }

//    @GetMapping("/{id}")
//    public EventDto getEventById(@PathVariable(value = "id") Long eventId){
//        Event event = eventService.getEventById(eventId);
//
//        return mapper.toDto(event);
//    }

//    @PatchMapping("/{id}")
//    public

//    @DeleteMapping("/{id}")
//    public EventDto deleteEvent(@PathVariable(value = "id") Long eventId){
//        Event event = eventService.deleteEventById(eventId);
//
//        return mapper.toDto(event);
//    }



}
