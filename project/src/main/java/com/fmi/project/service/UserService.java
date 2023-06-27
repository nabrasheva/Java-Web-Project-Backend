package com.fmi.project.service;
import com.fmi.project.controller.validation.ApiBadRequest;
import com.fmi.project.model.User;
import com.fmi.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

import static java.util.Objects.nonNull;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findUserByUsername(String username)
    {
        return userRepository.findFirstByUsername(username);
    }

    public Optional<User> findUserByEmail(String email)
    {
        return userRepository.findFirstByEmail(email);
    }

    public void addUser(User user)
    {
        if(user == null)
            throw new ApiBadRequest("Invalid user!");

        User foundUser = userRepository.findFirstByUsername(user.getUsername()).orElse(null);
        if(foundUser != null) throw new ApiBadRequest("User already exists!");

        userRepository.save(user);
    }

    public void deleteUser(User user)
    {
        if(user == null)
            throw new ApiBadRequest("Invalid user!");

        User foundUser = userRepository.findFirstByUsername(user.getUsername()).orElse(null);
        if(foundUser == null) throw new ApiBadRequest("User does not exist!");

        userRepository.delete(user);
    }

    public void updateUserById(Long user_id, String email, String first_name, String last_name, String picture_url, Date dob, String address)
    { //can we add also the password and confirm password field, because I think that a user will change often his password
        User user = userRepository.findById(user_id).orElse(null);
        if(user == null)
            throw new ApiBadRequest("Invalid user!");

        if(nonNull(email)) user.setEmail(email);
        if(nonNull(first_name)) user.setFirst_name(first_name);
        if(nonNull(last_name)) user.setLast_name(last_name);
        if(nonNull(picture_url)) user.setProfile_picture_url(picture_url);
        if(nonNull(dob)) user.setDate_of_birth(dob);
        if(nonNull(address)) user.setAddress(address);
        userRepository.save(user);


    }

    public void updateUserPassword(User user, String password){
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
