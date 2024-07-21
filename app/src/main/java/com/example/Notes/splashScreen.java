package com.example.Notes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splashScreen extends AppCompatActivity {
VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        videoView=findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.spalshscreen;
        Uri videoUri = Uri.parse(videoPath);
        videoView.setVideoURI(videoUri);
        videoView.start();
        new Handler().postDelayed(() -> {
            // Start the next activity when the timer ends
            Intent mainIntent = new Intent(splashScreen.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }, 4000); // 4



        videoView.setOnCompletionListener(mp -> {
            // Start the next activity when the video ends
            Intent mainIntent = new Intent(splashScreen.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });




    }
}