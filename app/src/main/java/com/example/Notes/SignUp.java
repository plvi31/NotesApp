package com.example.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
private EditText msignUpEmail,msignUpPass;
private Button msignUp;
private TextView mgotoLogin;

private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        msignUpEmail=findViewById(R.id.signupEmail);
        msignUpPass=findViewById(R.id.signupPass);
        msignUp = findViewById(R.id.Signup_Btn);
        mgotoLogin=findViewById(R.id.goBackToLogin);
        firebaseAuth=FirebaseAuth.getInstance();

        mgotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });

        msignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = msignUpEmail.getText().toString().trim();
                String password = msignUpPass.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignUp.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }else if(password.length()<7){
                    Toast.makeText(SignUp.this, "Password length should be greater than 7.", Toast.LENGTH_SHORT).show();
                }else{
                    //registered the user
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }else{
                                Toast.makeText(SignUp.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignUp.this, "verify email and then Login again.", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                }
            });
        }else{
            Toast.makeText(SignUp.this, "Failed to send verification Email ", Toast.LENGTH_SHORT).show();
            sendEmailVerification();
        }
    }
}