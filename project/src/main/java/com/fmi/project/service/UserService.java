package com.fmi.project.service;
import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.model.User;
import com.fmi.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

import static java.util.Objects.nonNull;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findUserByUsername(String username)
    {
        return userRepository.findFirstByUsername(username);
    }

    public void addUser(User user)
    {
        if(user == null)
            throw new ObjectNotFoundException("Invalid user!");

        User foundUser = userRepository.findFirstByUsername(user.getUsername()).orElse(null);
        if(foundUser != null) throw new ObjectNotFoundException("User already exists!");

        userRepository.save(user);
    }

    public void deleteUser(User user)
    {
        if(user == null)
            throw new ObjectNotFoundException("Invalid user!");

        User foundUser = userRepository.findFirstByUsername(user.getUsername()).orElse(null);
        if(foundUser == null) throw new ObjectNotFoundException("User does not exist!");

        userRepository.delete(user);
    }

    public void updateUserById(Long user_id, String email, String first_name, String last_name, String picture_url, Date dob, String address)
    { //can we add also the password and confirm password field, because I think that a user will change often his password
        User user = userRepository.findById(user_id).orElse(null);
        if(user == null)
            throw new ObjectNotFoundException("Invalid user!");

        if(nonNull(email)) user.setEmail(email);
        if(nonNull(first_name)) user.setFirstName(first_name);
        if(nonNull(last_name)) user.setLastName(last_name);
        if(nonNull(picture_url)) user.setProfilePictureUrl(picture_url);
        if(nonNull(dob)) user.setDateOfBirth(dob);
        if(nonNull(address)) user.setAddress(address);
        userRepository.save(user);


    }
}
