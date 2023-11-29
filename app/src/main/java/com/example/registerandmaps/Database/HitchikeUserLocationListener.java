package com.example.registerandmaps.Database;

import com.example.registerandmaps.Models.StateHandler;
import com.google.firebase.database.FirebaseDatabase;

import com.example.registerandmaps.Models.HitchikerLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HitchikeUserLocationListener {
    private StateHandler stateHandler;
    private FirebaseDatabase database;
    private DatabaseReference userStateRef;
    private ValueEventListener userStateListener;
    private String userId; // The UID of the user

    public HitchikeUserLocationListener(StateHandler stateHandler, String userId) {
        this.stateHandler = stateHandler;
        this.userId = userId;
        this.database = FirebaseDatabase.getInstance();
        initializeListener();
    }

    private void initializeListener() {
        // and each child is keyed by the user's UID
        userStateRef = database.getReference("Hitchike").child(userId);
        userStateListener = userStateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HitchikerLocation location = dataSnapshot.getValue(HitchikerLocation.class);
                    if (location != null) {
                        stateHandler.stateUpdated(location.getStatus(),location); // Notify the state handler with the location object
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log the error or handle the cancelled event
            }
        });
    }

    public void removeListener() {
        if (userStateListener != null) {
            userStateRef.removeEventListener(userStateListener);
        }
    }
}
