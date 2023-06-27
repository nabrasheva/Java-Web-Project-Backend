package com.fmi.project.controller;

import com.fmi.project.dto.LoginCredentials;
import com.fmi.project.model.User;
import com.fmi.project.service.JwtService;
import com.fmi.project.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@Valid @RequestBody LoginCredentials loginCredentials, BindingResult bindingResult){
//        if(bindingResult.hasErrors()) {
//            StringBuilder messages = new StringBuilder();
//
//            for (FieldError error : bindingResult.getFieldErrors()) {
//                messages.append(error.getDefaultMessage()).append("; ");
//            }
//
//            return ResponseEntity.badRequest().body(messages.toString());
//        }
//
//        Optional<User> loggedUser = userService.findUserByUsername(loginCredentials.getUsername());
//        //TODO: to encrypt the password and to check if this password is equal to the encrypted password, which is in the db
//        if(loggedUser.isEmpty() || !loggedUser.get().getPassword().equals(loginCredentials.getPassword())){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//
//        //authenticateManager.authenticate(...)
//
//        String token = generateToken(loginCredentials.getUsername());
//
//        return new ResponseEntity<String>("Token: " + token, HttpStatus.OK);
//    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginCredentials loginCredentials, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            StringBuilder messages = new StringBuilder();

            for (FieldError error : bindingResult.getFieldErrors()) {
                messages.append(error.getDefaultMessage()).append("; ");
            }

            return ResponseEntity.badRequest().body(messages.toString());
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginCredentials.getUsername(),
                loginCredentials.getPassword()
        ));

        User user = userService.findUserByUsername(loginCredentials.getUsername()).
                    orElseThrow(() -> new UsernameNotFoundException("There is no such user"));

        String token = jwtService.generateToken(user);

        return new ResponseEntity<String>("Successfully logged. Token: " + token, HttpStatus.OK);
    }

}
