package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.Authentication.Userstable;
import com.example.CulinaryGuide.models.Ingredient;
import com.example.CulinaryGuide.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Userstable loadUserByUsername(String username)  {
        Userstable user = userRepository.findByUsername(username);

        if (user == null) {
            return new Userstable();
        }

        return user;
    }

    public Userstable findUserById(Long userId) {
        Optional<Userstable> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new Userstable());
    }
//    public Userstable post(Long userId) {
//        Optional<Userstable> userFromDb = userRepository.findById(userId);
//        return userFromDb.orElse(new Userstable());
//    }

    public void postUser(Userstable userstable){
        userRepository.save(userstable);
    }
    public List<Userstable> allUsers() {
        return userRepository.findAll();
    }



    public boolean saveUser(Userstable user) {
        Userstable userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //user.setRole(new Role(1L, "User"));
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword((user.getPassword()));
        userRepository.save(user);
        return true;
    }




}
