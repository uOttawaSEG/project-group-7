package com.example.otams7;


//same imports as main activity default file
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.otams7.classes.AuthRepo;
import com.example.otams7.classes.Tutor;

import java.util.ArrayList;

import java.util.List;


public class RegisterTutorActivity extends AppCompatActivity {

    private EditText etTutorFirstName, etTutorLastName, etTutorEmail, etTutorPassword, etTutorPhone, etTutorDegree, etTutorCourses;
    private AuthRepo repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tutor);

        //
        repo = AuthRepo.getInstance();

        etTutorFirstName = findViewById(R.id.etTutorFirstName);
        etTutorLastName = findViewById(R.id.etTutorLastName);
        etTutorEmail = findViewById(R.id.etTutorEmail);
        etTutorPassword = findViewById(R.id.etTutorPassword);
        etTutorPhone = findViewById(R.id.etTutorPhone);
        etTutorDegree = findViewById(R.id.etTutorDegree);
        etTutorCourses = findViewById(R.id.etTutorCourses);
        Button btnRegisterTutor = findViewById(R.id.btnRegisterTutor);
        Button btnBackFromTutor = findViewById(R.id.btnBackFromTutor);

        btnRegisterTutor.setOnClickListener(v -> registerTutor());
        btnBackFromTutor.setOnClickListener(v -> finish());
    }

    private void registerTutor() {

        //set with variables string to use for constructing a tutor object after
        String first = etTutorFirstName.getText().toString().trim();
        String last = etTutorLastName.getText().toString().trim();
        String email = etTutorEmail.getText().toString().trim();
        String pass = etTutorPassword.getText().toString().trim();
        String phone = etTutorPhone.getText().toString().trim();
        String degree = etTutorDegree.getText().toString().trim();
        String coursesInput = etTutorCourses.getText().toString().trim();

        // confitions  for inputs to nove have errors
        if (first.isEmpty()) { toast("Enter first name"); return; }
        if (last.isEmpty()) { toast("Enter last name"); return; }
        if (!email.contains("@")) { toast("Invalid email"); return; }
        if (pass.length() <2) { toast("Password must be at least 2 "); return; }
        if (phone.isEmpty()) { toast("Enter phone number"); return; }
        if (degree.isEmpty()) { toast("Enter highest degree"); return; }
        if (coursesInput.isEmpty()) { toast("Enter > one course"); return; }

        List<String> courses = new ArrayList<>();
        for (String c : coursesInput.split(",")) {
            String course = c.trim();
            if (!course.isEmpty()) courses.add(course);
        }

        //if not foudn in list say if list null put toast message

        if (courses.isEmpty()) {
            toast("hey tutor provide a  courses");
            return;
        }

        Tutor t = new Tutor(first, last, email, pass, phone, degree, courses);

        boolean ok = repo.registerTutor(t); //call method from AuthRepo
        if (!ok) {
            toast("Email already registered");
        } else {
            toast("Tutor registered, please log in");
            finish(); // back to login
        }
    }

    //reference https://developer.android.com/guide/topics/ui/notifiers/toasts

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}



