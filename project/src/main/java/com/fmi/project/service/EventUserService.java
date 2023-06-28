package com.fmi.project.service;

import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.User;
import com.fmi.project.repository.EventUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventUserService {

    private final EventUserRepository eventUserRepository;


    public List<EventUser> getAllEventUsersByUserAndRole(User user, Role role)
    {
        return eventUserRepository.findEventUserByUserAndRole(user, role);
    }

    public Optional<EventUser> findFirstByEventAndRoleAdmin(Event event) //when we want to find the ADMIN
    {
        return eventUserRepository.findFirstByEventAndRole(event, Role.ADMIN);
    }

    public Optional<EventUser> findFirstByEventAndRoleForAssignee(Event event, User user)
    {
        return eventUserRepository.findFirstByEventAndUserAndRole(event, user, Role.PLANNER);
    }
    public List<EventUser> findEventUserByEventAndRole(Event event, Role role)
    {
        return eventUserRepository.findEventUserByEventAndRole(event, role);
    }

    public void addEventUser(EventUser eventUser)
    {
        eventUserRepository.save(eventUser);
    }

    public Optional<EventUser> findFirstByEventAndUser(Event event, User user) {
        return eventUserRepository.findFirstByEventAndUser(event, user);
    }
}
