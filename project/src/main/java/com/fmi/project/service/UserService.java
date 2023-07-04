package com.fmi.project.service;
import com.fmi.project.controller.validation.ObjectNotFoundException;
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

    public Optional<User> findUserByUsername(final String username)
    {
        return userRepository.findFirstByUsername(username);
    }

    public Optional<User> findUserByEmail(final String email){
        return userRepository.findFirstByEmail(email);
    }

    public void addUser(final User user)
    {
        if(user == null)
            throw new ObjectNotFoundException("Invalid user!");

        User foundUser = userRepository.findFirstByEmail(user.getEmail()).orElse(null);
        if(foundUser != null) throw new ObjectNotFoundException("User already exists!");

        user.set_enabled(false);
        userRepository.save(user);
    }

    public void deleteUser(final User user)
    {
        if(user == null)
            throw new ObjectNotFoundException("Invalid user!");

        userRepository.findFirstByEmail(user.getEmail()).orElseThrow(() -> new ObjectNotFoundException("User does not exist!"));

        userRepository.delete(user);
    }

    public User updateUserByEmail(String email, String first_name, String last_name, String picture_url, Date dob, String address) {
        User user = userRepository.findFirstByEmail(email).orElseThrow(() -> new ObjectNotFoundException("Invalid user!"));

        if(nonNull(first_name)) user.setFirstName(first_name);
        if(nonNull(last_name)) user.setLastName(last_name);
        if(nonNull(picture_url)) user.setProfilePictureUrl(picture_url);
        if(nonNull(dob)) user.setDateOfBirth(dob);
        if(nonNull(address)) user.setAddress(address);

        userRepository.save(user);

        return user;

    }

    public boolean verifyEmail(String email){
        User user = userRepository.findFirstByEmail(email).orElseThrow(() -> new ObjectNotFoundException("Invalid user!"));

        if(!user.isEnabled()){
            user.set_enabled(true);
            userRepository.save(user);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean updateUserPassword(String email, String password, String confirmPassword){
        User user = userRepository.findFirstByEmail(email).orElseThrow(() -> new ObjectNotFoundException("Invalid user!"));

        if(password.equals(confirmPassword)){
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        } else{
            return false;
        }
    }
}
