package com.example.registerandmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.registerandmaps.Database.HitchikeUserLocationListener;
import com.example.registerandmaps.Database.UserDatabase;
import com.example.registerandmaps.Models.HitchikerLocation;
import com.example.registerandmaps.Models.StateHandler;
import com.example.registerandmaps.Models.User;
import com.example.registerandmaps.Models.UserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CarHitchikerScreen extends AppCompatActivity implements StateHandler<HitchikerLocation> {

    private TextView carHitchikerEndCode;
    private TextView carHitchikerUserInfo;
    private EditText carHitchikerEndCodeEditText;
    private Button carHitchikerConfirmButton;
    private Button carHitchikerDeclineButton;
    private HitchikerLocation hitchikerLocation;
    private UserDatabase userDatabase = new UserDatabase();
    private HitchikeUserLocationListener hitchikeUserLocationListener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid = mAuth.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_hitchiker_screen);


        // Initialize views
        carHitchikerEndCode = findViewById(R.id.carHitchikerEndCode);
        carHitchikerUserInfo = findViewById(R.id.carHitchikerUserinfo);
        carHitchikerEndCodeEditText = findViewById(R.id.carHitcihkerEndCodeEditTExt);
        carHitchikerConfirmButton = findViewById(R.id.carHitchikerConfirm);
        carHitchikerDeclineButton = findViewById(R.id.CarHitchikerDecline);

        // get the location
        Intent intent = getIntent();
        hitchikerLocation = (HitchikerLocation) intent.getSerializableExtra("hitchikerLocation");

        userDatabase.getUser(hitchikerLocation.getSharerCode(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                carHitchikerUserInfo.setText(user.toString());
                carHitchikerConfirmButton.setText("Share ride +5");
                carHitchikerDeclineButton.setText("Dont Share ride");
            }
            @Override
            public void onError(Exception e) {}
        });

        // Set up listeners for buttons
        carHitchikerConfirmButton.setOnClickListener(v -> {
            if (hitchikerLocation.getStatus() == 0)
            {
                changeState(1);
            }
            else if (hitchikerLocation.getStatus() == 1)
            {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference locationRef = databaseRef.child("Hitchike").child(hitchikerLocation.getSharerCode()).child("status");
                locationRef.setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reserveLocation("");
                        endCarHitchikerScreen();
                    }
                });
            }
            else {
                endRide();
            }
        });

        carHitchikerDeclineButton.setOnClickListener(v -> {
            if (hitchikerLocation.getStatus() == 0)
            {
                reserveLocation("");
                endCarHitchikerScreen();
            }
            else
            {
                cancelRide();
            }
        });

        // set up the hitchikeUserLocationListener
        createHitchikeUserLocationListener(hitchikerLocation.getSharerCode());
        // reserve location
        reserveLocation(uid);

    }

    private void reserveLocation(String name) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference locationRef = databaseRef.child("Hitchike").child(hitchikerLocation.getSharerCode()).child("pickerCode");
        locationRef.setValue(name);
    }

    private void createHitchikeUserLocationListener(String locationId) {
        hitchikeUserLocationListener = new HitchikeUserLocationListener(this,locationId);
    }

    private void changeState(int state) {
        if (hitchikerLocation != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference locationRef = databaseRef.child("Hitchike").child(hitchikerLocation.getSharerCode()).child("status");
            locationRef.setValue(state);
        }
    }

    @Override
    public void stateUpdated(int stateCode, HitchikerLocation snapshot) {
        hitchikerLocation = snapshot;
        if (stateCode == 1) {
            updateUiStateCode1();
        } else if (stateCode == 5) {
            endCarHitchikerScreen();
        } else if (stateCode == 2) {
            updateUiStateCode2();
        }
    }

    private void endCarHitchikerScreen() {
        hitchikeUserLocationListener.removeListener();
        Intent intent = new Intent(this, HitchikerActivity.class);
        startActivity(intent);
        finish();
    }

    private void endRide() {
        if (String.valueOf(carHitchikerEndCodeEditText.getText()).trim().equals("31")){
            userDatabase.addPointsToUser(hitchikerLocation.getPickerCode(), 5, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    changeState(5);
                }
            });
        }
    }

    private void cancelRide() {
            userDatabase.addPointsToUser(hitchikerLocation.getPickerCode(), -5, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    changeState(5);
                }
            });
    }

    private void updateUiStateCode2() {
        userDatabase.getUser(hitchikerLocation.getPickerCode(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                carHitchikerUserInfo.setText("Sharing ride with \n"+user);
                carHitchikerEndCode.setVisibility(View.VISIBLE);
                carHitchikerDeclineButton.setVisibility(View.VISIBLE);
                carHitchikerEndCodeEditText.setVisibility(View.VISIBLE);
                carHitchikerConfirmButton.setText("End Ride");
                carHitchikerDeclineButton.setText("Cancel Ride -10 points");
            }
            @Override
            public void onError(Exception e){}
        });
    }

    private void updateUiStateCode1(){
        carHitchikerUserInfo.setText("waiting");
        carHitchikerConfirmButton.setText("cancel");
        carHitchikerDeclineButton.setVisibility(View.GONE);
    }

    @Override
    public void locationUpdated() {
        // bu niye var bilmiyorum bunları ayırmam lazım :(
    }
}
