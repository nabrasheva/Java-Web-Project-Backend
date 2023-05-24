package com.fmi.project.model;

import com.fmi.project.enums.Category;
import com.fmi.project.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

//@Entity
//@Table
@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventUser {
    private Long id;
    private Role role;
    private Category category;
    private User user;
    private Event event;
}
