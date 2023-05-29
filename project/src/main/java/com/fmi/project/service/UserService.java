package com.fmi.project.service;
import com.fmi.project.controller.validation.ApiBadRequest;
import com.fmi.project.model.User;
import com.fmi.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;


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
    {
        User user = userRepository.findById(user_id).orElse(null);
        if(user == null)
            throw new ApiBadRequest("Invalid user!");

        if(email!=null) user.setEmail(email);
        if(first_name!=null) user.setFirst_name(first_name);
        if(last_name!=null) user.setLast_name(last_name);
        if(picture_url != null) user.setProfile_picture_url(picture_url);
        if(dob !=null) user.setDate_of_birth(dob);
        if(address!=null) user.setAddress(address);
        userRepository.save(user);


    }
}
