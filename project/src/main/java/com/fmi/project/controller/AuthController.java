package com.fmi.project.controller;

import com.fmi.project.controller.validation.ObjectNotFoundException;
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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginCredentials loginCredentials){

        User user = userService.findUserByEmail(loginCredentials.getEmail()).
                orElseThrow(() -> new ObjectNotFoundException("User is not found!"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginCredentials.getEmail(),
                loginCredentials.getPassword()
        ));

        String jwtToken = jwtService.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
