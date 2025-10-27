package com.example.otams7.backend.auth;

package auth;
import data.AuthRepository;
import data.models.Tutor;

public class RegisterTutorUseCase {
    private final AuthRepository repo;
    public RegisterTutorUseCase(AuthRepository repo) { this.repo = repo; }
    public void execute(Tutor t) { repo.registerTutor(t); }
}