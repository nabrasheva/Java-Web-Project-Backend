package com.fmi.project.mapper;


import com.fmi.project.dto.UserDto;
import com.fmi.project.model.User;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class UserMapper {
    public UserDto toDto(User user){
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .first_name(user.getFirst_name())
                .profile_picture_url(user.getProfile_picture_url())
                .date_of_birth(user.getDate_of_birth())
                .address(user.getAddress())
                .build();
    }

    public User toEntity(UserDto userDto){
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .first_name(userDto.getFirst_name())
                .profile_picture_url(userDto.getProfile_picture_url())
                .date_of_birth(userDto.getDate_of_birth())
                .address(userDto.getAddress())
                .build();
    }
}
