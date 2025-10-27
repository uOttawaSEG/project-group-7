package com.example.otams7.classes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otams7.R;

public class LogoutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView newTV = findViewById(R.id.txtWelcome);
        Button logOut = findViewById(R.id.btnLogout);

        String newUser = getIntent().getStringExtra(Landing_Screen.newLand);
        if(newUser == null || newUser.isEmpty()) {
            newUser = getIntent().getStringExtra(Landing_Screen.newLand);
        }

        if(newUser == null || newUser.isEmpty()) {
            newUser = "User";
        }

        newTV.setText("Welcome! You are logged in as " + newUser);

        logOut.setOnClickListener(v -> {
            Intent newLogOut = new Intent(this, Landing_Screen.class);
            newLogOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(newLogOut);
        });
    }
}