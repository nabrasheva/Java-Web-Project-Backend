package com.fmi.project.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentials {
    @NotBlank(message = "The username is required!")
    private String username;
    @NotBlank(message = "The password is required!")
    private String password;
}
