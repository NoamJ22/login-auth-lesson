package com.lesson.lessons.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
//announces that this is an entity and will be mapped to the database
@Table(name = "roles")
//name of the table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Id marks this as the primary key

    @Column(unique = true, nullable = false)
    private String name;
    //this is a second field, the user's name

    //this announces that the relationship is Many To Many with the Users, and it is mapped by the roles
    @ManyToMany(mappedBy = "roles")
    // If you add @ToString, you must exclude collections in bidirectional relationships
    @ToString.Exclude
    //this annotation is to prevent a back and forth loop
    // with the users when calling all roles via admin/roles
    @JsonBackReference
    //the set of the users
    private Set<User> users = new HashSet<>();
}
