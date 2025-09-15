package com.lesson.lessons.Repository;

import com.lesson.lessons.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//this interface is meant for communication with the role entity from the database

public interface RoleRepository extends JpaRepository<Role, Long> {
    //a method to get a role by its name
    Optional<Role> findByName(String name);
}
