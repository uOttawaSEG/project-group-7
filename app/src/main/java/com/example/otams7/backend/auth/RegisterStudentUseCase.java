package com.example.otams7.backend.auth;


import com.example.otams7.backend.data.AuthRepository;
import com.example.otams7.backend.data.models.Student;

public class RegisterStudentUseCase {
    private final AuthRepository repo;
    public RegisterStudentUseCase(AuthRepository repo) { this.repo = repo; }
    public void execute(Student s) { repo.registerStudent(s); }
}