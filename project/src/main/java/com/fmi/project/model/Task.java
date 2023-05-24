package com.fmi.project.model;

import com.fmi.project.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

//@Entity
//@Table
@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    private String name;
    private String description;
    private LocalDate due_date;
    private Status status;
    private String creator_username;
    private Long event_id;
    private Set<EventUser> assignees;
    private Event event;
    private LocalDateTime created_date;
    private LocalDateTime last_modified_date;
    private BigDecimal version;
}
