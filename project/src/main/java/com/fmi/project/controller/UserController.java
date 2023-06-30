package com.fmi.project.controller;

import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.dto.UserDto;
import com.fmi.project.mapper.UserMapper;
import com.fmi.project.model.User;
import com.fmi.project.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") //TODO: ADD CORS POLITICS
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

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
        User newUser = userMapper.toEntity(userDto);
        userService.addUser(newUser);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully added user");

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
                              @RequestBody UserDto toUpdateUserDto){

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
