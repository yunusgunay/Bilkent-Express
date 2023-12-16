package com.example.registerandmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.registerandmaps.Database.UserDatabase;
import com.example.registerandmaps.Models.User;
import com.example.registerandmaps.Models.UserCallback;

public class LeaderBoard extends AppCompatActivity {

    TextView userWithMostPoints;
    TextView userWithLeastPoints;
    VideoView videoView;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        videoView = findViewById(R.id.videoView2);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.leader_background);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
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


        Typeface font = ResourcesCompat.getFont(this, R.font.coral_candy);
        userWithMostPoints = findViewById(R.id.mostPointsUser);
        userWithLeastPoints = findViewById(R.id.leastPointsUser);

        UserDatabase userDatabase = new UserDatabase();
        userDatabase.getUserWithMostPoints(new UserCallback() {
            @Override
            public void onCallback(User user) {
                userWithMostPoints.setText(" \n" +
                        "  LEADER  \n\n"
                        + user.getName()+ "\n"
                        + " points: " + user.getPoints() + " \n"
                        + " ");
                userWithMostPoints.setTypeface(font);
                userWithMostPoints.setTextSize(45);
                userWithMostPoints.setTextColor(Color.WHITE);
                userWithMostPoints.setBackgroundResource(R.drawable.leader_rounded_back);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        /*userDatabase.getUserWithLeastPoints(new UserCallback() {
            @Override
            public void onCallback(User user) {
                userWithLeastPoints.setText("User With Least Points is \n"+ user.getName()+"\n"+user.getPoints());
            }

            @Override
            public void onError(Exception e) {

            }
        });*/
    }
}