package com.example.otams7.backend.auth;


import com.example.otams7.backend.data.AuthRepository;
import com.example.otams7.backend.data.models.Tutor;

public class RegisterTutorUseCase {
    private final AuthRepository repo;
    public RegisterTutorUseCase(AuthRepository repo) { this.repo = repo; }
    public void execute(Tutor t) { repo.registerTutor(t); }
}