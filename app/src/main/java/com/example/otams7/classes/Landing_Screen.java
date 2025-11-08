package com.example.otams7.classes;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otams7.R;
import com.example.otams7.RegisterTutorActivity;
import com.example.otams7.adminactivity.AdminInboxActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Landing_Screen extends AppCompatActivity {
    public static final String newLand = "NEW USER";
    public static final String newAdmin = "NEW ADMIN";



    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth auth= FirebaseAuth.getInstance();









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);





        View tutor = findViewById(R.id.btnRegisterTutor);
        if (tutor != null) {
            tutor.setOnClickListener(v -> {

                Intent registerTutorIntent = new Intent(Landing_Screen.this, RegisterTutorActivity.class);
                startActivity(registerTutorIntent);
            });
        }

        View student = findViewById(R.id.linkRegisterStudent);
        if (student != null) {
            student.setOnClickListener(v -> {

                Intent registerStudentIntent = new Intent(Landing_Screen.this, RegisterStudentActivity.class);
                startActivity(registerStudentIntent);
            });
        }

        Button signIn = findViewById(R.id.btnSignin);
        EditText emailAdd = findViewById(R.id.email);
        EditText userPassword = findViewById(R.id.password);

        signIn.setOnClickListener(v -> {
            String emailAddress = emailAdd.getText().toString().trim();
            String password = userPassword.getText().toString();

            final String adEm = "admin@otams.com";
            final String adPa = "Admin@$345";

            if (emailAddress.equals(adEm) && password.equals(adPa)) {
                Intent inbox=  new Intent(this,AdminInboxActivity.class);
                inbox.putExtra(newLand,"Administrator");
                startActivity(inbox);
                Toast.makeText(this,"Welcome ADMINSTRATOR TO YOUR INBOX",Toast.LENGTH_SHORT).show();





            }
            else
            //any user student/tutor
            {
//                Toast.makeText(this, "Hey student/tutor you have now logged in", Toast.LENGTH_SHORT).show();
                auth.signInWithEmailAndPassword(emailAddress, password).addOnSuccessListener(authResult -> checkUserStatus(auth.getCurrentUser().getUid())).
                        addOnFailureListener(e ->
                                Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());


            }





        });











    }
    private void openAdminInbox() {
        Intent i = new Intent(this, AdminInboxActivity.class);
        i.putExtra(newAdmin, "Administrator");
        startActivity(i);
    }
    private void checkUserStatus(String uid){
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(!documentSnapshot.exists()) {
                        Toast.makeText(this, "No user record found.", Toast.LENGTH_SHORT).show();
                        return;


                    }
                    String role = documentSnapshot.getString("role");
                    String status = documentSnapshot.getString("status");

                    if(status==null){
                        Toast.makeText(this, "Your account has no status assigned.", Toast.LENGTH_SHORT).show();
                        return;

                    }

                    switch (status) {

                        case "APPROVED":
                            if ("Tutor".equalsIgnoreCase(role)) {
                                startActivity(new Intent(this, TutorActivity.class));
                                Toast.makeText(this, "You are now  logged in  you have been approved " + role, Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                            }

                            break;

                        case "PENDING":
                            Toast.makeText(this, "Your tutor registration is pending admin approval.", Toast.LENGTH_LONG).show();
                            break;


                        case "REJECTED":
                            Toast.makeText(this, "Your tutor registration request was rejected by admin.", Toast.LENGTH_LONG).show();
                            break;


                        default:
                            Toast.makeText(this,"Unkown status", Toast.LENGTH_SHORT).show();


                    }


                }).addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to fetch user info: " + e.getMessage(), Toast.LENGTH_SHORT).show());






    }
}