package com.fmi.project.controller;

import com.fmi.project.dto.EmailDto;
import com.fmi.project.dto.PasswordDto;
import com.fmi.project.model.User;
import com.fmi.project.service.EmailSenderService;
import com.fmi.project.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ForgotPasswordController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @PostMapping("/sendPasswordResetEmail")
    public ResponseEntity<Object> sendPasswordResetEmail(@Valid @RequestBody EmailDto emailDto){

        Map<String, String> response = new HashMap<>();

        Optional<User> user = userService.findUserByEmail(emailDto.getEmail());

        if(user.isEmpty()){
            response.put("message", "User with this provided email is not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        String subject = "Password reset:";
        String body = "Click here, in order to reset your password: http://localhost:8079/resetPassword/" + emailDto.getEmail();

        emailSenderService.sendEmail(emailDto.getEmail(), subject, body);

        response.put("message", "Email for resetting your password is sent. Please, check your email");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/resetPassword/{email}")
    public ResponseEntity<Object> resetPassword(@PathVariable String email, @Valid @RequestBody PasswordDto passwordDto) {

        boolean isPasswordReset = userService.updateUserPassword(email, passwordDto.getPassword(), passwordDto.getConfirm_password());
        Map<String, String> response = new HashMap<>();

        if(!isPasswordReset){
            response.put("message", "The password field and confirm password field must be equal");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else{
            response.put("message", "Password updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
