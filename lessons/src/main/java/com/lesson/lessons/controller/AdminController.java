package com.lesson.lessons.controller;

import com.lesson.lessons.User.UserDto;
import com.lesson.lessons.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

//this is here to announce that this class is a controller
@RestController
//assigning this path to this controller
@RequestMapping("/admin")
public class AdminController {
    //this is a controller that holds all the admin role related methods

    //bringing the UserService, that will allow the controller to use it's
    //implemented methods
    private final UserService userService;

    //this is the constructor to inject the UserService
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    //this is a function to check if the current user has an ADMIN role
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    //the default enter screen for the admin, displays a simple string
    @GetMapping("/dashboard")
    public ResponseEntity<String> adminDashboard() {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        return ResponseEntity.ok("Admin Dashboard - You are logged in as Admin!");
    }

    //get all the users in the database
    @GetMapping("/users")
    public ResponseEntity<?> listUsers() {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //create a new user, post a valid user body for this
    @PostMapping("/users/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    //edit a certain user's profile, cannot edit a none existing user or put a non-valid body
    @PutMapping("/users/{id}/edit")
    public ResponseEntity<?> editUser(@PathVariable Long id, @Valid @RequestBody UserDto updatedUser) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    //delete a certain user's profile, cannot delete a none existing user
    @DeleteMapping("/users/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
    }

    //get all the roles in the database
    @GetMapping("/roles")
    public ResponseEntity<?> manageRoles() {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        return ResponseEntity.ok(userService.getAllRoles());
    }

    //returns a simple string
    @GetMapping("/reports")
    public ResponseEntity<?> systemReports() {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        return ResponseEntity.ok("System reports (placeholder)");
    }
}
