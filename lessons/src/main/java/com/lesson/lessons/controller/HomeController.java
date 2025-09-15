package com.lesson.lessons.controller;

import com.lesson.lessons.User.UserDto;
import com.lesson.lessons.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//this is a controller built to house paths that everyone, including users that didn't log in
//can access and see
public class HomeController {

    //injecting the user service so that people can register from here
    @Autowired
    private UserService userService;

    //returns a simple string
    @GetMapping("/")
    public String home() {
        return "Welcome to the homepage!";
    }

    //create a new user in the database, and assigns him the role of USER
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    //returns a simple string
    @GetMapping("/about")
    public String about() {
        return "About the app...";
    }

    //returns a simple string
    @GetMapping("/contact")
    public String contact() {
        return "Contact us at support@example.com";
    }
}
