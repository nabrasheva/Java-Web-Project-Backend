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

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
public class ForgotPasswordController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @PostMapping("/sendPasswordResetEmail")
    public ResponseEntity<String> sendPasswordResetEmail(@Valid @RequestBody EmailDto emailDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            String message = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();

            return ResponseEntity.badRequest().body(message);
        }

        Optional<User> user = userService.findUserByEmail(emailDto.getEmail());
        if(user.isEmpty()){
            return new ResponseEntity<String>("User with this provided email is not found", HttpStatus.NOT_FOUND);
        }

        String subject = "Password reset:";
        String body = "Click here, in order to reset your password: http:localhost:8079//resetPassword/";

        emailSenderService.sendEmail(emailDto.getEmail(), subject, body);

        return new ResponseEntity<String>("Email for resetting your password is sent. Please, check your email",
                                            HttpStatus.OK);
    }

    @PatchMapping("/resetPassword/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @Valid @RequestBody PasswordDto passwordDto,
                                                BindingResult bindingResult) {

        //Optional<User> user = userService.findUserByResetToken(token);
        //if(user.isEmpty()){
        //  return new ResponseEntity<String>("There is no user with this token", HttpStatus.UNAUTHORIZED);
        //}
        //else{
        //  if(user.getResetPasswordExpires() < LocalDateTime.now()){
        //      return new ResponseEntity<String>("The token has expired", HttpStatus.UNAUTHORIZED);
        //  }
        //}

        if(bindingResult.hasErrors()){
            StringBuilder messages = new StringBuilder();

            for(FieldError error : bindingResult.getFieldErrors()){
                messages.append(error.getDefaultMessage()).append("; ");
            }

            return ResponseEntity.badRequest().body(messages.toString());
        }

        if(!passwordDto.getPassword().equals(passwordDto.getConfirm_password())){
            return new ResponseEntity<String>("The password field and confirm password field must be equal", HttpStatus.BAD_REQUEST);
        }

        //User thisUser = user.get();
        //userService.updateUserPassword(thisUser, passwordDto.getPassword());

        return new ResponseEntity<String>("Password updated successfully", HttpStatus.OK);
        //then, redirect to the login page
    }
}
