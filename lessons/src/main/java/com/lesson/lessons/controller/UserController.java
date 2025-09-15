package com.lesson.lessons.controller;

import com.lesson.lessons.User.UserDto;
import com.lesson.lessons.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/user")
public class UserController {
    //this is a controller that holds all the user role related methods


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //this is a function to check if the current user has an USER role
    private boolean isAuthenticatedWithRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    //returns the current user
    @GetMapping("/profile")
    public ResponseEntity<?> userProfile() {
        Optional<UserDto> currentUser = userService.getCurrentUser();

        if (currentUser.isPresent()) {
            return ResponseEntity.ok(currentUser.get());
        } else {
            return ResponseEntity.badRequest().body("Could not find current user");
        }
    }

    //edit the current user stats
    @PutMapping("/profile/edit")
    public ResponseEntity<?> editUserProfile(@RequestBody UserDto updatedUserDto) {
        if (!isAuthenticatedWithRole("ROLE_USER") && !isAuthenticatedWithRole("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Access denied.");
        }

        UserDto updatedUser = userService.updateCurrentUser(updatedUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    //returns a string, it's different whether the user is also an admin or not
    @GetMapping("/menu")
    public ResponseEntity<String> getMenu() {
        if (isAuthenticatedWithRole("ROLE_ADMIN")) {
            return ResponseEntity.ok("Admin Menu");
        } else if (isAuthenticatedWithRole("ROLE_USER")) {
            return ResponseEntity.ok("User Menu");
        } else {
            return ResponseEntity.status(403).body("Access denied.");
        }
    }

    //returns a simple string
    @GetMapping("/dashboard")
    public ResponseEntity<String> userDashboard() {
        if (!isAuthenticatedWithRole("ROLE_USER") && !isAuthenticatedWithRole("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        return ResponseEntity.ok("Welcome to your personal dashboard!");
    }

    //returns a simple string
    @GetMapping("/settings")
    public ResponseEntity<String> userSettings() {
        if (!isAuthenticatedWithRole("ROLE_USER") && !isAuthenticatedWithRole("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        return ResponseEntity.ok("Your personal settings");
    }

    //returns a simple string
    @GetMapping("/activity")
    public ResponseEntity<String> userActivity() {
        if (!isAuthenticatedWithRole("ROLE_USER") && !isAuthenticatedWithRole("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Access denied.");
        }
        return ResponseEntity.ok("Your recent activity");
    }
}
