package com.fmi.project.mapper;

import com.fmi.project.dto.EventDto;
import com.fmi.project.dto.EventUserDto;
import com.fmi.project.model.Event;
import com.fmi.project.model.EventUser;
import com.fmi.project.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EventUserMapper {
    public EventUserDto toDto(EventUser eventUser) {
        return EventUserDto.builder()
                .user_email(eventUser.getUser().getEmail())
                .role(eventUser.getRole())
                .category(eventUser.getCategory())
                .event_name(eventUser.getEvent().getName())
                .build();
    }

    public EventUser toEntity(EventUserDto eventUserDto, User user, Event event){
        return EventUser.builder()
                .role(eventUserDto.getRole())
                .category(eventUserDto.getCategory())
                .user(user)
                .event(event)
                .build();
    }

    public List<EventUserDto> toDtoCollection(List<EventUser> eventUsers){
        if(eventUsers == null){
            return Collections.emptyList();
        }

        return eventUsers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
