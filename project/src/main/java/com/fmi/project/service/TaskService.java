package com.fmi.project.service;

import com.fmi.project.controller.validation.ApiBadRequest;
import com.fmi.project.enums.Status;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
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

    public void addTask(Event event, Task task) { //maybe to check if there is such task and
                                                // add the username of the person, who will be the creator of the task??
                                                //maybe not to add event_id in the taskDto,because we can get it from the pathVariable
                                                //just to make it similar to addEvent method in EventService
        if (event == null || eventService.getEventById(event.getId()).orElse(null) == null) {
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
            event.getTasks().remove(task);
            taskRepository.delete(task);
        }
        else throw new ApiBadRequest("Invalid event");

    }

    public void updateTaskById(Long id, String name, String description, Date due_date, Status status)
    {
        Task task = taskRepository.findById(id).orElse(null);
        if(nonNull(task))
        {
            if(nonNull(name)) task.setName(name);
            if(nonNull(description)) task.setDescription(description);
            if(nonNull(due_date)) task.setDue_date(due_date);
            if(nonNull(status)) task.setStatus(status);
            taskRepository.save(task);
        }
        else throw new ApiBadRequest("Invalid task!");
    }
    //......................................................
    public boolean checkAssigneeByTaskId(Long task_id, User user){
        Task task = taskRepository.findById(task_id).orElse(null);

        if(task == null)
        {
            throw new ApiBadRequest("Invalid event");
        }

        return task.getAssignees().stream()
                    .anyMatch(assignee -> assignee.getUsername().equals(user.getUsername()));
    }

    public List<String> getAssigneesByTaskId(Long task_id){
        Task task = taskRepository.findById(task_id).orElse(null);

        if(task == null)
        {
            throw new ApiBadRequest("Invalid event");
        }

        return task.getAssignees().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

//    public List<Task> getTasksByAssignee(User user) {
//        Set<User> assignees =
//        return taskRepository.findByAssignees(user);
//    }

    public List<Task> getTasksByAdmin(String username) {
        return taskRepository.findByCreatorUsername(username);
    }
    //........................................................
    public void addAssigneeForTask(Long task_id, String username)
    {
        Task task = taskRepository.findById(task_id).orElse(null);
        User user = userService.findUserByUsername(username).orElse(null);
        if(nonNull(task) && nonNull(user))
        {
            EventUser eventUser = eventUserService.findFirstByEventAndRoleForAssignee(task.getEvent(), user).orElse(null);
            if(nonNull(eventUser))
            {
                if(task.getAssignees().contains(user)){
                    throw new ApiBadRequest("User is already assigned to task");
                }

                task.getAssignees().add(user);
                taskRepository.save(task);
            }
            else throw new ApiBadRequest("User is not part of the event or is not a planner!");
        }
        else throw new ApiBadRequest("Invalid task or invalid user!");
    }

    //TODO: delete assignee for task
}
