package com.fmi.project.service;

import com.fmi.project.controller.validation.ObjectNotFoundException;
import com.fmi.project.model.User;
import com.fmi.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findUserByUsername() {
    }

    @Test
    void findUserByEmail() {
    }

    @Test
    void addUser_whenPassedNullInput() {
        //Given
        //When
        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
          userService.addUser(null);
        });

        String expectedMessage = "Invalid user!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        //Then

    }


    @Test
    void deleteUser() {
    }

    @Test
    void updateUserByEmail() {
    }

    @Test
    void updateUserPassword() {
    }
}