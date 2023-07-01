package com.fmi.project.mapper;


import com.fmi.project.dto.UserDto;
import com.fmi.project.model.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    public UserDto toDto(User user){
        return UserDto.builder()
                .username(user.getUserUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .build();
    }

    public User toEntity(UserDto userDto){
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .profilePictureUrl(userDto.getProfilePictureUrl())
                .dateOfBirth(userDto.getDateOfBirth())
                .address(userDto.getAddress())
                .build();
    }
}
