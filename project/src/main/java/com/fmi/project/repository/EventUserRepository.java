package com.fmi.project.repository;

import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, Long> {
    EventUser findEventUserByEventAndRole(Event event, Role role);

}
