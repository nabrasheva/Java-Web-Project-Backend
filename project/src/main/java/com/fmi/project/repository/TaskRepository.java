package com.fmi.project.repository;

import com.fmi.project.model.Event;
import com.fmi.project.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByEvent(final Event event);
}
