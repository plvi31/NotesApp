package com.example.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText mLoginEmail, mLoginPassword;
    private Button mLogin, mGotoSignUp;
    private TextView mGotoForgotPassword;
    ProgressBar mProgressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adjust padding based on system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            startNotesActivity();
        }

        // Initialize views
        mLoginEmail = findViewById(R.id.loginEmail);
        mLoginPassword = findViewById(R.id.loginPass);
        mLogin = findViewById(R.id.login_Btn);
        mGotoSignUp = findViewById(R.id.login_pg_signUp_Btn);
        mGotoForgotPassword = findViewById(R.id.gotoforgotpass);
        mProgressBar = findViewById(R.id.progressBarofmainactivity);

        // Set onClick listeners
        mGotoSignUp.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignUp.class)));

        mGotoForgotPassword.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ForgotPassword.class)));

        mLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = mLoginEmail.getText().toString().trim();
        String password = mLoginPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }else {

            mProgressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                startNotesActivity();
                            } else {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Something's wrong ! Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void startNotesActivity() {
        startActivity(new Intent(MainActivity.this, NotesActivity.class));
        finish();
    }
}
