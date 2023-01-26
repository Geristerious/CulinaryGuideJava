package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.Authentication.Role;
import com.example.CulinaryGuide.models.Authentication.Userstable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByUsername(String username);
}