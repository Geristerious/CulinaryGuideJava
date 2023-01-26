package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.Authentication.Role;
import com.example.CulinaryGuide.models.Authentication.Userstable;
import com.example.CulinaryGuide.repository.RoleRepository;
import com.example.CulinaryGuide.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Role loadRoleByUsername(String username)  {
        Role role = roleRepository.findByUsername(username);

        if (role == null) {
            return new Role();
        }

        return role;
    }

    public List<Role> allRoles() {
        return roleRepository.findAll();
    }

    public boolean saveRole(Role role) {
        roleRepository.save(role);
        return true;
    }
}
