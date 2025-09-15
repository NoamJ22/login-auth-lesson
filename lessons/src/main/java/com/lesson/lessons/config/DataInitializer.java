package com.lesson.lessons.config;
//all the needed imports
import com.lesson.lessons.model.Role;
import com.lesson.lessons.Repository.RoleRepository;
import com.lesson.lessons.User.UserDto;
import com.lesson.lessons.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//this class's purpose is to create new dummy users for the project when the DB
//is empty, for ease of use and to save time
@Configuration
public class DataInitializer {
    //CommandLineRunner means this will be the first thing that will run on the project, even before the app
    @Bean
    public CommandLineRunner initDefaults(RoleRepository roleRepository, UserService userService) {
        return args -> {

            // first create roles if they do not exist
            if (roleRepository.count() == 0) {
                Role userRole = new Role();
                userRole.setName("ROLE_USER");
                roleRepository.save(userRole);

                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);

                System.out.println("Default roles created");
            }

            // then create users if none exist: one admin and one user to test it from both
            //roles perspective
            if (userService.countUsers() == 0) {
                System.out.println("No users found. Creating default test users...");

                // user with both ADMIN and USER roles
                UserDto adminUser = new UserDto();
                adminUser.setEmail("admin@example.com");
                adminUser.setPassword("admin123");
                userService.createUser(adminUser);
                userService.assignRoleToUser("admin@example.com", "ROLE_USER");
                userService.assignRoleToUser("admin@example.com", "ROLE_ADMIN");

                // user with only USER role
                UserDto normalUser = new UserDto();
                normalUser.setEmail("user@example.com");
                normalUser.setPassword("user123");
                userService.createUser(normalUser);
                userService.assignRoleToUser("user@example.com", "ROLE_USER");
            }
        };
    }
}
