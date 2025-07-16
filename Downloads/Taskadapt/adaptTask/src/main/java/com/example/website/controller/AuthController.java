package com.example.website.controller;

import com.example.website.model.User;
import com.example.website.model.User.Role;
import com.example.website.repository.UserRepository;
import com.example.website.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtUtil jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    /* ---------- CUSTOMER registration (default) ---------- */
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody AuthRequest req) {
        return createUser(req, Role.CUSTOMER, "Registered");
    }

    /* ---------- ADMIN registration (bootstrap only) ---------- */
    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest req) {
        return createUser(req, Role.ADMIN, "Admin registered");
    }

    /* ---------- LOGIN (common for both roles) ---------- */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        var user = users.findByUsername(req.username());
        if (user == null || !encoder.matches(req.password(), user.getPassword())) {
            return ResponseEntity.status(401).body("Bad credentials");
        }
        String token = jwt.generate(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    /* ---------- helper ---------- */
    private ResponseEntity<?> createUser(AuthRequest req, Role role, String msg) {
        if (users.findByUsername(req.username()) != null) {
            return ResponseEntity.badRequest().body("Username taken");
        }
        User user = new User(null, req.username(), encoder.encode(req.password()), role);
        users.save(user);
        return ResponseEntity.ok(msg);
    }

    /* ---------- records ---------- */
    record AuthRequest(String username, String password) {}
    record TokenResponse(String token) {}
}
