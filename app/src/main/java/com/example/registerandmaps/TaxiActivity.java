package com.example.registerandmaps;

import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
        import android.location.Address;
        import android.location.Geocoder;
        import android.os.Bundle;
        import android.text.InputType;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.SearchView;


        import com.example.registerandmaps.Database.TaxiLocationListener;
        import com.example.registerandmaps.Database.TaxiUserLocationListener;
        import com.example.registerandmaps.Models.StateHandler;
        import com.example.registerandmaps.Models.TaxiLocation;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.model.BitmapDescriptor;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.io.IOException;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.Random;

public class TaxiActivity extends AppCompatActivity implements OnMapReadyCallback, StateHandler<TaxiLocation>,GoogleMap.OnMarkerClickListener {
    private Map<Marker, TaxiLocation> markerLocationMap = new HashMap<>();// creates a key value system for storing location info
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private TaxiLocationListener taxiLocationListener;
    private TaxiUserLocationListener taxiUserLocationListener;
    private boolean isLocationAdded;
    String userId;
    private Button buttonRing;
    private Button buttonHitchhike;
    private Button buttonTaxi;
    private Button back;
    private LatLng pendingMarkerLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        taxiLocationListener = new TaxiLocationListener(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi);

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


        showMapScreen();

        FloatingActionButton fab = findViewById(R.id.taxi_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocationAdded){
                    // sets the value to 5
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference locationRef = databaseRef.child("Taxi").child(userId).child("status");
                    locationRef.setValue(5);
                }
                else {
                    showDestinationDialog();
                }
            }
        });
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

    private void showDestinationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Destination");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String destination = input.getText().toString();
                addLocationToTaxi(destination);
            }
        });

        // Set up the Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addLocationToTaxi(String destination) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            // Try to get location from the destination string
            addresses = geocoder.getFromLocationName(destination, 1);
            if (addresses == null || addresses.isEmpty()) {
                Log.e("Geocoder", "No location found for: " + destination);
                return;
            }
            Address location = addresses.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            pendingMarkerLatLng = new LatLng(lat, lng);

            // Create a new location object with the retrieved latitude and longitude
            TaxiLocation newLocation = new TaxiLocation(lat, lng, 0, "", userId, destination);

            // Add the location to the database and map
            DatabaseReference taxiRef = FirebaseDatabase.getInstance().getReference("Taxi");
            String locationId = userId;
            taxiRef.child(locationId).setValue(newLocation)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("locationAdder", "Added Location");
                        createTaxiUserLocationListener(locationId);
                        isLocationAdded = true;
                        addMarkerToMap(pendingMarkerLatLng); // Add marker to the map
                    })
                    .addOnFailureListener(e -> Log.d("locationAdder", e.getMessage()));

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Geocoder", "Geocoder failed", e);
        }
    }

    private void addMarkerToMap(LatLng latLng) {
        if (mMap != null) {
            String markerTitle = "Destination";
            mMap.addMarker(new MarkerOptions().position(latLng).title(markerTitle));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
    }



    private void createTaxiUserLocationListener(String locationId) {
        taxiUserLocationListener = new TaxiUserLocationListener(this,locationId);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        LatLng bilkent = new LatLng(39.875275, 32.748524);
        float zoomLevel = 13.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bilkent, zoomLevel));

        for (TaxiLocation location : taxiLocationListener.getLocations()) {
            if (location.getPickerUid().equals("") || location.getPickerUid() == null || location.getSharerUid().equals(userId) ){
                LatLng latLng = new LatLng(location.getLat(), location.getLng());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(location.getSharerUid()));
                markerLocationMap.put(marker, location);
            }
        }

        if (pendingMarkerLatLng != null) {
            // Add the pending marker
            addMarkerToMap(pendingMarkerLatLng);
            pendingMarkerLatLng = null; // Clear the pending location
        }

    }

    @Override
    public void locationUpdated() {
        if (mMap != null) {
            mMap.clear(); // Clear old markers
            markerLocationMap.clear(); // Clear old markers in hashmap

            for (TaxiLocation location : taxiLocationListener.getLocations()) {
                LatLng latLng = new LatLng(location.getLat(), location.getLng());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(location.getSharerUid()));
                markerLocationMap.put(marker, location);
            }
        }
    }


    @Override
    public void stateUpdated(int statusCode,TaxiLocation location) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.taxiContainer);
        if(statusCode == 1){
            showUserTaxiScreen(location);
        } else if (1 < statusCode && statusCode<5) {
            setStateForUserFragment(statusCode, location, currentFragment);
        } else if (statusCode == 0) {
            //TODO: STATUS 0 OLDUĞUNDA REFRESH ATIYOR O YÜZDEN REFRESH ATIYOR DİĞERİ AKTİFSE EKRAN DEĞİŞTİR
            showMapScreen();
        } else if (statusCode == 5){
            if (taxiUserLocationListener != null){
                taxiUserLocationListener.removeListener();
                removeLocation(userId);
            }
            showMapScreen();
        }
    }

    private void removeLocation(String locationId) {
        DatabaseReference taxiRef = FirebaseDatabase.getInstance().getReference("Taxi");
        taxiRef.child(locationId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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


    private void setStateForUserFragment(int statusCode, TaxiLocation location, Fragment currentFragment) {
        if (currentFragment instanceof UserTaxiFragment)
        {
            UserTaxiFragment fragment = (UserTaxiFragment) getSupportFragmentManager().findFragmentById(R.id.taxiContainer);
            if (fragment != null && fragment.isVisible()) {
                fragment.updateUIForState(statusCode, location);
            }

        }
    }

    public void showUserTaxiScreen(TaxiLocation location) {
        UserTaxiFragment userTaxiFragment = new UserTaxiFragment();

        Bundle args = new Bundle();
        args.putSerializable("taxiLocation", location);
        userTaxiFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.taxiContainer, userTaxiFragment)
                .addToBackStack(null)
                .commit();
    }


    public void showMapScreen() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.taxiContainer, mapFragment)
                .addToBackStack(null)
                .commit();

        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        TaxiLocation selectedLocation = markerLocationMap.get(marker);
        if (selectedLocation != null) {
            Intent intent = new Intent(this, CarTaxiScreen.class);
            intent.putExtra("taxiLocation", selectedLocation);
            startActivity(intent);
            finish();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        taxiLocationListener = new TaxiLocationListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (taxiLocationListener != null) {
            taxiLocationListener.removeListener();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }

}
