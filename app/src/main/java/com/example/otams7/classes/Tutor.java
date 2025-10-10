package com.example.otams7.classes;
import java.util.ArrayList;
import java.util.List;


public class Tutor  extends AnyUser{
    private String highestdegree;
    private List<String> coursesoffered;

    public Tutor(String firstName, String lastName, String email, String password, String phonenumber,
                 String highestdegree, List<String> coursesoffered){

        super(firstName, lastName, email, password, phonenumber, "my role is a Tutor");
        this.highestdegree=highestdegree;
        this.coursesoffered= new ArrayList<>(coursesoffered);
    }


    public List<String> getCoursesoffered() {
        return coursesoffered;
    }

    public void setCoursesoffered(List<String> coursesoffered) {
        this.coursesoffered = coursesoffered;
    }

    public String getHighestdegree() {
        return highestdegree;
    }

    public void setHighestdegree(String highestdegree) {
        this.highestdegree = highestdegree;
    }
}
