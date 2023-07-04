package com.fmi.project.controller;

import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.dto.UpdateUserDto;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailSenderService emailSenderService;
    private final JwtService jwtService;

    //TODO: GetMapping for "/verifyEmail/{token}" - that page will contain a message for successful verification and a link to the login page

    /**
     *
     * @param email  email of the user
     * @return userDto object
     */
    @GetMapping("/userInfo/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email) {
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User does not exist!"));

        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK);
    }

    /**
     *
     * @param userDto  object, that contains information about the user's request
     * @return response with successfully added user
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> addUser(@RequestBody UserDto userDto){
        //check if userDto.getPassword() == userDto.getConfirmPassword()
        //check if password is valid based on regex
        //make endpoint http://localhost:8079/verifyEmail/{token}, in which the user will be added!!!

        User newUser = userMapper.toEntity(userDto);
        //newUser.setEnabled(false);
        userService.addUser(newUser);

        String jwtToken = jwtService.generateToken(newUser);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully added user. In order to use your account, please verify your email");
        response.put("token", jwtToken);

        String subject = "Email verification:";
        String body = "Click here, in order to verify your email: http://localhost:4200/users/verifyEmail/" + newUser.getEmail();

        emailSenderService.sendEmail(newUser.getEmail(), subject, body);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PatchMapping("/verifyEmail/{email}")
    public ResponseEntity<Object> verifyEmail(@PathVariable String email){
        boolean isVerified = userService.verifyEmail(email);
        Map<String, String> response = new HashMap<>();

        if(isVerified){
            response.put("message", "Successfully verified user");
        } else {
            response.put("message", "The user is already verified");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /**
     *
     * @param email - email of the user
     * @param toUpdateUserDto object, that contains information about the user's request
     * @return updated userDto object, that contains information about the updated user's data (response)
     */
    @PatchMapping("/updateUser/{email}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String email,
                              @RequestBody UpdateUserDto toUpdateUserDto){

        User user = userService.updateUserByEmail(email,
                                toUpdateUserDto.getFirstName(), toUpdateUserDto.getLastName(),
                                toUpdateUserDto.getProfilePictureUrl(), toUpdateUserDto.getDateOfBirth(),
                                toUpdateUserDto.getAddress());

        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK);
    }

    /**
     *
     * @param email email of the user
     * @return response with message for successfully deleted user
     */
    @DeleteMapping("/userInfo/{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email){
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ObjectNotFoundException("The is no such user"));
        userService.deleteUser(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully deleted user");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
