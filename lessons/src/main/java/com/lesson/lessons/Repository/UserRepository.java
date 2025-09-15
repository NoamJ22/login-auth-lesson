package com.lesson.lessons.Repository;

import com.lesson.lessons.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//this interface is meant for communication with the user entity from the database

public interface UserRepository extends JpaRepository<User, Long> {
    //a method to get a user by its email
    Optional<User> findByEmail(String email);
}