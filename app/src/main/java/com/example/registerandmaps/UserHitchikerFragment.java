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

import com.example.registerandmaps.Database.HitchikerLocationListener;
import com.example.registerandmaps.Database.UserDatabase;
import com.example.registerandmaps.Models.HitchikerLocation;
import com.example.registerandmaps.Models.User;
import com.example.registerandmaps.Models.UserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class UserHitchikerFragment extends Fragment{
    private TextView endCodeUserHitchiker;
    private TextView userInfoUserHitchiker ;
    private EditText userHitchikerEndCodeEditText ;
    private Button userHitchikerConfirmButton;
    private Button userHitchikerDeclineButton;
    private UserDatabase userDatabase;
    private HitchikerLocation hitchikerLocation;


    // No need for factory method and parameters if not used
    public UserHitchikerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_hitchiker, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            hitchikerLocation = (HitchikerLocation) getArguments().getSerializable("hitchikerLocation");
        }

        userDatabase = new UserDatabase();
        // Initialize your views here using view.findViewById
        endCodeUserHitchiker = view.findViewById(R.id.endCodeUserHitchiker);
        userInfoUserHitchiker = view.findViewById(R.id.UserInfoUserHitchiker);
        userHitchikerEndCodeEditText = view.findViewById(R.id.userHitcihkerEndCodeEditTExt);
        userHitchikerConfirmButton = view.findViewById(R.id.userHitchikerConfirm);
        userHitchikerDeclineButton = view.findViewById(R.id.userHitchikerDecline);

        userHitchikerConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hitchikerLocation.getStatus() == 2){
                    endRide();
                }
                else {
                    changeState(2);
                }
            }
        });

        userHitchikerDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hitchikerLocation.getStatus() == 2){
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
        if (String.valueOf(userHitchikerEndCodeEditText.getText()).trim().equals("31")){
            userDatabase.addPointsToUser(hitchikerLocation.getPickerCode(), 5, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    changeState(5);
                }
            });
        }
    }

    private void cancelRide() {
            userDatabase.addPointsToUser(hitchikerLocation.getSharerCode(), -5, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    changeState(5);
                }
            });
    }

    private void changeState(int state) {
        if (hitchikerLocation != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference locationRef = databaseRef.child("Hitchike").child(hitchikerLocation.getSharerCode()).child("status");
            locationRef.setValue(state);
        }
    }


    public void updateUIForState(int statusCode, HitchikerLocation hitchikerLocation) {
        this.hitchikerLocation = hitchikerLocation;
        Log.d("status1", hitchikerLocation.getSharerCode());
        if (statusCode == 1){
            status1uiupdate();
        }
        if (statusCode == 2){
            userHitchikerEndCodeEditText.setVisibility(View.VISIBLE);
            status2uiupdate();
        }
    }

    private void status1uiupdate() {
        userDatabase.getUser(hitchikerLocation.getPickerCode(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                Log.d("status1",user.toString());
                userInfoUserHitchiker.setText(user+ "\n wants to pick you up");
            }
            @Override
            public void onError(Exception e){}
        });
    }

    private void status2uiupdate() {
        userDatabase.getUser(hitchikerLocation.getPickerCode(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                userInfoUserHitchiker.setText("Sharing ride with \n"+user);
                userHitchikerConfirmButton.setText("End Ride");
                userHitchikerDeclineButton.setText("Cancel Ride -10 points");
            }
            @Override
            public void onError(Exception e){}
        });
    }
}
