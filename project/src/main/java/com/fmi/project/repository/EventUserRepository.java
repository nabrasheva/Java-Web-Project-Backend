package com.fmi.project.repository;

import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, Long> {
    Optional<EventUser> findFirstByEventAndRole(final Event event, final Role role);

    Optional<EventUser> findFirstByEventAndUserAndRole(final Event event, final User user, final Role role);

    List<EventUser> findEventUserByUserAndRole(final User user, final Role role);

    List<EventUser> findEventUserByEventAndRole(final Event event, final Role role);

    Optional<EventUser> findFirstByEventAndUser(Event event, User user);

}
