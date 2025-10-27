package com.example.otams7.classes;
import com.example.otams7.classes.Tutor;
import com.example.otams7.classes.Student;
import com.example.otams7.classes.AnyUser;
import java.util.ArrayList;
import java.util.List;

public class AuthRepo {


    private static AuthRepo instance;
    private List<AnyUser> users= new ArrayList<>(); //insitailze users list for fake DB

    //constrcutor for default repo  use admin

    public AuthRepo(){

        users.add(new AnyUser("Admin","Admin",
                "admin@uottawa.ca","1234","0000000000","Adminstrator","PENDING!"));
    }


    public boolean registerTutor(Tutor t) {
        if (findUserByEmail(t.getEmail()) != null) return false;
        users.add(t);
        return true;
    }

    public boolean registerStudent(Student std) {
        if (findUserByEmail(std.getEmail()) != null) return false;
        users.add(std);
        return true;
    }



    public static AuthRepo getInstance(){

        if (instance == null) instance = new AuthRepo();
        return instance;


    }
    public String login(String email, String password) {
        AnyUser u = findUserByEmail(email);
        if (u != null && u.getPassword().equals(password)) {
            return u.getRole();
        }
        return null;
    }
    private AnyUser findUserByEmail(String email) {
        for (AnyUser u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }


}
