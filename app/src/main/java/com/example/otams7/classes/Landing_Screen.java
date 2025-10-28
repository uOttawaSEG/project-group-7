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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Landing_Screen extends AppCompatActivity {
    public static final String newLand = "NEW USER";
    public static final String newAdmin = "NEW ADMIN";


    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private void login(String adminLogin) {
        Intent newAdm = new Intent(this, LogoutPage.class);
        newAdm.putExtra(newAdmin, adminLogin);
        newAdm.putExtra(newLand, adminLogin);
        startActivity(newAdm);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);



        View admin = findViewById(R.id.linkDevAdmin);
        if(admin != null) {
            admin.setOnClickListener(v -> login("Administrator"));
        }

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









//                Intent log = new Intent(this, AdminInboxActivity.class);
//                log.putExtra(newLand, "Administrator");
//                startActivity(log);
//
//
//                 no test for admin db creds ??
//
//
//
//                // Now store the credentials in Firebase (only if login is successful)
//                Map<String, Object> user = new HashMap<>();
//                user.put("email", emailAddress);
//                user.put("password", password); // You may want to store a hashed password instead for security reasons
//
//                db.collection("users")
//                        .add(user)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Toast.makeText(Landing_Screen.this,"Data stored",Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(Landing_Screen.this,"ERROR: " + e.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
            else{
                Toast.makeText(this,"Invalid creds",Toast.LENGTH_SHORT).show();

            }
//

        });





    }
    private void openAdminInbox() {
        Intent i = new Intent(this, AdminInboxActivity.class);
        i.putExtra(newAdmin, "Administrator");
        startActivity(i);
    }
}