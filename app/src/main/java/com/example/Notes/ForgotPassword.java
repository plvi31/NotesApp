package com.example.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
private EditText mforgotpassword;
private Button mpasswordRecoveryBtn;
private TextView mgoBackBtn;
FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mforgotpassword=findViewById(R.id.forgotPassword);
        mpasswordRecoveryBtn=findViewById(R.id.passwordRecoverButton);
        mgoBackBtn=findViewById(R.id.goBackToLogin);
        firebaseAuth=FirebaseAuth.getInstance();


        mgoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordRecoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mforgotpassword.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(ForgotPassword.this, "Enter your mail first", Toast.LENGTH_SHORT).show();
                }else{
                    //we have to send pass recovering mail
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this, "Mail sent,You can recover your password", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                            }else{
                                Toast.makeText(ForgotPassword.this, "Email is wrong or Account does not exist.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });
    }
}