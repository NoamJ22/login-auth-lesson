package com.lesson.lessons.service;

//the required imports
import com.lesson.lessons.Exceptions.UserNotFound;
import com.lesson.lessons.Repository.RoleRepository;
import com.lesson.lessons.Repository.UserRepository;
import com.lesson.lessons.User.UserDto;
import com.lesson.lessons.User.UserMapper;
import com.lesson.lessons.model.Role;
import com.lesson.lessons.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//this is the user service, and is the one handling all the logic in the app
@Service
class UserServiceImpl implements UserService {
    //the UserService forces the impl to implement all the methods written in it
    //for the project

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    //injecting the required stuff to fully implement this Service

    //transactional ensures that the method will fully complete else do nothing
    @Transactional
    @Override
    //this is a method to create a new user within the database
    //receiving a UserDto and
    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email must not be blank.");
        }

        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be blank.");
        }

        User user = userMapper.toEntity(userDto); // raw password here
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode it

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.getRoles().add(userRole);

        userRepository.save(user);
        return userMapper.toDto(user);
    }


    //assign a new role to an existing user
    @Override
    public void assignRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User with email " + email + " not found."));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found."));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    //return the amount of users in the database
    @Override
    public long countUsers() {
        return userRepository.count();
    }

    //returns a list of all the users in the database
    @Override
    public List<UserDto> getAllUsers() {
        //making it so that the user's password will not be displayed
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), null))
                .collect(Collectors.toList());
    }

    //find a specific user with his id
    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDto(user.getId(), user.getEmail(), null));
    }

    //find a specific user with his id and replace his stats with a userDto that is received
    @Override
    public UserDto updateUser(Long id, UserDto updatedUser) {
        //check if the email is valid
        if (updatedUser.getEmail() == null || updatedUser.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email must not be blank.");
        }

        //find the user and assemble him
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User with ID " + id + " not found."));

        user.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    //delete a user by his id
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFound("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    //get a list of all the roles in the database
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    //returns the stats of the current user
    @Override
    public Optional<UserDto> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email)
                .map(user -> new UserDto(user.getId(), user.getEmail(), null));
    }

    //update the stats of the current user and replace his stats with a userDto that is received
    @Override
    public UserDto updateCurrentUser(UserDto updatedUserDto) {
        //check if the email is not blank
        if (updatedUserDto.getEmail() == null || updatedUserDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email must not be blank.");
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;


        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            email = principal.toString();
        }

        //find the user and replace his stats
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User not found."));

        user.setEmail(updatedUserDto.getEmail());

        if (updatedUserDto.getPassword() != null && !updatedUserDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }

        userRepository.save(user);

        return userMapper.toDto(user);
    }

}
