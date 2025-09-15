package com.lesson.lessons.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

//this is a User Dto, it is the class that carries the users relevant stats
//in the upper layers of the app
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    //the primary key

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    //a field of email type

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    //the user password
}
