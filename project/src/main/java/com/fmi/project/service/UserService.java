package com.fmi.project.service;

import com.fmi.project.dto.EventDto;
import com.fmi.project.dto.UserDto;
import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.Task;
import com.fmi.project.model.User;
import com.fmi.project.repository.EventRepository;
import com.fmi.project.repository.TaskRepository;
import com.fmi.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> testUsers() {
        final List<User> test = userRepository.findAll();
        return test;
    }
}
