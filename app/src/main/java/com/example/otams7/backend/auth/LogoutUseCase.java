package com.example.otams7.backend.auth;



import com.example.otams7.backend.data.AuthRepository;


public class LogoutUseCase {
    private final AuthRepository repo;
    public LogoutUseCase(AuthRepository repo) { this.repo = repo; }
    public void execute() { repo.logout(); }
}