package com.fmi.project.controller;

import com.fmi.project.dto.TaskDto;
import com.fmi.project.enums.Role;
import com.fmi.project.model.Task;
import com.fmi.project.service.EventService;
import com.fmi.project.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("events")
@AllArgsConstructor
public class EventController {
    private final TaskService taskService;
    private final EventService eventService;

    @GetMapping("/tasks")
    public List<String> getTasks() {
        return taskService.test().stream().map(item -> item.getId() + " " + item.getName() + "\n")
                .collect(Collectors.toList());
    }

    @PostMapping("/getEvents")
    public List<String> getEvents(@RequestBody Role test) {
        System.out.println(test);
        return eventService.testEvents().stream().map(item -> item.getId() + " " + item.getName() + "\n")
                .collect(Collectors.toList());
    }
}
