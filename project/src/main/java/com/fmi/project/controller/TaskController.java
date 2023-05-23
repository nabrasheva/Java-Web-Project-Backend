package com.fmi.project.controller;

import com.fmi.project.dto.TaskDto;
import com.fmi.project.model.Task;
import com.fmi.project.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("events/eventId/tasks") // eventId is different for each event
public class TaskController {
    private final TaskService taskService;
    //private final

    @Autowired
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

//    @GetMapping
//    public List<Task> getTasks(){
//        final List<Task> tasks = taskService.getTasks();
//
//        return mapper.toDtoCollection(tasks);
//    }

//    @PostMapping("/newTask")
//    public Long addTask(@RequestBody TaskDto taskDto){
//        return taskService.addTask(mapper.toEntity(taskDto));
//    }

//    @GetMapping("/{id}")
//    public TaskDto getTaskById(@PathVariable(value = "id") Long taskId){
//        Task task = taskService.getTaskById(taskId);
//
//        return mapper.toDto(task);
//    }

//    @PatchMapping("/{id}")
//    public

//    @DeleteMapping("/{id}")
//    public TaskDto deleteTask(@PathVariable(value = "id") Long taskId){
//        Task task = eventService.deleteTaskById(taskId);
//
//        return mapper.toDto(task);
//    }


}
