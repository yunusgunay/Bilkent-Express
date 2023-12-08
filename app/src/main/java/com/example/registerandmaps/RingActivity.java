package com.example.registerandmaps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        statusTextView = findViewById(R.id.textViewStatus);
        spinnerRingLine = findViewById(R.id.spinnerRingLine);
        listViewItinerary = findViewById(R.id.listViewItinerary);

        statusTextView.setVisibility(View.INVISIBLE);

        // Set up the spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.ring_lines, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRingLine.setAdapter(spinnerAdapter);

        spinnerRingLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRingLine = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Define the list of itinerary stops
        String[] stops = {"East Campus", "Main Campus", "Nizamiye", "ODTU", "Armada", "ASTI", "Bahcelievler"};

        // Set up the ListView with an ArrayAdapter
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, stops);
        listViewItinerary.setAdapter(listAdapter);

        // Set up the click listener for ListView items
        listViewItinerary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedRingLine == 1){
                    statusTextView.setText(timeGuesser.dmb(position+1)+ position);
                    statusTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}

