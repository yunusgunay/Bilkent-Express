package com.example.registerandmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

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
    VideoView videoView;
    Button back;


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


        videoView = findViewById(R.id.videoView3);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.profile_background);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });


        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Typeface font = ResourcesCompat.getFont(this, R.font.coral_candy);
        // get user info;
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userDatabase = new UserDatabase();
        userDatabase.getUser(mAuth.getUid(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                Drawable icon = getResources().getDrawable(R.drawable.baseline_person_24);
                profileInfoView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                profileInfoView.setCompoundDrawablePadding(8);

                currentUser = user;
                profileInfoView.setText(currentUser.toString());
                profileInfoView.setTypeface(font);
                profileInfoView.setTextSize(20);
                profileInfoView.setTextColor(Color.BLACK);
                profileInfoView.setBackgroundResource(R.drawable.profile_background);
            }
            @Override
            public void onError(Exception e) {}
        });


        resetPasswordButton.setOnClickListener(v -> {
            mAuth.sendPasswordResetEmail(currentUser.getEmail());
        });


        updateUserInfoButton.setOnClickListener(v -> {
            String email = currentUser.getEmail().toString();
            String phone = currentUser.getNumber().toString();
            String extraInfo = currentUser.getInfo().toString();
            String name = currentUser.getName().toString();

            Editable emaileditable = editTextEmail.getText();
            if(emaileditable != null && !emaileditable.toString().isEmpty()) {
                email = editTextEmail.getText().toString();
            }

            Editable phoneEditable = editTextPhone.getText();
            if(phoneEditable != null && !phoneEditable.toString().isEmpty() ) {
                phone = editTextPhone.getText().toString();
            }

            Editable infoEditable = editTextExtraInfo.getText();
            if(infoEditable != null && !infoEditable.toString().isEmpty()) {
                extraInfo = editTextExtraInfo.getText().toString();
            }

            Editable nameEditable = editTextChangeName.getText();
            if(nameEditable != null && !nameEditable.toString().isEmpty()) {
                name = editTextChangeName.getText().toString();
            }

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
