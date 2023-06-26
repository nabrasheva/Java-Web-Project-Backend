package com.fmi.project.dto;

import com.fmi.project.enums.Category;
import com.fmi.project.enums.Role;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserToEventDto {
    @Size(min = 1, max = 32)
    private String username;
    private Role role;
    private Category category;
}
