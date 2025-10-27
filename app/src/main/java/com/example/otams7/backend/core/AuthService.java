package com.example.otams7.backend.core;

// com/seg2105/project/core/AuthService.java
package core;

import auth.LoginUseCase;
import auth.LogoutUseCase;
import auth.RegisterStudentUseCase;
import auth.RegisterTutorUseCase;
import data.AuthRepository;
import com.example.otams7.backend.data.InMemoryAuthRepository;
import data.models.Student;
import data.models.Tutor;

/**
 * Thin facade the UI can call.
 * Swap the underlying repository (e.g., FirebaseAuthRepository) without changing UI code.
 */
public class AuthService {
    private final AuthRepository repo;

    public AuthService(AuthRepository repo) {
        this.repo = repo;
    }

    // Convenience static factory for a default in-memory setup (with seeding).
    public static AuthService createDefaultSeeded(String adminEmail, String adminPassword) {
        InMemoryAuthRepository mem = new InMemoryAuthRepository();
        mem.seedAdmin(adminEmail, adminPassword);
        return new AuthService(mem);
    }

    public void registerStudent(String email, String password, String first, String last) {
        new RegisterStudentUseCase(repo).execute(new Student(email, password, first, last));
    }

    public void registerTutor(String email, String password, String first, String last) {
        new RegisterTutorUseCase(repo).execute(new Tutor(email, password, first, last));
    }

    /** Returns "Admin"|"Student"|"Tutor". */
    public String login(String email, String password) {
        return new LoginUseCase(repo).execute(email, password);
    }

    public void logout() { new LogoutUseCase(repo).execute(); }

    public boolean isLoggedIn() { return repo.isLoggedIn(); }

    public String currentRole() { return repo.currentRole(); }
}