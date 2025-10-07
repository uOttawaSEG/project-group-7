package com.example.otams7.classes;

public class Student extends AnyUser{

    private String programofstudy;

    //construct student inherits from anyuser attributes
    public Student( String firstName, String lastName, String email, String password,
                    String phonenumber, String programofstudy){
        //inherit from anyuser and the role is student
        super(firstName, lastName, email, password, phonenumber, "My role is a student");
        this.programofstudy=programofstudy;



    }


    public String getProgramofstudy() {
        return programofstudy;
    }

    public void setProgramofstudy(String programofstudy) {
        this.programofstudy = programofstudy;
    }
}
