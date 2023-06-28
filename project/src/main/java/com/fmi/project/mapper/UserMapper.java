package com.fmi.project.mapper;


import com.fmi.project.dto.UserDto;
import com.fmi.project.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user){
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .build();
    }

    public User toEntity(UserDto userDto){
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .firstName(userDto.getFirstName())
                .profilePictureUrl(userDto.getProfilePictureUrl())
                .dateOfBirth(userDto.getDateOfBirth())
                .address(userDto.getAddress())
                .build();
    }
}
