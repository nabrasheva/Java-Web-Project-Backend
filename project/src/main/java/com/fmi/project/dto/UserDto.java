package com.fmi.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "Username is required!")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "Invalid password format")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirm_password;

    @NotBlank(message = "First name is required")
    private String first_name;

    @NotBlank(message = "Last name is required")
    private String last_name;

    private String profile_picture_url;
    private Date date_of_birth;
    private String address;
}
