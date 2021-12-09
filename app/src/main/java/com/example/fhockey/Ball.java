package com.example.fhockey;

import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

public class Ball {

    private RectF ice;
    private RectF mRect;
    private float mXVelocity;
    private float mYVelocity;
    private float mBallWidth;
    private float mBallHeight;

    /*
    TODO: Fix constructor
    public Ball(int screenX, int screenY){

    // Make the mBall size relative to the screen resolution
    mBallWidth = screenX / 100;
    mBallHeight = mBallWidth;

    mYVelocity = screenY / 4;
    mXVelocity = mYVelocity;

    // Initialize the Rect that represents the mBall
    mRect = new RectF();

     */
    public Ball(RectF ice) {
        mBallWidth = ice.width() / 100;
        mBallHeight = mBallWidth;

        mXVelocity = ice.width() / 10;
        mYVelocity = mXVelocity;

        this.ice = ice;

        mRect = new RectF();

        mRect.top = ice.height() / 2;
        ice.left = ice.width() / 20;
        mRect.bottom = mRect.top + mBallHeight;
        mRect.right = ice.left + mBallWidth;
    }

    public RectF getRect() {
        return mRect;
    }

    public void update(long fps) {
        mRect.left = mRect.left + (mXVelocity / fps);

        mRect.top = mRect.top + (mYVelocity / fps);
        mRect.right = mRect.left + mBallWidth;
        mRect.bottom = mRect.top + mBallHeight;

        //Log.d("ballTop", String.valueOf(mRect.top));
    }

    // Reverse the vertical heading
    public void reverseYVelocity() {
        mYVelocity = -mYVelocity;
    }

    // Reverse the horizontal heading
    public void reverseXVelocity() {
        mXVelocity = -mXVelocity;
    }

    public void setRandomYVelocity() {

        // Generate a random number either 0 or 1
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if (answer == 0) {
            reverseYVelocity();

        }
    }

//    public void increaseVelocity(){
//        mXVelocity = mXVelocity + mXVelocity / 10;
//        mYVelocity = mYVelocity + mYVelocity / 10;
//    }

    public void clearObstacleY(float y){
        mRect.bottom = y + mBallHeight;
        mRect.top = y;
    }

    public void clearObstacleX(float x){
        mRect.left = x;
        mRect.right = x + mBallWidth;
    }

    public void reset(int x, int y){
        mRect.top = y / 20;
        mRect.left = x / 2;

        mRect.bottom = mRect.top + mBallHeight;
        mRect.right = mRect.left + mBallWidth;
    }
}
