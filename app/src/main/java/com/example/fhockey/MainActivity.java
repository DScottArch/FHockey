package com.example.fhockey;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.ActionBar;
import android.app.StatusBarManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowMetrics;

public class MainActivity extends AppCompatActivity {

    PongView pongView;

    @RequiresApi(api = Build.VERSION_CODES.M)
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

         //Get a Display object to access screen details
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);

        //final WindowMetrics metrics = getWindowManager().getCurrentWindowMetrics();
        // Load the resolution into a Point object

        //Rect rect = new Rect();
        //Rect statusBarHeight = new Rect();
//        Window window = getWindow();
//        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        //window.getDecorView().getHeight();
        //int height = window.getAttributes().height;
//        window.getDecorView().getClipBounds(statusBarHeight);
//        DisplayMetrics metrics = new DisplayMetrics();
//
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;

        int width = size.x;
        int height = size.y;

        // Initialize pongView and set it as the view
        pongView = new PongView(this, width, height);

        Log.d("Display", String.valueOf(width));
        Log.d("Display", String.valueOf(height));
        //Log.d("Display", String.valueOf(statusBarHeight.height()));

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