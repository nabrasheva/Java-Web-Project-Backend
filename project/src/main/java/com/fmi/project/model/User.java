package com.fmi.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@Entity
//@Table
@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String confirm_password;
    private String first_name;
    private String last_name;
    private String profile_picture_url;
    private LocalDate date_of_birth;
    private String address;
    private LocalDateTime created_date;
    private LocalDateTime last_modified_date;
    private BigDecimal version;
}
