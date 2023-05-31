package com.fmi.project.dto;

import com.fmi.project.enums.Category;
import com.fmi.project.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserToEventDto {
    private String username;
    private Role role;
    private Category category;
}
