package com.example.registerandmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.registerandmaps.Database.UserDatabase;
import com.example.registerandmaps.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import  com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button register;
    EditText email;
    EditText password;
    EditText name;
    EditText phoneNumber;
    UserDatabase userDatabase;
    TextView errorText;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth and database
        mAuth = FirebaseAuth.getInstance();
        userDatabase = new UserDatabase();
        //get ui components
        register = this.findViewById(R.id.register_button);
        email = this.findViewById(R.id.editTextTextEmailAddress);
        password = this.findViewById(R.id.editTextTextPassword);
        name = this.findViewById(R.id.registerEditTextName);
        phoneNumber = this.findViewById(R.id.registerEditTextEnterNumber);

        errorText = this.findViewById(R.id.textView3);
        //add listeners
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            register_via_firebase();
            }
        });

        back = findViewById(R.id.btnBack);
        back.setBackgroundColor(Color.parseColor("#f9d423"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackToLogin();
            }
        });
    }

    private void register_via_firebase() {
        String email_text = String.valueOf(email.getText());
        String password_text = String.valueOf(password.getText());
        String name_text = String.valueOf(name.getText());
        String phoneNumber_text = String.valueOf(phoneNumber.getText());
        mAuth.createUserWithEmailAndPassword(email_text, password_text)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            User newUser = new User(name_text,phoneNumber_text,email_text,0,"",user.getUid());
                            userDatabase.addUser(newUser, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    updateUI();
                                }
                            }, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    errorText.setText("something went wrong");
                                    errorText.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            errorText.setText(task.getException().getMessage());
                            errorText.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void updateUI() {
        // Java code inside Register Activity
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }

    public void onBackToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}