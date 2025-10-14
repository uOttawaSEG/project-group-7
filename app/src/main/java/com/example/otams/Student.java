package com.example.otams;

public class Student {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String phone;
    public String program;

    public Student() {
        // Required empty constructor for Firebase
    }

    public Student(String firstName, String lastName, String email, String password, String phone, String program) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.program = program;
    }
}
