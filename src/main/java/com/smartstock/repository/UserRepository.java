package com.smartstock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstock.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
