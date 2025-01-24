package com.yuliia.airlines_api.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuliia.airlines_api.users.User;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(nullable = false)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    Set<User> users;
}
