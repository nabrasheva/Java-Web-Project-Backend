package com.fmi.project.controller;


import com.fmi.project.controller.validation.ApiBadRequest;
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

@Slf4j
@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     *
     * @param username
     * @return userDto object
     */
    @GetMapping("/userInfo/{username}")
    public UserDto getUser(@PathVariable(name = "username") String username) {
        User user = userService.findUserByUsername(username).orElse(null);

        if(user == null){
            throw new ObjectNotFoundException("There is no such user");
        }

        return userMapper.toDto(user);
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
     * @param username
     * @param toUpdateUserDto
     * @return updated userDto
     */
    @PatchMapping("/updateUser/{username}")
    public UserDto updateUser(@PathVariable(name = "username") String username,
                              @RequestBody UserDto toUpdateUserDto){

        User user = userService.findUserByUsername(username).orElse(null);

        if(user == null){
            throw new ObjectNotFoundException("There is no such user");
        }

        userService.updateUserById(user.getId(), toUpdateUserDto.getEmail(),
                                    toUpdateUserDto.getFirstName(), toUpdateUserDto.getLastName(),
                                    toUpdateUserDto.getProfilePictureUrl(), toUpdateUserDto.getDateOfBirth(),
                                    toUpdateUserDto.getAddress());

        return userMapper.toDto(user);
    }

    /**
     *
     * @param username
     * @return response with message for successfully deleted user
     */
    @DeleteMapping("/userInfo/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "username") String username){
        User user = userService.findUserByUsername(username).orElse(null);

        if(user == null){
            throw new ObjectNotFoundException("The is no such user");
        }

        userService.deleteUser(user);

        return new ResponseEntity<>("Successfully deleted user", HttpStatus.OK);
    }

}
