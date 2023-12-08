package com.example.registerandmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.registerandmaps.Database.HitchikerLocationListener;
import com.example.registerandmaps.Database.HitchikeUserLocationListener;
import com.example.registerandmaps.Models.HitchikerLocation;
import com.example.registerandmaps.Models.StateHandler;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HitchikerActivity extends AppCompatActivity implements OnMapReadyCallback, StateHandler<HitchikerLocation>,GoogleMap.OnMarkerClickListener {
    private Map<Marker, HitchikerLocation> markerLocationMap = new HashMap<>();// creates a key value system for storing location info
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private HitchikerLocationListener hitchikerLocationListener;
    private HitchikeUserLocationListener hitchikeUserLocationListener;
    private boolean isLocationAdded;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        hitchikerLocationListener = new HitchikerLocationListener(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        showMapScreen();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocationAdded){
                    // sets the value to 5
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference locationRef = databaseRef.child("Hitchike").child(userId).child("status");
                    locationRef.setValue(5);
                }
                else {
                    addLocationToHitchike();
                }
            }
        });
    }

    private void addLocationToHitchike() {
        // Create a new location object
        // Base coordinates for the location (for example, your location)
        double baseLat = 34.0522;
        double baseLng = 32.7340074;
        int status = 0;
        Random random = new Random();
        // Generate random values to add/subtract from the base coordinates to simulate nearby locations
        double randomLat = baseLat + (random.nextDouble() * 0.01 - 0.005); // fluctuate within ~1km range
        double randomLng = baseLng + (random.nextDouble() * 0.01 - 0.005); // fluctuate within ~1km range
        HitchikerLocation newLocation = new HitchikerLocation(randomLat,randomLng,status,"",userId);
        // Get the reference to the "Hitchike" node in the database
        DatabaseReference hitchikeRef = FirebaseDatabase.getInstance().getReference("Hitchike");
        String locationId = userId;
        Log.d("locationAdder","adding location");
        // Add the location to the "Hitchike" node using the locationId as the key
        hitchikeRef.child(locationId).setValue(newLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("locationAdder","added Location");
                        createHitchikeUserLocationListener(locationId);
                        isLocationAdded = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add the location
                        Log.d("locationAdder",e.getMessage());
                    }
                });

    }

    private void createHitchikeUserLocationListener(String locationId) {
        hitchikeUserLocationListener = new HitchikeUserLocationListener(this,locationId);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        for (HitchikerLocation location : hitchikerLocationListener.getLocations()) {
            if (location.getPickerUid().equals("") || location.getPickerUid() == null || location.getSharerUid().equals(userId) ){
                LatLng latLng = new LatLng(location.getLat(), location.getLng());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(location.getSharerUid()));
                markerLocationMap.put(marker, location);
            }
        }

    }

    @Override
    public void locationUpdated() {
        // This will be called when the location listener detects a change
        // Update the map with new markers
        if (mMap != null) {
            mMap.clear(); // Clear old markers
            markerLocationMap.clear();//clear old markers in hashmap
            for (HitchikerLocation location : hitchikerLocationListener.getLocations()) {
                if (location.getPickerUid().equals("") || location.getPickerUid() == null || location.getSharerUid().equals(userId)){
                    LatLng latLng = new LatLng(location.getLat(), location.getLng());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(location.getSharerUid()));
                    markerLocationMap.put(marker, location);
                }
            }
        }
    }

    @Override
    public void stateUpdated(int statusCode,HitchikerLocation location) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.hitchikeContainer);
        if(statusCode == 1){
            showUserHitchikerScreen(location);
        } else if (1 < statusCode && statusCode<5) {
            setStateForUserFragment(statusCode, location, currentFragment);
        } else if (statusCode == 0) {
            //TODO: STATUS 0 OLDUĞUNDA REFRESH ATIYOR O YÜZDEN REFRESH ATIYOR DİĞERİ AKTİFSE EKRAN DEĞİŞTİR
            showMapScreen();
        } else if (statusCode == 5){
            if (hitchikeUserLocationListener != null){
                hitchikeUserLocationListener.removeListener();
                removeLocation(userId);
            }
            showMapScreen();
        }
    }

    private void removeLocation(String locationId) {
        DatabaseReference hitchikeRef = FirebaseDatabase.getInstance().getReference("Hitchike");
        hitchikeRef.child(locationId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Successfully removed the location
                        Log.d("locationRemover", "Removed location with ID: " + locationId);
                        isLocationAdded = false;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove the location
                        Log.d("locationRemover", "Failed to remove location", e);
                    }
                });
    }


    private void setStateForUserFragment(int statusCode, HitchikerLocation location, Fragment currentFragment) {
        if (currentFragment instanceof UserHitchikerFragment)
        {
            UserHitchikerFragment fragment = (UserHitchikerFragment) getSupportFragmentManager().findFragmentById(R.id.hitchikeContainer);
            if (fragment != null && fragment.isVisible()) {
                fragment.updateUIForState(statusCode, location);
            }

        }
    }

    public void showUserHitchikerScreen(HitchikerLocation location) {
        UserHitchikerFragment userHitchikerFragment = new UserHitchikerFragment();

        Bundle args = new Bundle();
        args.putSerializable("hitchikerLocation", location);
        userHitchikerFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.hitchikeContainer, userHitchikerFragment)
                .addToBackStack(null)
                .commit();
    }


    public void showMapScreen() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.hitchikeContainer, mapFragment)
                .addToBackStack(null)
                .commit();

        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        HitchikerLocation selectedLocation = markerLocationMap.get(marker);
        if (selectedLocation != null) {
            Intent intent = new Intent(this, CarHitchikerScreen.class);
            intent.putExtra("hitchikerLocation", selectedLocation);
            startActivity(intent);
            finish();
        }
        return false;
    }

}
