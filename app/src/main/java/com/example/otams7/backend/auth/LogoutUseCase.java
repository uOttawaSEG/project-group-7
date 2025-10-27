package com.example.otams7.backend.auth;

package auth;

import data.AuthRepository;

public class LogoutUseCase {
    private final AuthRepository repo;
    public LogoutUseCase(AuthRepository repo) { this.repo = repo; }
    public void execute() { repo.logout(); }
}