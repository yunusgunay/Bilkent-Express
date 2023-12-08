package com.example.registerandmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.registerandmaps.Database.UserDatabase;
import com.example.registerandmaps.Models.User;
import com.example.registerandmaps.Models.UserCallback;

public class LeaderBoard extends AppCompatActivity {

    TextView userWithMostPoints;
    TextView userWithLeastPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        userWithMostPoints = findViewById(R.id.mostPointsUser);
        userWithLeastPoints = findViewById(R.id.leastPointsUser);
        UserDatabase userDatabase = new UserDatabase();
        userDatabase.getUserWithMostPoints(new UserCallback() {
            @Override
            public void onCallback(User user) {
                userWithMostPoints.setText("User With Most Points is \n"+ user.getName()+"\n"+user.getPoints());
            }

            @Override
            public void onError(Exception e) {

            }
        });

        userDatabase.getUserWithLeastPoints(new UserCallback() {
            @Override
            public void onCallback(User user) {
                userWithLeastPoints.setText("User With Least Points is \n"+ user.getName()+"\n"+user.getPoints());
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}