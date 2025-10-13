package com.example.otams;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private EditText firstName, lastName, email, password, phone, program;
    private Button registerButton;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        program = findViewById(R.id.program);
        registerButton = findViewById(R.id.registerButton);
        errorText = findViewById(R.id.errorText);

        registerButton.setOnClickListener(v -> {
            String first = firstName.getText().toString().trim();
            String last = lastName.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String phoneNum = phone.getText().toString().trim();
            String prog = program.getText().toString().trim();

            if (!mail.contains("@")) {
                errorText.setText("Invalid email address.");
                errorText.setTextColor(Color.parseColor("#F44336"));
                errorText.setVisibility(View.VISIBLE);
                return;
            }

            if (pass.length() < 2) {
                errorText.setText("Password must be at least 2 characters.");
                errorText.setTextColor(Color.parseColor("#F44336"));
                errorText.setVisibility(View.VISIBLE);
                return;
            }

            if (first.isEmpty() || last.isEmpty() || mail.isEmpty() || pass.isEmpty() || phoneNum.isEmpty() || prog.isEmpty()) {
                errorText.setText("Please fill in all fields.");
                errorText.setVisibility(View.VISIBLE);
                return;
            }
            else {
                errorText.setText("Registration Successful!");
                errorText.setTextColor(Color.parseColor("#4CAF50"));
                firstName.setText("");
                lastName.setText("");
                email.setText("");
                password.setText("");
                phone.setText("");
                program.setText("");
                errorText.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Registered, please log in.", Toast.LENGTH_SHORT).show();
                StudentRepository repo = new StudentRepository();
                repo.registerStudent(first, last, mail, pass, phoneNum, prog);


            }
        });
    }
}