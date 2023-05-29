package com.fmi.project.service;

import com.fmi.project.controller.validation.ApiBadRequest;
import com.fmi.project.enums.Category;
import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventUserService eventUserService;

    private final UserService userService;


    public List<Event> getEventsByRoleAndUser(Role role, User user) {
       final List<EventUser> list = eventUserService.getAllEventUsersByUserAndRole(user, role);

        final List<Event> eventList = new ArrayList<>();
        list.forEach(eventUser -> eventList.add(eventUser.getEvent()));

        return eventList;
    }

    public void addEvent(Event event, String username) {
        if (event == null) {
            throw new IllegalArgumentException("Incorrect data");
        }
        List<Event> eventList = eventRepository.findAll();
        User newEventCreator = userService.findUserByUsername(username).orElse(null);

        if(newEventCreator == null) throw new ApiBadRequest("User do not exist!");
        eventList.forEach(event1 -> {

           EventUser eventUserForCreator = eventUserService.findFirstByEventAndRole(event1, Role.ADMIN).orElse(null);
           if(eventUserForCreator == null) throw new ApiBadRequest("EventUser row does not exist!"); //ToDo: make better exception
           User eventCreator = eventUserForCreator.getUser();

            if (eventCreator.getId().equals(newEventCreator.getId()) && event1.equals(event)) { // TODO: make equals
                throw new ApiBadRequest("Event already exists!");
            }
        });

        eventRepository.save(event);

        EventUser newEventUser = EventUser.builder()
                                            .event(event)
                                            .user(newEventCreator)
                                            .role(Role.ADMIN)
                                            .category(Category.NONE)
                                            .build();
        eventUserService.addEventUser(newEventUser);
    }

    public void deleteEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Incorrect data");
        }
        try {
            eventRepository.delete(event);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void updateEventById(Long id, String description, String location, Date date)
    {
        Event event = eventRepository.findById(id).orElse(null);

        if (event != null)
        {
            if(description != null) event.setDescription(description);
            if(location != null) event.setLocation(location);
            if(date != null) event.setDate(date);
            eventRepository.save(event);
        }
        else throw new ApiBadRequest("Invalid event!");
    }

    public Optional<Event> getEventById(Long id)
    {
        return eventRepository.findById(id);
    }

    public List<Task> getAllTasksByEvent(Event event)
    {
        Event event1 = eventRepository.findById(event.getId()).orElse(null);

        if(event1 == null) throw new ApiBadRequest("Invalid event!");

        return event1.getTasks().stream().toList();
    }

    public List<Task> getAllTasksByEventId(Long event_id)
    {
        Event event = eventRepository.findById(event_id).orElse(null);

        if(event == null) throw new ApiBadRequest("Invalid event!");

        return event.getTasks().stream().toList();
    }
}
