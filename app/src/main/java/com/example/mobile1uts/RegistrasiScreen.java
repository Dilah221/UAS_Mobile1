package com.example.mobile1uts;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrasiScreen extends AppCompatActivity {

    private EditText inputUser, inputPass, inputEmail;
    private Button regisButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_screen);

        inputUser = findViewById(R.id.edUserRegis);
        inputPass = findViewById(R.id.edPasRegis);
        inputEmail = findViewById(R.id.edEmailRegis);
        regisButton = findViewById(R.id.buLoginRegis);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        regisButton.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View view){
                final String user = inputUser.getText().toString().trim();
                final String pass = inputPass.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(user)){
                    inputUser.setError("User Name Tidak Boleh Kosong.");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    inputPass.setError("Password Tidak Boleh Kosong.");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    inputEmail.setError("email Tidak Boleh Kosong.");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    String userId  = mAuth.getCurrentUser().getUid();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("email", email);
                                    user.put("Password", pass);

                                    db.collection("users").document(userId).set(user)
                                            .addOnSuccessListener(aVoid -> {
                                               Toast.makeText(RegistrasiScreen.this, "Registrasi Berhasil",
                                                       Toast.LENGTH_SHORT).show();
                                               Intent intent = new Intent(RegistrasiScreen.this, loginScreen.class);
                                               startActivity(intent);
                                               finish();
                                            }).addOnFailureListener(e -> {
                                                Toast.makeText(RegistrasiScreen.this, "Error" + e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(RegistrasiScreen.this, "Registrasi Gagal" +
                                            task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.v("Errornya", task.getException().getMessage());
                                }
                            }
                        });
        }
    });
    }
}