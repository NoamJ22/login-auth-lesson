package com.lesson.lessons.User;

import com.lesson.lessons.model.User;
import org.springframework.stereotype.Component;

//the mapper is the class responsible for conversion between a User entity and a UserDto
@Component
public class UserMapper {

    //convert User Dto to a User entity
    public User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    //convert User entity to UserDto
    public UserDto toDto(User entity) {
        if (entity == null) return null;

        return new UserDto(entity.getId(), entity.getEmail(), entity.getPassword());
    }
}
