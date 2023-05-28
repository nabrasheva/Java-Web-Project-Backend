package com.fmi.project.service;

import com.fmi.project.dto.EventDto;
import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.Task;
import com.fmi.project.repository.EventRepository;
import com.fmi.project.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<Event> testEvents() {
        final List<Event> test = eventRepository.findAll();
        return test;
    }
}
