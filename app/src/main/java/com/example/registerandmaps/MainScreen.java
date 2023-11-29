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

        // Set up click listeners for each button
        buttonRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement your ring action
                showToast("Ring clicked");
            }
        });

        buttonTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement your taxi action
                showToast("Taxi clicked");
            }
        });

        buttonHitchhiking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement your hitchhiking action
                Intent intent = new Intent(MainScreen.this, MainActivity2.class);
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
    }

    private void showToast(String message) {
        Toast.makeText(MainScreen.this, message, Toast.LENGTH_SHORT).show();
    }
}