package com.fmi.project.service;

import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.enums.Category;
import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

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

    public void addEvent(Event event, String email) {
        if (event == null) {
            throw new IllegalArgumentException("Incorrect data");
        }

        List<Event> eventList = eventRepository.findAll();
        User newEventCreator = userService.findUserByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User do not exist!"));

        eventList.forEach(event1 -> {

           EventUser eventUserForCreator = eventUserService.findFirstByEventAndRoleAdmin(event1).orElseThrow(() ->
                   new ObjectNotFoundException("EventUser row does not exist!"));

           User eventCreator = eventUserForCreator.getUser();

            if (eventCreator.getId().equals(newEventCreator.getId()) && event.getName().equals(event1.getName())) {
                throw new ObjectNotFoundException("Event already exists!");
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

    public Event updateEventById(String name, String description, String location, LocalDate date)
    {
        Event event = eventRepository.findFirstByName(name).orElseThrow(() -> new ObjectNotFoundException("There is no such event"));

        if(nonNull(description)) event.setDescription(description);

        if(nonNull(location)) event.setLocation(location);

        if(nonNull(date)) event.setDate(date);

        eventRepository.save(event);

        return event;
    }

    public Optional<Event> getEventByName(String name)
    {
        return eventRepository.findFirstByName(name);
    }

    public Task getTaskByEventNameAndTaskName(String eventName, String taskName){
        Event event1 = eventRepository.findFirstByName(eventName).orElseThrow(() -> new ObjectNotFoundException("Invalid event!"));

        Task task1 = event1.getTasks().stream()
                                        .filter(task -> task.getName().equals(taskName))
                                        .findFirst()
                                        .orElseThrow(() -> new ObjectNotFoundException("Invalid task for this event"));

        return task1;
    }

    public List<Task> getAllTasksByEvent(String name)
    {
        Event event1 = eventRepository.findFirstByName(name).orElseThrow(() -> new ObjectNotFoundException("Invalid event!"));

        return event1.getTasks().stream().toList();
    }

    public void addUserToEvent(String eventName, String email, Role role, Category category)
    {
        Event event = eventRepository.findFirstByName(eventName).orElse(null);
        User user = userService.findUserByEmail(email).orElse(null);
        if(nonNull(event) && nonNull(user))
        {
            EventUser eventUser = eventUserService.findFirstByEventAndUser(event, user).orElse(null);
            if(nonNull(eventUser)) throw new ObjectNotFoundException("User is already added to event!");
            EventUser createdEventUser;
            if(role == Role.GUEST)
            {
                createdEventUser = EventUser.builder()
                                                    .event(event)
                                                    .role(Role.GUEST)
                                                    .category(category)
                                                    .user(user)
                                                    .build();
            }
            else {
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

    //TODO: remove user from event!

}
