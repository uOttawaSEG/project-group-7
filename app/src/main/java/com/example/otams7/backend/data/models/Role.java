

package com.example.otams7.backend.data.models;
public enum Role {
    Admin("Admin"),
    Student("Student"),
    Tutor("Tutor");

    private final String value;
    Role(String value) { this.value = value; }
    public String value() { return value; }
}