package com.example.otams7.classes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otams7.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterStudentActivity extends AppCompatActivity {

    private EditText etStudentFirstName, etStudentLastName, etStudentEmail, etStudentPassword, etStudentPhone, etStudentProgram;
    private AuthRepo repo;

    FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student); // Link to the correct layout XML

        // Initialize the repository and UI components
        repo = AuthRepo.getInstance();

        etStudentFirstName = findViewById(R.id.etStudentFirstName);
        etStudentLastName = findViewById(R.id.etStudentLastName);
        etStudentEmail = findViewById(R.id.etStudentEmail);
        etStudentPassword = findViewById(R.id.etStudentPassword);
        etStudentPhone = findViewById(R.id.etStudentPhone);
        etStudentProgram = findViewById(R.id.etStudentProgram);
        Button btnRegisterStudent = findViewById(R.id.btnRegisterStudent);
        Button btnBackFromStudent = findViewById(R.id.btnBackFromStudent);

        // Register button logic
        btnRegisterStudent.setOnClickListener(v -> registerStudent());
        btnBackFromStudent.setOnClickListener(v -> finish()); // Finish activity and return to previous screen
    }

    private void registerStudent() {
        // Get input values from the EditText fields
        String first = etStudentFirstName.getText().toString().trim();
        String last = etStudentLastName.getText().toString().trim();
        String email = etStudentEmail.getText().toString().trim();
        String pass = etStudentPassword.getText().toString().trim();
        String phone = etStudentPhone.getText().toString().trim();
        String program = etStudentProgram.getText().toString().trim();

        // Validate the inputs
        if (first.isEmpty()) { toast("Enter first name"); return; }
        if (last.isEmpty()) { toast("Enter last name"); return; }
        if (!email.contains("@")) { toast("Invalid email"); return; }
        if (pass.length() < 6) { toast("Password must be at least 6 characters"); return; }
        if (phone.isEmpty()) { toast("Enter phone number"); return; }
        if (program.isEmpty()) { toast("Enter program of study"); return; }

        // Create the student object
        Student student = new Student(first, last, email, pass, phone, program);

        // Register the student using the repository
        boolean isRegistered = repo.registerStudent(student);

        if (!isRegistered) {
            toast("Email already registered");
        } else {
            toast("Registration successful, please log in");


            Map<String, Object> student1 = new HashMap<>();
            student1.put("firstName", first);
            student1.put("lastName", last);
            student1.put("email", email);
            student1.put("password", pass);
            student1.put("phoneNumber", phone);
            student1.put("program of study", pass);
            student1.put("role", "Student");

            db.collection("users")
                    .add(student1)
                    .addOnSuccessListener((DocumentReference doc) ->
                            Toast.makeText(this, "Student saved in database!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error not saved!: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}