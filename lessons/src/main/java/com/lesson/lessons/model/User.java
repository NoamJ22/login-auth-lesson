package com.lesson.lessons.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
//announces that this is an entity and will be mapped to the database
@Table(name = "users")
//name of the table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Id marks this as the primary key


    @Email
    //this makes the field a special type  fit for email
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    //this is to encrypt the password in the database
    @JsonIgnore
    private String password;

    //eager makes the users come always with all their roles
    //brings it from their join table
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    // If you add @ToString, you must exclude collections in bidirectional relationships
    @ToString.Exclude
    //this annotation is to prevent a back and forth loop
    // with the users when calling all roles via admin/roles
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();
    //the set of roles for the users
}