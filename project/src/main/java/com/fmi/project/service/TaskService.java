package com.fmi.project.service;

import com.fmi.project.model.Task;
import com.fmi.project.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

//    public List<Task> test() {
//        final List<Task> test = taskRepository.findAll();
//        return test;
//    }

}
