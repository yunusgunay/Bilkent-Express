package com.example.registerandmaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView text ;
    Button addData;
    Button goBack;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = this.findViewById(R.id.textView2);
        mAuth = FirebaseAuth.getInstance();
        addData = this.findViewById(R.id.getData);
        goBack = this.findViewById(R.id.toMaps);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewData();
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMaps();
                //mAuth.sendPasswordResetEmail("barsyayc@gmail.com");
            }
        });

    }

    private void toMaps() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    private void addnewData() {
        Random random = new Random();

        // Base coordinates for the location (for example, your location)
        double baseLat = 34.0522;
        double baseLng = 32.7340074;

        // Generate random values to add/subtract from the base coordinates to simulate nearby locations
        double randomLat = baseLat + (random.nextDouble() * 0.01 - 0.005); // fluctuate within ~1km range
        double randomLng = baseLng + (random.nextDouble() * 0.01 - 0.005); // fluctuate within ~1km range

        // Create a map for the location data
        Map<String, Object> locationData = new HashMap<>();
        locationData.put("lat", randomLat);
        locationData.put("long", randomLng);

        // Add the location data to the "Location" collection with a random ID
        db.collection("Location").add(locationData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("addData", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addData", "Error adding document", e);
                    }
                });
    }

    private ListenerRegistration locationListener;

    @Override
    protected void onStart() {
        super.onStart();

        // Existing code to set email
        String email = mAuth.getCurrentUser().getEmail();
        text.setText(email);

        // Listen to changes in the Location collection
        locationListener = db.collection("Location").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("listenLocation", "Listen failed.", e);
                    return;
                }

                for (DocumentChange docChange : value.getDocumentChanges()) {
                    if (docChange.getType() == DocumentChange.Type.ADDED) {
                        Map<String, Object> newLocation = docChange.getDocument().getData();
                        String newText = text.getText() + "\n" + newLocation.toString();
                        text.setText(newText);
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationListener != null) {
            locationListener.remove();
        }
    }
}