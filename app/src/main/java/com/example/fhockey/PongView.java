package com.example.fhockey;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class PongView extends SurfaceView implements Runnable {

    // This is our thread
    Thread mGameThread = null;

    // We need a SurfaceHolder object
    // We will see it in action in the draw method soon.
    SurfaceHolder mOurHolder;

    // A boolean which we will set and unset
    // when the game is running- or not
    // It is volatile because it is accessed from inside and outside the thread
    volatile boolean mPlaying;

    // Game is mPaused at the start
    boolean mPaused = true;

    // A Canvas and a Paint object
    Canvas mCanvas;
    Paint blue;
    Paint red;
    Paint background;
    Paint textPaint;
    Paint icePaint;
    Paint goalPaint;

    // This variable tracks the game frame rate
    long mFPS;

    // The size of the screen in pixels
    int mScreenX;
    int mScreenY;

    // The players mBat
    Bat mBat;
    Bat mBat2;

    Goalie mGoalie;
    Goalie mGoalie2;

    // A mBall
    Ball mBall;

    // For sound FX
    SoundPool sp;
    int beep1ID = -1;
    int beep2ID = -1;
    int beep3ID = -1;
    int loseLifeID = -1;
    int explodeID = -1;

    // The mScore
    int mScore = 0;

    // Lives
    int mLives = 3;

    int iceWidth;
    int iceHeight;

    RectF ice;

    /*
    When the we call new() on pongView
    This custom constructor runs
*/
    public PongView(Context context, int x, int y) {

    /*
        The next line of code asks the
        SurfaceView class to set up our object.
    */
        super(context);

        // Set the screen width and height
        mScreenX = x;
        mScreenY = y;

        // Initialize mOurHolder and mPaint objects
        mOurHolder = getHolder();

        background = new Paint();
        icePaint = new Paint();
        textPaint = new Paint();
        goalPaint = new Paint();
        blue = new Paint();
        red = new Paint();

        ice = new RectF(200, 50, mScreenX - 225, mScreenY - 300);

        iceWidth = (int)ice.width();
        iceHeight = (int)ice.height();

//        // A new mBat
//        mBat = new Bat(mScreenX, mScreenY, 1);
//        mBat2 = new Bat(mScreenX, mScreenY, 2);
        // A new mBat
        mBat = new Bat(ice, 1);
        mBat2 = new Bat(ice, 2);

        mGoalie = new Goalie(ice, 1);
        // Create a mBall
        mBall = new Ball(ice);

    /*
        Instantiate our sound pool
        dependent upon which version
        of Android is present
    */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }


        try{
            // Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("beep1.ogg");
            beep1ID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("beep2.ogg");
            beep2ID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("beep3.ogg");
            beep3ID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("loseLife.ogg");
            loseLifeID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("explode.ogg");
            explodeID = sp.load(descriptor, 0);

        }catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        setupAndRestart();
    }

    private void setupAndRestart() {
        // Put the mBall back to the start
        mBall.reset(iceWidth, iceHeight);

        // if game over reset scores and mLives
        if(mLives == 0) {
            mScore = 0;
            mLives = 3;
        }
    }

    // Everything that needs to be updated goes in here
    // Movement, collision detection etc.
    public void update(){
        mBat.update(mFPS);
        mBat2.update(mFPS);

        mGoalie.update(mFPS);

        mBall.update(mFPS);

        // Check for mBall colliding with mBat
        if(RectF.intersects(mBat.getRect(), mBall.getRect())) {
            mBall.setRandomYVelocity();
            mBall.reverseXVelocity();
            mBall.clearObstacleX(mBat.getRect().right + 2);

            mScore++;
            //mBall.increaseVelocity();

            sp.play(beep1ID, 1, 1, 0, 0, 1);
        }

        //TODO:Ball is causing game to pause

        // Bounce the mBall back when it hits the bottom of screen
        if(mBall.getRect().right > ice.right){
            mBall.reverseXVelocity();
            mBall.clearObstacleX(ice.right - 20);
        }

        if(mBall.getRect().top < ice.top){
            mBall.reverseYVelocity();
            mBall.clearObstacleY(ice.top + 20);

            sp.play(beep2ID, 1, 1, 0, 0, 1);
        }

        // If the mBall hits left wall bounce
        if(mBall.getRect().left < ice.left){
            mBall.reverseXVelocity();
            mBall.clearObstacleX(5);

            //Lose a life
            mLives--;
            sp.play(loseLifeID, 1, 1, 0, 0, 1);

            if(mLives == 0){
                mPaused = true;
                setupAndRestart();
            }

            sp.play(beep3ID, 1, 1, 0, 0, 1);
        }

        // If the mBall hits right wall bounce
        if(mBall.getRect().bottom > ice.bottom){
            mBall.reverseYVelocity();
            mBall.clearObstacleY(ice.bottom - 20);

            sp.play(beep3ID, 1, 1, 0, 0, 1);
        }
    }

    private void draw() {
        // Make sure our drawing surface is valid or we crash
        if (mOurHolder.getSurface().isValid()) {

            // Draw everything here

            // Lock the mCanvas ready to draw
            mCanvas = mOurHolder.lockCanvas();

            // Clear the screen with my favorite color
            mCanvas.drawColor(Color.parseColor("#000000"));

            icePaint.setColor(Color.parseColor("#bebebe"));

            // Choose the brush color for drawing
            red.setColor(Color.parseColor("#ff213f"));
            blue.setColor(Color.parseColor("#3e2bf5"));

            textPaint.setColor(Color.parseColor("#deddde"));

            ice = new RectF(100, 50, mScreenX - 125, mScreenY - 300);
            mCanvas.drawRect(ice, icePaint);

            //RectF topLeftSideline = new RectF(150, 100, 170, ice.bottom - 400);
            //mCanvas.drawRect(topLeftSideline, red);

            //RectF outerScreenleft = new RectF(0, 0, 10, ice.bottom - 100);
            //RectF

            // Draw the mBat
            mCanvas.drawRect(mBat.getRect(), red);
            mCanvas.drawRect(mBat2.getRect(), blue);

            mCanvas.drawRect(mGoalie.getRect(), red);
            mCanvas.drawRect(mGoalie.getmRect2(), red);

            // Draw the mBall
            mCanvas.drawRect(mBall.getRect(), blue);


            // Change the drawing color to white


            // Draw the mScore
            textPaint.setTextSize(40);
            mCanvas.drawText("Score: " + mScore + "   Lives: " + mLives, mScreenY - 100, 200, textPaint);
            mCanvas.drawText("Score: " + mScore + "   Lives: " + mLives, mScreenY - 100, mScreenX - 200, textPaint);

            // Draw everything to the screen
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void resume() {
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    public void pause() {
        mPlaying = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    @Override
    public void run() {
        while (mPlaying) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            // Update the frame
            if(!mPaused){
                update();
            }

            // Draw the frame
            draw();

        /*
            Calculate the FPS this frame
            We can then use the result to
            time animations in the update methods.
        */
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                mFPS = 1000 / timeThisFrame;
            }
        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() ;
        float x = event.getX() ;
        float y = event.getY() ;

        float width = mBat.getRect().width();
        float height = mBat.getRect().height();

        RectF touchPoint = new RectF(x, y, x + 100, y + 100);
        RectF touchPoint2 = new RectF(x - 100, y - 100, x, y);

        Log.d("xy", String.valueOf(x) + String.valueOf(y));

        switch(action) {
            case MotionEvent.ACTION_MOVE:

                if(mBat.getRect().intersect(touchPoint) || mBat.getRect().intersect(touchPoint2)) {
                    // Something should happen
                    mBat.set(touchPoint);
                    Log.d("touch", "touch");
                }

        }

        return true ;

    }

//    public boolean onTouchEvent(MotionEvent motionEvent) {
//
//        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
//
//            // Player has touched the screen
//            case MotionEvent.ACTION_DOWN:
//
//
//                mPaused = false;
//
//                // Is the touch on the down or up?
//                if(motionEvent.getY() > mScreenY / 2){
////                    mBat.setMovementState(mBat.DOWN);
////                    mGoalie.setMovementState(mGoalie.DOWN);
//                }
//                else{
////                    mBat.setMovementState(mBat.UP);
////                    mGoalie.setMovementState(mGoalie.UP);
//                }
//                break;
//
//            // Player has removed finger from screen
//            case MotionEvent.ACTION_UP:
//                mBat.setMovementState(mBat.STOPPED);
//                mGoalie.setMovementState(mGoalie.STOPPED);
//                break;


//        }
////        return true;
////    }
}
