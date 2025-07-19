package com.smartstock.controller;

import com.smartstock.models.User;
import com.smartstock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            return "Username already exists!";
        }
        userRepo.save(user);
        return "Registered successfully!";
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        User existing = userRepo.findByUsername(user.getUsername());
        if (existing == null || !existing.getPassword().equals(user.getPassword())) {
            return "Invalid credentials";
        }
        return "Login successful";
    }
}
