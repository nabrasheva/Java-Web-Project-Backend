package com.fmi.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String email;
    private String password;
    private String confirm_password;
    private String first_name;
    private String last_name;
    private String profile_picture_url;
    private LocalDate date_of_birth;
    private String address;
}
