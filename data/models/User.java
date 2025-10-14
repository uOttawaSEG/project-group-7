package data.models;

import java.util.Objects;
import java.util.UUID;

public abstract class User {
    private final String id = UUID.randomUUID().toString();
    private final String email;
    private final String password; // NOTE: plain text for the exercise
    private final Role role;

    protected User(String email, String password, Role role) {
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.role = Objects.requireNonNull(role);
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
}
