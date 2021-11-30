package com.example.fhockey;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    PongView pongView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);


//        View decorView = getWindow().getDecorView();
//
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//
//        decorView.setSystemUiVisibility(uiOptions);
//
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        Log.d("x", String.valueOf(size.x));
        Log.d("y", String.valueOf(size.y));


        // Initialize pongView and set it as the view
        pongView = new PongView(this, size.x, size.y);
        setContentView(pongView);
    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the pongView resume method to execute
        pongView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the pongView pause method to execute
        pongView.pause();
    }
}