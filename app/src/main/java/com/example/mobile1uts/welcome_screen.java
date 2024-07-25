package com.example.mobile1uts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class welcome_screen extends AppCompatActivity {
    private Button btnlogin;
    private Button btnsignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        btnlogin = findViewById(R.id.BtnLogIn);
        btnsignup = findViewById(R.id.BtnSignUp);

        mAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_screen.this, loginScreen.class);
                startActivity(intent);
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_screen.this, RegistrasiScreen.class);
                startActivity(intent);
            }
        });
        cekUser();

    }

    private void cekUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(welcome_screen.this, listScreen.class));
            finish();
        }
    }
}