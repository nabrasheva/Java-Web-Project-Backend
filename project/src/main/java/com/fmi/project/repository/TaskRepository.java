package com.fmi.project.repository;

import com.fmi.project.model.Event;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByEvent(final Event event);

    //List<Task> findByAssignees(Set<User> assignees);

    List<Task> findByCreatorEmail(final String email);

    Optional<Task> findFirstByName(final String name);
}
