package com.fmi.project.service;

import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.User;
import com.fmi.project.repository.EventUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventUserService {

    private final EventUserRepository eventUserRepository;


    public List<EventUser> getAllEventUsersByUserAndRole(User user, Role role)
    {
        return eventUserRepository.findEventUserByUserAndRole(user, role);
    }

    public EventUser findEventUserByEventAndRole(Event event, Role role)
    {
        return eventUserRepository.findEventUserByEventAndRole(event, role);
    }

    public void addEventUser(EventUser eventUser)
    {

        eventUserRepository.save(eventUser);
    }
}
