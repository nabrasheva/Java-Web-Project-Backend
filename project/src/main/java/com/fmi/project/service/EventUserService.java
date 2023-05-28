package com.fmi.project.service;

import com.fmi.project.repository.EventUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventUserService {

    private final EventUserRepository eventUserRepository;

    

}
