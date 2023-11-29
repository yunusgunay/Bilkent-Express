package com.example.registerandmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.registerandmaps.Database.UserDatabase;
import com.example.registerandmaps.Models.User;
import com.example.registerandmaps.Models.UserCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileScreen extends AppCompatActivity {

    private TextView profileInfoView;
    private EditText editTextEmail, editTextPhone, editTextExtraInfo, editTextChangeName;
    private Button resetPasswordButton, updateUserInfoButton;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private User currentUser;
    private UserDatabase userDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        // Initialize components
        profileInfoView = findViewById(R.id.profileProfileİnfoView);
        editTextEmail = findViewById(R.id.profileEditTextEmail);
        editTextPhone = findViewById(R.id.profileEditTextPhone);
        editTextExtraInfo = findViewById(R.id.profileEditTextExtraInfo);
        editTextChangeName = findViewById(R.id.profileEditTextChangeName);
        resetPasswordButton = findViewById(R.id.profileResetPassword);
        updateUserInfoButton = findViewById(R.id.profileUpdateUserInfoButton);

        // get user info;
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userDatabase = new UserDatabase();
        userDatabase.getUser(mAuth.getUid(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                currentUser = user;
                profileInfoView.setText(currentUser.toString());
            }
            @Override
            public void onError(Exception e) {}
        });

        resetPasswordButton.setOnClickListener(v -> {
            mAuth.sendPasswordResetEmail(currentUser.getEmail());
        });

        updateUserInfoButton.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String phone = editTextPhone.getText().toString();
            String extraInfo = editTextExtraInfo.getText().toString();
            String name = editTextChangeName.getText().toString();
            firebaseUser.updateEmail(email);
            User updated_user = new User(name,phone,email,currentUser.getPoints(),extraInfo,currentUser.getUid());
            userDatabase.addUser(updated_user, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    profileInfoView.setText(updated_user.toString());
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        });
    }

}
