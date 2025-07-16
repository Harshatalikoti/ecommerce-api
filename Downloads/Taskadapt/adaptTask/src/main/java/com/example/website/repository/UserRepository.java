package com.example.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    /** Find a user by username (used in login & JWT auth). */
    User findByUsername(String username);

    /** Check if a username already exists (helpful in registration). */
    boolean existsByUsername(String username);
}

