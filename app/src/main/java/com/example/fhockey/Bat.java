package com.example.fhockey;

import android.graphics.RectF;
import android.util.Log;

public class Bat {
    // RectF is an object that holds four coordinates - just what we need
    private RectF mRect;

    // How long and high our mBat will be
    private float mLength;
    private float mLengthHalf;
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

    private int iceWidth;
    private int iceHeight;

    RectF ice;

    // This is the constructor method
// When we create an object from this class we will pass
// in the screen width and mHeight
    public Bat(RectF ice, int player){

        Log.d("yCoor", String.valueOf(mScreenY));
        Log.d("xCoor", String.valueOf(mScreenX));

        this.ice = ice;

        // 1/8 screen height wide
        mLength = ice.height() / 5;

        // 1/25 screen mWidth high
        mWidth = ice.width() / 75;

        // Start mBat in roughly the screen centre
        mYCoord = ((ice.bottom - ice.top) / 2) + ice.top;

        Log.d("mYCoord", String.valueOf(mYCoord));
        Log.d("iceTop", String.valueOf(ice.top));
        Log.d("iceBottom", String.valueOf(ice.bottom));

        mXCoord = (ice.right - ice.bottom) + ice.right;

        mLengthHalf = mLength / 2;

        if(player == 1) {
            mRect = new RectF((int)(ice.left), mYCoord - mLengthHalf, (int)(ice.left) + mWidth, mYCoord + mLengthHalf);
        }
        else
        {
            mRect = new RectF((int)(ice.right) - mWidth, mYCoord - mLengthHalf, (int)(ice.right), mYCoord + mLengthHalf);
        }

        //TODO: change mBatSpeed

        // How fast is the mBat in pixels per second
        mBatSpeed = ice.width();
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
        if(mRect.top < ice.top){
            mYCoord = ice.top;
        }
        if(mRect.bottom > ice.bottom){
            mYCoord = ice.bottom - mLength;
        }

        // Update the Bat graphics
        mRect.top = mYCoord;
        mRect.bottom = mYCoord + mLength;
    }
}
