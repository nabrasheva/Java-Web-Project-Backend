package com.fmi.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  @Size(min = 1, max = 32)
  private String username;
  @Email()
  @Size(max = 64)
  private String email;
  @Size(min = 6, max = 10)
  private String password;
  @Size(min = 6, max = 10)
  private String confirmPassword;
  @Size(max = 32)
  private String firstName;
  @Size(max = 32)
  private String lastName;
  private String profilePictureUrl;
  @Past
  private Date dateOfBirth;
  @Size(max = 64)
  private String address;
}
