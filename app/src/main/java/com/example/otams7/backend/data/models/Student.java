package com.example.otams7.backend.data.models;




public class Student extends User {
    private final String firstName;
    private final String lastName;

    public Student(String email, String password, String firstName, String lastName) {
        super(email, password, Role.Student);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
}