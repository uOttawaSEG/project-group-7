// com/seg2105/project/data/InMemoryAuthRepository.java
package com.example.otams7.backend.data;
import data.models.Role;
import data.models.Student;
import data.models.Tutor;
import data.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryAuthRepository implements AuthRepository {
    private final Map<String, User> byEmail = new HashMap<>();
    private String currentEmail = null;

    /** Seed an administrator user (use README creds). */
    public void seedAdmin(String email, String password) {
        byEmail.put(email, new User(email, password, Role.Admin) {});
    }

    @Override
    public void registerStudent(Student student) {
        Objects.requireNonNull(student);
        String email = student.getEmail();
        if (byEmail.containsKey(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        byEmail.put(email, student);
    }

    @Override
    public void registerTutor(Tutor tutor) {
        Objects.requireNonNull(tutor);
        String email = tutor.getEmail();
        if (byEmail.containsKey(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        byEmail.put(email, tutor);
    }

    @Override
    public String login(String email, String password) {
        User u = byEmail.get(email);
        if (u == null || !u.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        currentEmail = email;
        return u.getRole().value(); // exact role string for Welcome screen
    }

    @Override
    public void logout() { currentEmail = null; }

    @Override
    public boolean isLoggedIn() { return currentEmail != null; }

    @Override
    public String currentRole() {
        if (!isLoggedIn()) return null;
        return byEmail.get(currentEmail).getRole().value();
    }
}