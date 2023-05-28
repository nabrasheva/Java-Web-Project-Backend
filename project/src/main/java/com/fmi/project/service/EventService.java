package com.fmi.project.service;

import com.fmi.project.controller.validation.ApiBadRequest;
import com.fmi.project.enums.Category;
import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.User;
import com.fmi.project.repository.EventRepository;
import com.fmi.project.repository.EventUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventUserRepository eventUserRepository;

    private final UserService userService;

//    public List<Event> testEvents() {
//        final List<Event> test = eventRepository.findAll();
//        return test;
//    }

    public List<Event> getEventsByRole(Role role) {
        final List<EventUser> list = eventUserRepository.findAll().stream().filter(eventUser -> eventUser.getRole() == role).toList();

        final List<Event> eventList = new ArrayList<>();
        list.forEach(eventUser -> eventList.add(eventUser.getEvent()));

        return eventList;
    }

    public void addEvent(Event event, String username) {
        if (event == null) {
            throw new IllegalArgumentException("Incorrect data");
        }
        List<Event> eventList = eventRepository.findAll();
        User newEventCreator = userService.findUserByUsername(username);

        eventList.forEach(event1 -> {
            User eventCreator = eventUserRepository.findEventUserByEventAndRole(event1, Role.ADMIN).getUser();

            if (eventCreator.equals(newEventCreator) && event1.equals(event)) {
                throw new ApiBadRequest("Event already exists!");
            }
        });

        eventRepository.save(event);
        EventUser newEventUser = new EventUser();

        newEventUser.setEvent(event);
        newEventUser.setUser(newEventCreator);
        newEventUser.setRole(Role.ADMIN);
        newEventUser.setCategory(Category.NONE);
        newEventUser.setTasks(new HashSet<>());

        eventUserRepository.save(newEventUser);
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

        // TODO: CASCADE REMOVE FOR EVENT_USER!!!

//        List<EventUser> eventUsers = eventUserRepository.findAll()
//                .stream()
//                .filter(eventUser -> eventUser.getEvent().equals(event))
//                .toList();

    }

    public void updateEvent(Long id, String description, String location, Date date)
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


}
