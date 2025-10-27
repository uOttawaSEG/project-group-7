package com.example.otams7;


//same imports as main activity default file
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.otams7.classes.AuthRepo;
import com.example.otams7.classes.Tutor;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegisterTutorActivity extends AppCompatActivity {


    FirebaseFirestore db= FirebaseFirestore.getInstance();

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
        if (pass.length() <6) { toast("Password must be at least 6 "); return; }
        if (phone.isEmpty()) { toast("Enter phone number"); return; }
        if (degree.isEmpty()) { toast("Enter highest degree"); return; }
        if (coursesInput.isEmpty()) { toast("Enter > one course"); return; }

        List<String> coursesarray = new ArrayList<>();
        for (String c : coursesInput.split(",")) {
            String coursetrimmed = c.trim(); //get rid of white spaces
            if (!coursetrimmed.isEmpty()) coursesarray.add(coursetrimmed);
        }

        //if not foudn in list say if list null put toast message

        if (coursesarray.isEmpty()) {
            toast("hey tutor provide a  courses");
            return;
        }

        Tutor t = new Tutor(first, last, email, pass, phone, degree, coursesarray);

        boolean ok = repo.registerTutor(t); //call method from AuthRepo
        if (!ok) {
            toast("Email already registered");
        } else {
            toast("Hey tutor you are  registered, please log in");


            Map<String, Object> tutor = new HashMap<>();
            tutor.put("firstName", first);
            tutor.put("lastName", last);
            tutor.put("email", email);
            tutor.put("password", pass);
            tutor.put("phoneNumber", phone);
            tutor.put("highestDegree", degree);
            tutor.put("coursesOffered", coursesarray);
            tutor.put("role", "Tutor");

            db.collection("users")
                    .add(tutor)
                    .addOnSuccessListener((DocumentReference doc) ->
                            Toast.makeText(this, "Tutor saved in database!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error not saved!: " + e.getMessage(), Toast.LENGTH_SHORT).show());




        }

    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



}



