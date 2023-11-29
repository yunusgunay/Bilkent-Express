package com.example.registerandmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.registerandmaps.Database.UserDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email;
    EditText password;
    TextView error;
    Button login;
    Button goToRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mAuth = FirebaseAuth.getInstance(); // Initialize mAuth
        email = this.findViewById(R.id.email);
        password = this.findViewById(R.id.password);
        error = this.findViewById(R.id.errTextLogin);
        login = this.findViewById(R.id.loginButton);
        goToRegister =this.findViewById(R.id.logreg);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("emailSignup","pressed");
                login();
            }
        });

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    private void goToRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    private void login() {
        String enteredEmail= String.valueOf(email.getText());
        String enteredPassword = String.valueOf(password.getText());
        mAuth.signInWithEmailAndPassword(enteredEmail,enteredPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("emailSignup", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("emailSignup", "signInWithEmail:failure", task.getException());
                            error.setText(task.getException().getMessage());
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void updateUI() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }


}