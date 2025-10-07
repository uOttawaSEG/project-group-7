package com.example.otams7.classes;

public class Administrator extends AnyUser{

    //CONSTuct no attributes only
    public Administrator(String firstName, String lastName,
                   String email, String password, String phonenumber){
        super(firstName, lastName, email, password, phonenumber, "I am the Admin");


    }
}
