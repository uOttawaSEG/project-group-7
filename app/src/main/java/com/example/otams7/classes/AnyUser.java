package com.example.otams7.classes;

public  class AnyUser {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String phonenumber;

    //get role of user
    private String role;

    //construct all params


    public AnyUser(String firstName, String lastName,
                   String email, String password, String phonenumber, String role){

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.role = role;


    }



    //getters/setters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setRole(String role) {
        this.role = role;
    }




}
