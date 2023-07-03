package com.fmi.project.dto;

import com.fmi.project.enums.Category;
import com.fmi.project.enums.Role;
import com.fmi.project.model.Event;
import com.fmi.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUserDto {
    private String user_email;

    private Role role;

    private Category category;

    private String event_name;

}
