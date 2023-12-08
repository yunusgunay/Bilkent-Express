package com.example.registerandmaps;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.registerandmaps.Database.UserDatabase;
import com.example.registerandmaps.Models.TaxiLocation;
import com.example.registerandmaps.Models.User;
import com.example.registerandmaps.Models.UserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserTaxiFragment extends Fragment{
    private TextView endCodeUserTaxi;
    private TextView userInfoUserTaxi;
    private EditText userTaxiEndCodeEditText ;
    private Button userTaxiConfirmButton;
    private Button userTaxiDeclineButton;
    private UserDatabase userDatabase;
    private TaxiLocation taxiLocation;


    // No need for factory method and parameters if not used
    public UserTaxiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_taxi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            taxiLocation = (TaxiLocation) getArguments().getSerializable("taxiLocation");
        }

        userDatabase = new UserDatabase();
        // Initialize your views here using view.findViewById
        endCodeUserTaxi = view.findViewById(R.id.endCodeUserTaxi);
        userInfoUserTaxi = view.findViewById(R.id.UserInfoUserTaxi);
        userTaxiEndCodeEditText = view.findViewById(R.id.userTaxiEndCodeEditTExt);
        userTaxiConfirmButton = view.findViewById(R.id.userTaxiConfirm);
        userTaxiDeclineButton = view.findViewById(R.id.userTaxiDecline);

        userTaxiConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taxiLocation.getStatus() == 2){
                    endRide();
                }
                else {
                    changeState(2);
                }
            }
        });

        userTaxiDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taxiLocation.getStatus() == 2){
                    cancelRide();
                }
                else {
                    changeState(5);
                }
            }
        });

        status1uiupdate();
    }

    private void endRide() {
            userDatabase.addPointsToUser(taxiLocation.getPickerUid(), 5, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    changeState(5);
                }
            });
    }

    private void cancelRide() {
        userDatabase.addPointsToUser(taxiLocation.getSharerUid(), -5, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                changeState(5);
            }
        });
    }

    private void changeState(int state) {
        if (taxiLocation != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference locationRef = databaseRef.child("Taxi").child(taxiLocation.getSharerUid()).child("status");
            locationRef.setValue(state);
        }
    }


    public void updateUIForState(int statusCode, TaxiLocation taxiLocation) {
        this.taxiLocation = taxiLocation;
        Log.d("status1", taxiLocation.getSharerUid());
        if (statusCode == 1){
            status1uiupdate();
        }
        if (statusCode == 2){
            userTaxiEndCodeEditText.setVisibility(View.VISIBLE);
            status2uiupdate();
        }
    }

    private void status1uiupdate() {
        userDatabase.getUser(taxiLocation.getPickerUid(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                Log.d("status1",user.toString());
                userInfoUserTaxi.setText(user+ "\n wants to pick you up");
            }
            @Override
            public void onError(Exception e){}
        });
    }

    private void status2uiupdate() {
        userDatabase.getUser(taxiLocation.getPickerUid(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                userInfoUserTaxi.setText("Sharing ride with \n"+user);
                userTaxiConfirmButton.setText("End Ride");
                userTaxiDeclineButton.setText("Cancel Ride -10 points");
            }
            @Override
            public void onError(Exception e){}
        });
    }
}
