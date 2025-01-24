package com.yuliia.airlines_api.roles;

import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role getById(Long id) {
        return repository.findById(id).orElseThrow( () -> new RuntimeException("Role not found"));
    }

    public Set<Role> assignDefaultRole() {
        Role defaultRole = this.getById(1L);

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);

        return roles;
    }
}
