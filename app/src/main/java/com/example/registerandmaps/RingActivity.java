package com.example.registerandmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.registerandmaps.Models.TimeGuesser;

public class RingActivity extends AppCompatActivity {
    TimeGuesser timeGuesser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }
}