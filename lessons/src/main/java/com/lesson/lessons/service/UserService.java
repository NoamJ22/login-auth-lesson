package com.lesson.lessons.service;

import com.lesson.lessons.User.UserDto;
import com.lesson.lessons.model.Role;


import java.util.List;
import java.util.Optional;

//the service interface, to determine ahead of time the methods used in the project
public interface UserService {
    UserDto createUser(UserDto userDto);
    long countUsers();
    void assignRoleToUser(String email, String roleName);

    // --- Admin operations ---
    List<UserDto> getAllUsers();
    Optional<UserDto> getUserById(Long id);
    UserDto updateUser(Long id, UserDto updatedUser);
    void deleteUser(Long id);
    List<Role> getAllRoles();

    // --- User profile operations ---
    Optional<UserDto> getCurrentUser(); // based on logged-in user
    UserDto updateCurrentUser(UserDto userDto);
}
