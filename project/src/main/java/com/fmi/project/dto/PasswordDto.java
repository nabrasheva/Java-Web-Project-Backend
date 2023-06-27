package com.fmi.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "Invalid password format")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirm_password;
}
