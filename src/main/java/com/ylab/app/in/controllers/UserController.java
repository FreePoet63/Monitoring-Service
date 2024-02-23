package com.ylab.app.in.controllers;

import com.ylab.app.model.User;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.UserService;
import com.ylab.app.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * The UserController class handles HTTP requests related to user interactions.
 * It provides methods for user registration, retrieving a user by ID, and retrieving a list of all users.
 *
 * @RestController indicates that the data returned by each method will be straight into the response body instead of rendering a template.
 * @Author razlivinsky
 * @since 14.02.2024
 */
@RestController
public class UserController {
    private final UserServiceImpl userService;

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     */
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param user the user to register
     * @return a response entity containing the registered user information
     * @throws SQLException if an error occurs while accessing the database
     */
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "method provide registers a new user", tags = {"users"})
    public ResponseEntity<UserDto> registerUser(@RequestBody User user) throws SQLException {
        UserDto userDto = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }


    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a response entity containing the user information
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "Find a User by id ", description = "method provide to find user by id", tags = {"users"})
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a response entity containing a list of all users
     * @throws SQLException if an error occurs while accessing the database
     */
    @GetMapping("/users/all")
    @Operation(summary = "Find list users", description = "method provide user list", tags = {"users"})
    public ResponseEntity<List<UserDto>> getAllUsers() throws SQLException {
            List<UserDto> userDto = userService.getAllUsers();
            return ResponseEntity.ok(userDto);
    }
}
