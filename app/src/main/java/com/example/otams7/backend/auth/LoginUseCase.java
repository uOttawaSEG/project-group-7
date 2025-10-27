package com.example.otams7.backend.auth;



import com.example.otams7.backend.data.AdminSeeder;
import com.example.otams7.backend.data.AuthRepository;

public class LoginUseCase {
    private final AuthRepository repo;
    public LoginUseCase(AuthRepository repo) { this.repo = repo; }

    /** Returns the exact role string: "Admin"|"Student"|"Tutor". */
    public String execute(String email, String password) {
        return repo.login(email, password);
    }
}