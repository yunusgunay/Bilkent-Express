package com.example.registerandmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button buttonRing = findViewById(R.id.button_ring);
        Button buttonTaxi = findViewById(R.id.button_taxi);
        Button buttonHitchhiking = findViewById(R.id.button_hitchhiking);
        Button buttonProfile = findViewById(R.id.button_profile);
        Button buttonLeaderBoard = findViewById(R.id.button_leaderboard);

        buttonRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, RingActivity.class);
                startActivity(intent);
            }
        });

        buttonTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, TaxiActivity.class);
                startActivity(intent);
            }
        });

        buttonHitchhiking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement your hitchhiking action
                Intent intent = new Intent(MainScreen.this, HitchikerActivity.class);
                startActivity(intent);
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, ProfileScreen.class);
                startActivity(intent);
            }
        });

        buttonLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, LeaderBoard.class);
                startActivity(intent);
            }
        });
    }

}