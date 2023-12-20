package com.example.registerandmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.registerandmaps.Database.TaxiUserLocationListener;
import com.example.registerandmaps.Database.UserDatabase;
import com.example.registerandmaps.Models.StateHandler;
import com.example.registerandmaps.Models.TaxiLocation;
import com.example.registerandmaps.Models.User;
import com.example.registerandmaps.Models.UserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CarTaxiScreen extends AppCompatActivity implements StateHandler<TaxiLocation> {

    private TextView carTaxiEndCode;
    private TextView carTaxiUserInfo;
    private EditText carTaxiEndCodeEditText;
    private Button carTaxiConfirmButton;
    private Button carTaxiDeclineButton;
    private TaxiLocation taxiLocation;
    private UserDatabase userDatabase = new UserDatabase();
    private TaxiUserLocationListener taxiUserLocationListener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid = mAuth.getUid();
    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_taxi);

        videoView = findViewById(R.id.videoView5);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.flame_video);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });


        // Initialize views
        carTaxiEndCode = findViewById(R.id.carTaxiEndCode);
        carTaxiUserInfo = findViewById(R.id.carTaxiUserinfo);
        carTaxiEndCodeEditText = findViewById(R.id.carTaxiEndCodeEditTExt);
        carTaxiConfirmButton = findViewById(R.id.carTaxiConfirm);
        carTaxiDeclineButton = findViewById(R.id.CarTaxiDecline);

        // get the location
        Intent intent = getIntent();
        taxiLocation = (TaxiLocation) intent.getSerializableExtra("taxiLocation");

        Typeface font = ResourcesCompat.getFont(this, R.font.coral_candy);
        userDatabase.getUser(taxiLocation.getSharerUid(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                carTaxiUserInfo.setText(user.toString()+"\n destination is: \n" + taxiLocation.getLocation());
                carTaxiUserInfo.setTypeface(font);
                carTaxiUserInfo.setTextSize(20);
                carTaxiUserInfo.setTextColor(Color.WHITE);
                int backColor = Color.parseColor("#CCFFFFFF");
                carTaxiUserInfo.setBackgroundColor(backColor);

                carTaxiConfirmButton.setText(" Contact User (+5) ");
                carTaxiDeclineButton.setText(" Don't Share Ride ");
            }
            @Override
            public void onError(Exception e) {}
        });

        // Set up listeners for buttons
        carTaxiConfirmButton.setOnClickListener(v -> {
            if (taxiLocation.getStatus() == 0)
            {
                changeState(1);
            }
            else if (taxiLocation.getStatus() == 1)
            {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference locationRef = databaseRef.child("Taxi").child(taxiLocation.getSharerUid()).child("status");
                locationRef.setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reserveLocation("");
                        endCarTaxiScreen();
                    }
                });
            }
            else {
                endRide();
            }
        });

        carTaxiDeclineButton.setOnClickListener(v -> {
            if (taxiLocation.getStatus() == 0)
            {
                reserveLocation("");
                endCarTaxiScreen();
            }
            else
            {
                cancelRide();
            }
        });

        createTaxiUserLocationListener(taxiLocation.getSharerUid());
        // reserve location
        reserveLocation(uid);

    }

    private void reserveLocation(String name) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference locationRef = databaseRef.child("Taxi").child(taxiLocation.getSharerUid()).child("pickerUid");
        locationRef.setValue(name);
    }

    private void createTaxiUserLocationListener(String locationId) {
        taxiUserLocationListener = new TaxiUserLocationListener(this,locationId);
    }

    private void changeState(int state) {
        if (taxiLocation != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference locationRef = databaseRef.child("Taxi").child(taxiLocation.getSharerUid()).child("status");
            locationRef.setValue(state);
        }
    }

    @Override
    public void stateUpdated(int stateCode, TaxiLocation snapshot) {
        taxiLocation = snapshot;
        if (stateCode == 1) {
            updateUiStateCode1();
        } else if (stateCode == 5) {
            endCarTaxiScreen();
        } else if (stateCode == 2) {
            updateUiStateCode2();
        }
    }

    private void endCarTaxiScreen() {
        taxiUserLocationListener.removeListener();
        Intent intent = new Intent(this, TaxiActivity.class);
        startActivity(intent);
        finish();
    }

    private void endRide() {
        changeState(5);
    }

    private void cancelRide() {
        changeState(5);
    }

    private void updateUiStateCode2() {
        userDatabase.getUser(taxiLocation.getSharerUid(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                carTaxiUserInfo.setText("Sharing Ride With: \n"+user);
                carTaxiDeclineButton.setVisibility(View.VISIBLE);
                carTaxiEndCodeEditText.setVisibility(View.VISIBLE);
                carTaxiConfirmButton.setText(" End Ride ");
                carTaxiDeclineButton.setText(" Cancel Ride (-10) ");
            }
            @Override
            public void onError(Exception e){}
        });
    }

    private void updateUiStateCode1(){
        carTaxiUserInfo.setText(" •ᴗ• WAITING... ");
        carTaxiUserInfo.setVisibility(View.VISIBLE);
        carTaxiUserInfo.setTypeface(ResourcesCompat.getFont(this, R.font.coral_candy));
        carTaxiUserInfo.setTextSize(10);
        carTaxiUserInfo.setTextColor(Color.BLACK);

        carTaxiConfirmButton.setText(" Cancel ");
        carTaxiDeclineButton.setVisibility(View.GONE);

        videoView = findViewById(R.id.videoView5);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.waiting_screen);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }

    @Override
    public void locationUpdated() {
        // bu niye var bilmiyorum bunları ayırmam lazım :(
    }
}
