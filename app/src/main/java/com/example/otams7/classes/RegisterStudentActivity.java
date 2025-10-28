package com.example.otams7.classes;

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

import java.util.HashMap;
import java.util.Map;

public class RegisterStudentActivity extends AppCompatActivity {

    private EditText etStudentFirstName, etStudentLastName, etStudentEmail, etStudentPassword, etStudentPhone, etStudentProgram;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etStudentFirstName = findViewById(R.id.etStudentFirstName);
        etStudentLastName = findViewById(R.id.etStudentLastName);
        etStudentEmail = findViewById(R.id.etStudentEmail);
        etStudentPassword = findViewById(R.id.etStudentPassword);
        etStudentPhone = findViewById(R.id.etStudentPhone);
        etStudentProgram = findViewById(R.id.etStudentProgram);
        Button btnRegisterStudent = findViewById(R.id.btnRegisterStudent);
        Button btnBackFromStudent = findViewById(R.id.btnBackFromStudent);

        btnRegisterStudent.setOnClickListener(v -> registerStudent());
        btnBackFromStudent.setOnClickListener(v -> finish());
    }

    private void registerStudent() {
        String first = etStudentFirstName.getText().toString().trim();
        String last = etStudentLastName.getText().toString().trim();
        String email = etStudentEmail.getText().toString().trim();
        String pass = etStudentPassword.getText().toString().trim();
        String phone = etStudentPhone.getText().toString().trim();
        String program = etStudentProgram.getText().toString().trim();

        if (first.isEmpty()) { toast("Enter first name"); return; }
        if (last.isEmpty()) { toast("Enter last name"); return; }
        if (!email.contains("@")) { toast("Invalid email"); return; }
        if (pass.length() < 6) { toast("Password must be at least 6 characters"); return; }
        if (phone.isEmpty()) { toast("Enter phone number"); return; }
        if (program.isEmpty()) { toast("Enter program of study"); return; }


        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();


                            Map<String, Object> student = new HashMap<>();
                            student.put("firstName", first);
                            student.put("lastName", last);
                            student.put("email", email);
                            student.put("phoneNumber", phone);
                            student.put("programofstudy", program);
                            student.put("role", "Student");
                            student.put("status", "PENDING");

                            db.collection("users").document(uid)
                                    .set(student)
                                    .addOnSuccessListener(aVoid -> {
                                        toast("Registration successful!");
                                        finish();
                                    })
                                    .addOnFailureListener(e -> toast("Error saving student: " + e.getMessage()));
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
