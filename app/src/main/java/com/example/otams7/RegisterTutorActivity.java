package com.example.otams7;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otams7.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterTutorActivity extends AppCompatActivity {

    private EditText etTutorFirstName, etTutorLastName, etTutorEmail, etTutorPassword, etTutorPhone, etTutorDegree, etTutorCourses;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tutor);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
        String first = etTutorFirstName.getText().toString().trim();
        String last = etTutorLastName.getText().toString().trim();
        String email = etTutorEmail.getText().toString().trim();
        String pass = etTutorPassword.getText().toString().trim();
        String phone = etTutorPhone.getText().toString().trim();
        String degree = etTutorDegree.getText().toString().trim();
        String coursesInput = etTutorCourses.getText().toString().trim();

        if (first.isEmpty()) { toast("Enter first name"); return; }
        if (last.isEmpty()) { toast("Enter last name"); return; }
        if (!email.contains("@")) { toast("Invalid email"); return; }
        if (pass.length() < 6) { toast("Password must be at least 6 characters"); return; }
        if (phone.isEmpty()) { toast("Enter phone number"); return; }
        if (degree.isEmpty()) { toast("Enter highest degree"); return; }
        if (coursesInput.isEmpty()) { toast("Enter at least one course"); return; }

        // Convert courses string to list
        List<String> courses = new ArrayList<>();
        for (String c : coursesInput.split(",")) {
            String trimmed = c.trim();
            if (!trimmed.isEmpty()) courses.add(trimmed);
        }
        if (courses.isEmpty()) { toast("Provide at least one course"); return; }

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();

                            Map<String, Object> tutor = new HashMap<>();
                            tutor.put("firstName", first);
                            tutor.put("lastName", last);
                            tutor.put("email", email);
                            tutor.put("phoneNumber", phone);
                            tutor.put("highestDegree", degree);
                            tutor.put("coursesOffered", courses);
                            tutor.put("role", "Tutor");
                            tutor.put("status", "PENDING");

                            db.collection("users").document(uid)
                                    .set(tutor)
                                    .addOnSuccessListener(aVoid -> {
                                        toast("Registration successful!");
                                        finish();
                                    })
                                    .addOnFailureListener(e -> toast("Error saving tutor: " + e.getMessage()));
                        } else {
                            toast("Registration failed: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
