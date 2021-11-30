package com.example.fhockey;

import android.graphics.RectF;
import android.util.Log;

public class Goalie {
    // RectF is an object that holds four coordinates - just what we need
    private RectF mRect;

    // How long and high our mBat will be
    private float mLength;
    private float mWidth;

    // X is the far left of the rectangle which forms our mBat
    private float mXCoord;

    // Y is the top coordinate
    private float mYCoord;

    // This will hold the pixels per second speed that
    // the mBat will move
    private float mBatSpeed;

    // Which ways can the mBat move
    public final int STOPPED = 0;
    public final int UP = 1;
    public final int DOWN = 2;

    // Is the mBat moving and in which direction
    private int mBatMoving = STOPPED;

    // The screen length and width in pixels
    private int mScreenX;
    private int mScreenY;

    // This is the constructor method
// When we create an object from this class we will pass
// in the screen width and mHeight
    public Goalie(int x, int y){

        mScreenX = x;
        mScreenY = y;

        Log.d("yCoor", String.valueOf(mScreenY));
        Log.d("xCoor", String.valueOf(mScreenX));


        // 1/8 screen height wide
        mLength = mScreenY / 7;

        // 1/25 screen mWidth high
        mWidth = mScreenX / 50;

        // Start mBat in roughly the screen centre
        mYCoord = mScreenY / 2;

        //mYCoord = mWidth;


        mRect = new RectF(mXCoord, mYCoord, mXCoord +  + mWidth, mYCoord + mLength);

        // How fast is the mBat in pixels per second
        mBatSpeed = mScreenX;
        // Cover entire screen in 1 second
    }

    public RectF getRect(){
        return mRect;

        //return new RectF(0, 100, 100,0);
    }

    // This method will be used to change/set if the mBat is going
// left, right or nowhere

    public void setMovementState(int state){
        mBatMoving = state;
        //Log.d("batstate", String.valueOf(mBatMoving));
    }

    // This update method will be called from update in PongView
// It determines if the Bat needs to move and changes the coordinates
// contained in mRect if necessary
    public void update(long fps){

        if(mBatMoving == UP){
            mYCoord = mYCoord - mBatSpeed / fps;
        }

        if(mBatMoving == DOWN){
            mYCoord = mYCoord + mBatSpeed / fps;
        }

        // Make sure it's not leaving screen
        if(mRect.top < 0){
            mYCoord = 0;
        }
        if(mRect.bottom > mScreenY){
            mYCoord = mScreenY - (mRect.bottom - mRect.bottom);
        }

        // Update the Bat graphics
        mRect.top = mYCoord;
        mRect.bottom = mYCoord + mLength;
    }
}
