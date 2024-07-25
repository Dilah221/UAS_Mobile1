package com.example.mobile1uts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class loginScreen extends AppCompatActivity {
    private Button loginButton;
    private GoogleSignInClient mGoogleSignInCllient;
    private static final int RC_SIGN_IN = 9001;
    private static  final String TAG = "LogActivity";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        loginButton = findViewById(R.id.buLogin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_token))
                .requestEmail()
                .build();

        mGoogleSignInCllient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "SignWithCredential : Success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(loginScreen.this, "Authentication successful",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginScreen.this, listScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.w(TAG, "SignWithCredential : failure", task.getException());
                    Toast.makeText(loginScreen.this, "Authentication failure",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void signIn(){
        Intent singIntent = mGoogleSignInCllient.getSignInIntent();
        startActivityForResult(singIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }catch (ApiException e){
                Log.w(TAG, "Google Sign In Failed", e);
                Toast.makeText(this, "Google Sign In Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }
}