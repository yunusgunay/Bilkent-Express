package com.example.registerandmaps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registerandmaps.Models.TimeGuesser;

public class RingActivity extends AppCompatActivity {

    private Spinner spinnerRingLine;
    private ListView listViewItinerary;
    private TimeGuesser timeGuesser = new TimeGuesser();
    private TextView statusTextView;
    private int selectedRingLine = 0;
    private Button buttonRing;
    private Button buttonHitchhike;
    private Button buttonTaxi;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        buttonRing = findViewById(R.id.buttonRing);
        buttonHitchhike = findViewById(R.id.buttonHitchhike);
        buttonTaxi = findViewById(R.id.buttonTaxi);

        buttonRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRingActivity();
            }
        });

        buttonHitchhike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHitchhikeActivity();
            }
        });

        buttonTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTaxiActivity();
            }
        });

        back = findViewById(R.id.btnBack);
        back.setBackgroundColor(Color.parseColor("#f9d423"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        statusTextView = findViewById(R.id.textViewStatus);
        spinnerRingLine = findViewById(R.id.spinnerRingLine);
        listViewItinerary = findViewById(R.id.listViewItinerary);
        statusTextView.setVisibility(View.INVISIBLE);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.ring_lines, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRingLine.setAdapter(spinnerAdapter);

        spinnerRingLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRingLine = position;
                updateListViewForSelectedRingLine();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        listViewItinerary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedRingLine == 0) {
                    statusTextView.setText(timeGuesser.dmt(position + 1));
                    statusTextView.setVisibility(View.VISIBLE);
                } else if (selectedRingLine == 1) {
                    statusTextView.setText(timeGuesser.dmb(position + 1));
                    statusTextView.setVisibility(View.VISIBLE);
                } else if (selectedRingLine == 2) {
                    statusTextView.setText(timeGuesser.tmd(position + 1));
                    statusTextView.setVisibility(View.VISIBLE);
                } else if (selectedRingLine == 3) {
                    statusTextView.setText(timeGuesser.bmd(position + 1));
                    statusTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateListViewForSelectedRingLine() {
        String[] stops;
        switch (selectedRingLine) {
            case 0:
                stops = new String[]{"East Campus", "Main Campus", "Nizamiye",  "ODTU", "Armada", "Tunus"};
                break;
            case 1:
                stops = new String[]{"East Campus", "Main Campus", "Nizamiye", "ODTU", "Armada", "Asti", "Bahcelievler"};
                break;
            case 2:
                stops = new String[]{"Tunus", "Armada", "ODTU", "Nizamiye", "Main Campus", "East Campus"};
                break;
            case 3:
                stops = new String[]{"Bahcelievler", "Asti",  "Armada", "ODTU", "Nizamiye", "Main Campus", "East Campus"};
                break;
            default:
                stops = new String[]{};
                break;
        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, stops);
        listViewItinerary.setAdapter(listAdapter);
    }

    private void openRingActivity() {
        Intent intent = new Intent(this, RingActivity.class);
        startActivity(intent);
    }

    private void openHitchhikeActivity() {
        Intent intent = new Intent(this, HitchikerActivity.class);
        startActivity(intent);
    }

    private void openTaxiActivity() {
        Intent intent = new Intent(this, TaxiActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }
}
