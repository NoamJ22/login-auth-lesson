package com.lesson.lessons.service;

import com.lesson.lessons.model.User;
import com.lesson.lessons.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
//this class is responsible for implementing custom logic to load users during authentication.
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    //bringing and injecting the userRepository to use its findByEmail method

    @Override
    //in this case the username is the user's email
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //this line is for debugging
        System.out.println("Attempting to load user by email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // maps each role to different grantAuthority
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        //returns the user built in this function
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

}
