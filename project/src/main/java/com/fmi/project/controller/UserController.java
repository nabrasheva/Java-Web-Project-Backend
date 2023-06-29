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

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     *
     * @param email
     * @return userDto object
     */
    @GetMapping("/userInfo/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email) {
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User does not exist!"));

        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK);
    }

    /**
     *
     * @param userDto
     * @return response with successfully added user
     */
    @PostMapping("/signup")
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto){
        User newUser = userMapper.toEntity(userDto);
        userService.addUser(newUser);

        return new ResponseEntity<>("Successfully added user", HttpStatus.OK);
    }

    /**
     *
     * @param email
     * @param toUpdateUserDto
     * @return updated userDto
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
     * @param email
     * @return response with message for successfully deleted user
     */
    @DeleteMapping("/userInfo/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email){
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ObjectNotFoundException("The is no such user"));
        userService.deleteUser(user);

        return new ResponseEntity<>("Successfully deleted user", HttpStatus.OK);
    }

}
