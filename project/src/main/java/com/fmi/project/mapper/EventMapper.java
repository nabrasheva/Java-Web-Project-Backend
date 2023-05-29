package com.fmi.project.mapper;

import com.fmi.project.dto.EventDto;
import com.fmi.project.model.Event;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public EventDto toDto(Event event) {
        return EventDto.builder()
                .name(event.getName())
                .date(event.getDate())
                .location(event.getLocation())
                .description(event.getDescription())
                .build();
    }

    public Event toEntity(EventDto eventDto){
        return Event.builder()
                .name(eventDto.getName())
                .date(eventDto.getDate())
                .location(eventDto.getLocation())
                .description(eventDto.getDescription())
                .created_date(Timestamp.from(Instant.now()))
                .last_modified_date(Timestamp.from(Instant.now()))
                .version(1L)
                .build();
    }

    public List<EventDto> toDtoCollection(List<Event> events){
        if(events == null){
            return Collections.emptyList();
        }

        return events.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
    }
}
