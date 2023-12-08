package com.example.registerandmaps.Database;

import com.example.registerandmaps.Models.StateHandler;
import com.example.registerandmaps.Models.TaxiLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Listens to new added locations and adds them to a new array.
 */
public class TaxiLocationListener {
    private ArrayList<TaxiLocation> locations = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference taxiLocationRef;
    private StateHandler stateHandler;
    private ValueEventListener taxiUpdatedListener;

    public TaxiLocationListener(StateHandler stateHandler) {
        this.stateHandler = stateHandler;
        this.database = FirebaseDatabase.getInstance();
        initializeListener();
    }

    private void initializeListener() {
        taxiLocationRef = database.getReference("Taxi");
        taxiUpdatedListener = taxiLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the old locations
                locations.clear();

                // Iterate through all children and add them to the locations array
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    TaxiLocation location = locationSnapshot.getValue(TaxiLocation.class);
                    if (location != null) {
                        locations.add(location);
                    }
                }

                // Notify the state handler that new data is available
                stateHandler.locationUpdated(); // This should be modified to call the appropriate state
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log the error or handle the cancelled event
            }
        });
    }

    public void removeListener() {
        if (taxiUpdatedListener != null) {
            taxiLocationRef.removeEventListener(taxiUpdatedListener);
        }
    }

    // Getter method for locations
    public ArrayList<TaxiLocation> getLocations() {
        return locations;
    }
}
