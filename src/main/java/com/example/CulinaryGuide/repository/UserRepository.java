package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.Authentication.Userstable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Userstable, Long> {
    Userstable findByUsername(String username);
}