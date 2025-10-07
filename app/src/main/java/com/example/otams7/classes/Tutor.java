package com.example.otams7.classes;
import java.util.ArrayList;


public class Tutor  extends AnyUser{
    private String highestdegree;
    private ArrayList<String> coursesoffered;

    public Tutor(String firstName, String lastName, String email, String password, String phonenumber,
                 String highestdegree, ArrayList<String> coursesoffered){

        super(firstName, lastName, email, password, phonenumber, "my role is a Tutor");
        this.highestdegree=highestdegree;
        this.coursesoffered=coursesoffered;
    }


    public ArrayList<String> getCoursesoffered() {
        return coursesoffered;
    }

    public void setCoursesoffered(ArrayList<String> coursesoffered) {
        this.coursesoffered = coursesoffered;
    }

    public String getHighestdegree() {
        return highestdegree;
    }

    public void setHighestdegree(String highestdegree) {
        this.highestdegree = highestdegree;
    }
}
