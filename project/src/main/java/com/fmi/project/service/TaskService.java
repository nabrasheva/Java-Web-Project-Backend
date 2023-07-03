package com.fmi.project.service;

import com.fmi.project.controller.validation.ObjectFoundException;
import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.enums.Status;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.repository.TaskRepository;
import com.fmi.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final EventService eventService;
    private final EventUserService eventUserService;
    private final UserService userService;

    public List<Task> getAllTasksByEventId(Event event) {
        return taskRepository.findAllByEvent(event);
    }

    public Optional<Task> findByTaskId(long taskId) {
        return taskRepository.findById(taskId);
    }

    public void addTask(Event event, Task task) {
        Task newTask = taskRepository.findFirstByName(task.getName()).orElse(null);

        if (event == null || eventService.getEventByName(event.getName()).orElse(null) == null) {
            throw new ObjectNotFoundException("Invalid event");
        }

        if (newTask == null) {
            event.getTasks().add(task);
            taskRepository.save(task);
        } else throw new ObjectFoundException("There is already such task!");
    }

    public void removeTask(Event event, String taskName) {
        Task task = taskRepository.findFirstByName(taskName).orElseThrow(() -> new ObjectNotFoundException("Invalid event"));

        if (task.getEvent().getId().equals(event.getId())) {
            event.getTasks().remove(task);
            taskRepository.delete(task);
        } else throw new ObjectNotFoundException("Invalid event");

    }

    public Task updateTaskByName(String name, String description, Date due_date, Status status, List<String> assignees) {
        Task task = taskRepository.findFirstByName(name).orElseThrow(() -> new ObjectNotFoundException("Invalid task!"));

        if (nonNull(description)) task.setDescription(description);

        if (nonNull(due_date)) task.setDue_date(due_date);

        if (nonNull(status)) task.setStatus(status);

        if (assignees != null && !assignees.isEmpty()){
            Set<User> userSet = assignees.stream().map(email -> {
                User user = userService.findUserByEmail(email).orElse(null);
                EventUser eventUser = eventUserService.findFirstByEventAndRoleForAssignee(task.getEvent(), user).orElse(null);
                if(nonNull(eventUser)) return user;
                else throw new ObjectNotFoundException("User is not part of the event or is not a planner!");
            }).collect(Collectors.toSet());
            task.setAssignees(userSet);
        }

        taskRepository.save(task);

        return task;
    }

    //......................................................
    public boolean checkAssigneeByTaskId(Long task_id, User user) {
        Task task = taskRepository.findById(task_id).orElse(null);

        if (task == null) {
            throw new ObjectNotFoundException("Invalid event");
        }

        return task.getAssignees().stream()
                .anyMatch(assignee -> assignee.getUsername().equals(user.getUsername()));
    }

    public List<String> getAssigneesByTaskName(String taskName) {
        Task task = taskRepository.findFirstByName(taskName).orElseThrow(() -> new ObjectNotFoundException("Invalid event"));

        return task.getAssignees().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByAdmin(String email) {
        return taskRepository.findByCreatorEmail(email);
    }

    //........................................................
    public void addAssigneeForTask(String taskName, String email) {
        Task task = taskRepository.findFirstByName(taskName).orElse(null);
        User user = userService.findUserByEmail(email).orElse(null);

        if (nonNull(task) && nonNull(user)) {
            EventUser eventUser = eventUserService.findFirstByEventAndRoleForAssignee(task.getEvent(), user).orElse(null);

            if (nonNull(eventUser)) {

                if (task.getAssignees().contains(user)) {
                    throw new ObjectFoundException("User is already assigned to task");
                }

                task.addUser(user);
                taskRepository.save(task);

            } else throw new ObjectNotFoundException("User is not part of the event or is not a planner!");

        } else throw new ObjectNotFoundException("Invalid task or invalid user!");
    }

    //TODO: delete assignee for task
}
