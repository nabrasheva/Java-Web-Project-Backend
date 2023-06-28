package com.fmi.project.controller;


import com.fmi.project.controller.validation.ApiBadRequest;
import com.fmi.project.dto.EventDto;
import com.fmi.project.dto.UserDto;
import com.fmi.project.mapper.UserMapper;
import com.fmi.project.model.User;
import com.fmi.project.service.EmailSenderService;
import com.fmi.project.service.JwtService;
import com.fmi.project.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailSenderService emailSenderService;

    //TODO: GetMapping for "/verifyEmail/{token}" - that page will contain a message for successful verification and a lint to the login page

    /**
     *
     * @param username
     * @return userDto object
     */
    @GetMapping("/userInfo/{username}")
    public UserDto getUser(@PathVariable(name = "username") String username) {
        User user = userService.findUserByUsername(username).orElse(null);

        if(user == null){
            throw new ApiBadRequest("There is no such user");
        }

        return userMapper.toDto(user);
    }

    /**
     *
     * @param userDto
     * @return response with successfully added user
     */
    @PostMapping("/signup")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            StringBuilder messages = new StringBuilder();

            for(FieldError error : bindingResult.getFieldErrors()){
                messages.append(error.getDefaultMessage()).append("; ");
            }

            return ResponseEntity.badRequest().body(messages.toString());
        }

        User newUser = userMapper.toEntity(userDto);
        //newUser.setEnabled(false);
        userService.addUser(newUser);

        String subject = "Email verification:";
        String body = "Click here, in order to verify your email: http://localhost:8079/verifyEmail/";

        emailSenderService.sendEmail(newUser.getEmail(), subject, body);

        return new ResponseEntity<String>("Successfully added user. Please, check you email for verification.",
                                            HttpStatus.OK);
    }

    /**
     *
     * @param username
     * @param toUpdateUserDto
     * @return updated userDto
     */
    @PatchMapping("/updateUser/{username}")
    public UserDto updateUser(@PathVariable(name = "username") String username,
                              @RequestBody UserDto toUpdateUserDto){

        User user = userService.findUserByUsername(username).orElse(null);

        if(user == null){
            throw new ApiBadRequest("There is no such user");
        }

        userService.updateUserById(user.getId(), toUpdateUserDto.getEmail(),
                                    toUpdateUserDto.getFirst_name(), toUpdateUserDto.getLast_name(),
                                    toUpdateUserDto.getProfile_picture_url(), toUpdateUserDto.getDate_of_birth(),
                                    toUpdateUserDto.getAddress());

        return userMapper.toDto(user);
    }

    /**
     *
     * @param username
     * @return response with message for successfully deleted user
     */
    @DeleteMapping("/userInfo/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "username") String username){
        User user = userService.findUserByUsername(username).orElse(null);

        if(user == null){
            throw new ApiBadRequest("The is no such user");
        }

        userService.deleteUser(user);

        return new ResponseEntity<String>("Successfully deleted user", HttpStatus.OK);
    }

}
