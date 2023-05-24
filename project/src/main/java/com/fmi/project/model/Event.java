package com.fmi.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//@Entity
//@Table
@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private Long id;
    private String name;
    private LocalDateTime date;
    private String location;
    private String description;
    private LocalDateTime created_date;
    private LocalDateTime last_modified_date;
    private BigDecimal version;
}
