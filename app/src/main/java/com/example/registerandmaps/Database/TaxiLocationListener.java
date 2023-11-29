package com.example.registerandmaps.Database;
import com.example.registerandmaps.Models.StateHandler;
import com.example.registerandmaps.Models.TaxiLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

/**
 * listens to new added locations and adds them to a new array
 */
public class TaxiLocationListener {
    ArrayList<TaxiLocation> locations = new ArrayList<TaxiLocation>();
    StateHandler stateHandler;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ListenerRegistration taxiUpdatedListener;

}
