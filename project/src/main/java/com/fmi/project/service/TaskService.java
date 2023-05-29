package com.fmi.project.service;

import com.fmi.project.controller.validation.ApiBadRequest;
import com.fmi.project.enums.Status;
import com.fmi.project.model.Event;
import com.fmi.project.model.Task;
import com.fmi.project.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;


@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final EventService eventService;

    /*

     */

    public void addTask(Event event, Task task)
    {
        if(event == null || eventService.getEventById(event.getId()).orElse(null) == null )
        {
            throw new ApiBadRequest("Invalid event");
        }

        event.getTasks().add(task);
        taskRepository.save(task);
    }

    public void removeTask(Event event, Long task_id)
    {
        Task task = taskRepository.findById(task_id).orElse(null);

        if(task == null)
        {
            throw new ApiBadRequest("Invalid event");
        }

        if(task.getEvent().getId().equals(event.getId()))
        {
            event.getTasks().remove(task); // TODO: !!!!
            taskRepository.delete(task);
        }
        else throw new ApiBadRequest("Invalid event");

    }

    public void updateTaskById(Long id, String name, String description, Date due_date, Status status)
    {
        Task task = taskRepository.findById(id).orElse(null);
        if(task != null)
        {
            if(name != null) task.setName(name);
            if(description != null) task.setDescription(description);
            if(due_date != null) task.setDue_date(due_date);
            if(status != null) task.setStatus(status);
            taskRepository.save(task);
        }
    }
}
