package com.yuliia.airlines_api.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuliia.airlines_api.security.Authority;
import com.yuliia.airlines_api.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
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

    public Role(Set<User> users, String name, Set<Authority> authorities) {
        this.users = users;
        this.name = name;
    }
}
