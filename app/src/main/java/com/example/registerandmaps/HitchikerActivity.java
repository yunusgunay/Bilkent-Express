package com.example.registerandmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.registerandmaps.Database.HitchikerLocationListener;
import com.example.registerandmaps.Database.HitchikeUserLocationListener;
import com.example.registerandmaps.Models.HitchikerLocation;
import com.example.registerandmaps.Models.StateHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HitchikerActivity extends AppCompatActivity implements OnMapReadyCallback, StateHandler<HitchikerLocation>,GoogleMap.OnMarkerClickListener {
    private static final int YOUR_PERMISSIONS_REQUEST_CODE = 1001;
    private Map<Marker, HitchikerLocation> markerLocationMap = new HashMap<>();// creates a key value system for storing location info
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private HitchikerLocationListener hitchikerLocationListener;
    private HitchikeUserLocationListener hitchikeUserLocationListener;
    private boolean isLocationAdded;
    String userId;
    private Button buttonRing;
    private Button buttonHitchhike;
    private Button buttonTaxi;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        hitchikerLocationListener = new HitchikerLocationListener(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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
                    addLocationToHitchikeWithLocation();
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

    private void addLocationToHitchike() {
        double baseLat = 39.875275;
        double baseLng = 32.748524;
        int status = 0;
        Random random = new Random();

        // Generate random values to add/subtract from the base coordinates to simulate nearby locations
        double randomLat = baseLat + (random.nextDouble() * 0.01 - 0.005); // fluctuate within ~1km range
        double randomLng = baseLng + (random.nextDouble() * 0.01 - 0.005); // fluctuate within ~1km range
        HitchikerLocation newLocation = new HitchikerLocation(randomLat,randomLng,status,"",userId);

        DatabaseReference hitchikeRef = FirebaseDatabase.getInstance().getReference("Hitchike");
        String locationId = userId;


        hitchikeRef.child(locationId).setValue(newLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("locationAdder","added Location");
                createHitchikeUserLocationListener(locationId);
                isLocationAdded = true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                        // Failed to add the location
                    Log.d("locationAdder",e.getMessage());
                }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == YOUR_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                addLocationToHitchikeWithLocation();
            } else {
                // Permission was denied
                // Handle the case where the user denies the permission
            }
        }
    }

    private void addLocationToHitchikeWithLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if GPS permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, YOUR_PERMISSIONS_REQUEST_CODE);
            return;
        }

        // Request a single location update
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                int status = 0;

                HitchikerLocation newLocation = new HitchikerLocation(latitude, longitude, status, "", userId);

                DatabaseReference hitchikeRef = FirebaseDatabase.getInstance().getReference("Hitchike");
                String locationId = userId;
                Log.d("locationAdder", "adding location");

                hitchikeRef.child(locationId).setValue(newLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("locationAdder", "added Location");
                        createHitchikeUserLocationListener(locationId);
                        isLocationAdded = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("locationAdder", e.getMessage());
                    }
                });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        }, null);
    }

    private void createHitchikeUserLocationListener(String locationId) {
        hitchikeUserLocationListener = new HitchikeUserLocationListener(this,locationId);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }

}
