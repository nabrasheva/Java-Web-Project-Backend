package com.fmi.project.controller;

import com.fmi.project.dto.EventDto;
import com.fmi.project.model.Event;
import com.fmi.project.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("events")
public class EventController {
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
