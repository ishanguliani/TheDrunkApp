package thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import junit.framework.Assert;


public class PhysicalChallenge extends Activity {

    //variable to keep track if the game has begun
    public Boolean gamestarted = false ;

    //instantiate the async task
    UpdateTimerTask updatetimertask ;

    //media player instance
    private static MediaPlayer player ;

    public Integer SCORE = -1;
    public Integer TOTAL_SCORE = -2;

    private final static Integer RANDOM = 0;

    private static Integer speedMode = RANDOM;

    private static final String TAG = "Lab-Graphics";

    // Main view
    private RelativeLayout mFrame;

    // Bubble image
    private Bitmap mBitmap;

    // Display dimensions
    private int mDisplayWidth, mDisplayHeight;

    // Sound variables

    // AudioManager
    private AudioManager mAudioManager;
    // SoundPool
    private SoundPool mSoundPool;
    // ID for the bubble popping sound
    private int mSoundID;
    // Audio volume
    private float mStreamVolume;

    // Gesture Detector
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         /*MAKE THE ACTIVITY FULL SCREEN*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_physical_challenge);


        mFrame = (RelativeLayout) findViewById(R.id.frame);

        // Load basic bubble Bitmap
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);


        //TO get the screensize
        int X_VALUE = getScreenSizeX();
        int Y_VALUE = getScreenSizeY();
        Log.i("TAG", "Screen size for toast started")  ;

         randomGenerateModified(X_VALUE, Y_VALUE);

         //initialize the media player instance
        player = new MediaPlayer() ;
        //instantiate the AsyncTask UpdateTimerTask
        updatetimertask = new UpdateTimerTask();
        //execute the Asynctask in the background to start the timer
        updatetimertask.execute(1);
        
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Manage bubble popping sound
        // Use AudioManager.STREAM_MUSIC as stream type

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mStreamVolume = (float) mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC)
                / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        // DONE - make a new SoundPool, allowing up to 10 streams
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        // DONE - set a SoundPool OnLoadCompletedListener that calls setupGestureDetector()
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupGestureDetector();

                // simple test of the sound
                //soundPool.play(sampleId, 0.5f, 0.5f, 0, 0, 1.0f);
            }
        });

        // DONE - load the sound from res/raw/bubble_pop.wav
        mSoundID = mSoundPool.load(getApplicationContext(), R.raw.bubble_pop, 1);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            // Get the size of the display so this view knows where borders are
            mDisplayWidth = mFrame.getWidth();
            mDisplayHeight = mFrame.getHeight();

        }
    }

    // Set up GestureDetector
    private void setupGestureDetector() {

        mGestureDetector = new GestureDetector(this,

                new GestureDetector.SimpleOnGestureListener() {

                    // If a fling gesture starts on a BubbleView then change the
                    // BubbleView's velocity

                    // If a single tap intersects a BubbleView, then pop the BubbleView
                    // Otherwise, create a new BubbleView at the tap's location and add
                    // it to mFrame. You can get all views from mFrame with ViewGroup.getChildAt()

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent event) {

                        // DONE - Implement onSingleTapConfirmed actions.
                        // You can get all Views in mFrame using the
                        // ViewGroup.getChildCount() method

                        float eventX, eventY;
                        boolean addNew = true;

                        eventX = event.getRawX();
                        eventY = event.getRawY();

                        // iterate over all the bubbles currently being displayed
                        for (int i = 0; i < mFrame.getChildCount(); i++) {
                            BubbleView current = (BubbleView) mFrame.getChildAt(i);

                            // if the tap intersects with a bubble, then we should "pop" it
                            if (current.intersects(eventX, eventY)) {
                                current.stop(true);        // tell the BubbleView to stop
                                addNew = false;
                                break;
                            }
                        }

                        // if it did not intersect with any, add a new bubble

                        // always return true because action has been consumed... that makes sense, right?
                        return true;
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // DONE - delegate the touch to the gestureDetector

        mGestureDetector.onTouchEvent(event);

        return false;

    }

    @Override
    protected void onPause() {

        // DONE - Release all SoundPool resources
        mSoundPool.release();

        super.onPause();
    }

    // BubbleView is a View that displays a bubble.
    // This class handles animating, drawing, popping amongst other actions.
    // A new BubbleView is created for each bubble on the display

    private class BubbleView extends View {

        private static final int BITMAP_SIZE = 64;
        private static final int REFRESH_RATE = 40;
        private final Paint mPainter = new Paint();
        private ScheduledFuture<?> mMoverFuture;
        private int mScaledBitmapWidth;
        private Bitmap mScaledBitmap;

        // location, speed and direction of the bubble
        // mXPos, mYPos represents the top-left corner of the image because
        // that is the way that drawing methods deal with bitmaps
        private float mXPos, mYPos, mDx, mDy;
        private long mRotate, mDRotate;

        public BubbleView(Context context, float x, float y) {
            super(context);
            log("Creating Bubble at: x:" + x + " y:" + y);

            // Create a new random number generator to
            // randomize size, rotation, speed and direction
            Random r = new Random();

            // Creates the bubble bitmap for this BubbleView
            createScaledBitmap(r);

            // Adjust position to center the bubble under user's finger
            mXPos = x - mScaledBitmapWidth / 2;
            mYPos = y - mScaledBitmapWidth / 2;

            // Set the BubbleView's speed and direction

            setSpeedAndDirection(r);

            mDRotate = 3 ;
            mPainter.setAntiAlias(true);

        }


        private void setSpeedAndDirection(Random r) {

            mDx = r.nextFloat() * 15.0f - 7.5f;
            mDy = r.nextFloat() * 15.0f - 7.5f;

        }

        private void createScaledBitmap(Random r) {

            if (speedMode != RANDOM) {

                mScaledBitmapWidth = BITMAP_SIZE * 2;

            } else {

              mScaledBitmapWidth = 2 * BITMAP_SIZE;

            }
            // DONE - create the scaled bitmap using size set above
            // use the mBitmap variable that was created with the BitmapFactory earlier
            mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mScaledBitmapWidth, mScaledBitmapWidth, true);
        }

        // Start moving the BubbleView & updating the display
        private void start() {

            // Creates a WorkerThread
            ScheduledExecutorService executor = Executors
                    .newScheduledThreadPool(1);

            // Execute the run() in Worker Thread every REFRESH_RATE
            // milliseconds
            // Save reference to this job in mMoverFuture
            mMoverFuture = executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    // DONE - implement movement logic.
                    // Each time this method is run the BubbleView should
                    // move one step. If the BubbleView exits the display,
                    // stop the BubbleView's Worker Thread.
                    // Otherwise, request that the BubbleView be redrawn.

                    // moveWhileOnScreen() handles the movement
                    boolean offScreen = moveWhileOnScreen();

                    if (offScreen)
                        stop(false);    // stop the bubble's movement, but don't play pop sound

                    // postInvalidate is used to refresh the screen from a non-UI thread
                    BubbleView.this.postInvalidate();

                }
            }, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
        }

        private synchronized boolean intersects(float x, float y) {

            // DONE - Return true if the BubbleView intersects position (x,y)
            if ((x > mXPos && x < mXPos + mScaledBitmapWidth) &&    // if the point is within the width
                    (y > mYPos && y < mYPos + mScaledBitmapWidth))        // and, if it is within the height
                return true;

            return false;
        }

        // Cancel the Bubble's movement
        // Remove Bubble from mFrame
        // Play pop sound if the BubbleView was popped

        private void stop(final boolean popped) {
            TOTAL_SCORE++;

            if (null != mMoverFuture && mMoverFuture.cancel(true)) {

                // This work will be performed on the UI Thread

                mFrame.post(new Runnable() {
                    @Override
                    public void run() {

                        // DONE - Remove the BubbleView from mFrame

                        mFrame.removeView(BubbleView.this);

                        if (popped) {
                            log("Pop!");

                            // DONE - If the bubble was popped by user,
                            // play the popping sound, with mSoundID being the
                            // variable set earlier
                            mSoundPool.play(mSoundID, 0.5f, 0.5f, 0, 0, 1.0f);
                            SCORE++;
                            startgame(gamestarted);
                            Toast.makeText(getApplicationContext(),"***Score = " + SCORE + " / " + TOTAL_SCORE + "***",Toast.LENGTH_SHORT).show();

                        }

                        //Regeneration of bubble after Popping the last bubble
                        log("Bubble removed from view!");

                        //TO get the screensize
                        int X_VALUE = getScreenSizeX();
                        int Y_VALUE = getScreenSizeY();
                        Log.i("TAG", "Screen size for toast started");

                        Log.i("TAG", "Screensize  shown");

                        randomGenerate(X_VALUE, Y_VALUE);
                        Log.i("TAG", "Bubble Generated ");


                    }
                });
            }
        }


        // Draw the Bubble at its current location
        @Override
        protected synchronized void onDraw(Canvas canvas) {



            // DONE - save the canvas
            canvas.save();

            // DONE - increase the rotation of the original image by mDRotate
            mRotate += mDRotate;


            // DONE Rotate the canvas by current rotation
            // need to calculate the values of the center pivot point, since mXPos and mYPos represent the
            // top left corner
            canvas.rotate((float) mRotate, mXPos + (mScaledBitmapWidth / 2), mYPos + (mScaledBitmapWidth / 2));


            // DONE - draw the bitmap at it's new location
            canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter);


            // DONE - restore the canvas
            canvas.restore();

        }


        private synchronized boolean moveWhileOnScreen() {

            // DONE - Move the BubbleView
            // Returns true if the BubbleView has exited the screen

            mXPos += mDx;
            mYPos += mDy;

            return isOutOfView();
        }

        private boolean isOutOfView() {

            // DONE - Return true if the BubbleView has exited the screen

            if (mXPos > mDisplayWidth ||            // check the right edge
                    mXPos + mScaledBitmapWidth < 0 ||    // check the left edge
                    mYPos > mDisplayHeight ||            //   "    "  bottom edge
                    mYPos + mScaledBitmapWidth < 0        //   "    "  top edge
                    )
                return true;

            return false;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_physical_challenge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static void log(String message) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, message);
    }

    public void randomGenerate(int MAX_X_VALUE, int MAX_Y_VALUE) {

        Random rx = new Random();
        int ix = rx.nextInt(MAX_X_VALUE - 0) + 0;

        Random ry = new Random();
        int iy = ry.nextInt(MAX_Y_VALUE - 0) + 0;

        BubbleView temp1 = new BubbleView(mFrame.getContext(), ix, iy);
        Log.i("TAG", "BubbleView Created at " + ix + "and" + iy);
        mFrame.addView(temp1); // add it to the view so that it will draw on the screen
        Log.i("TAG", "BubbleView drawn");


        temp1.start();         // start the bubble's motion
        Log.i("TAG", "Motion Started");
    }

    public int getScreenSizeX() {
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        int width = screenSize.x;
        return width;
    }

    public int getScreenSizeY() {
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize1 = new Point();
        display.getSize(screenSize1);
        int height = screenSize1.y;
        return height;
    }

    public void randomGenerateModified(int MAX_X_VALUE, int MAX_Y_VALUE){


        // DONE - Implement onSingleTapConfirmed actions.
        // You can get all Views in mFrame using the
        // ViewGroup.getChildCount() method


        TOTAL_SCORE++;
        Random rx = new Random();
        int ix = rx.nextInt(MAX_X_VALUE - 0) + 0;

        Random ry = new Random();
        int iy = ry.nextInt(MAX_Y_VALUE - 0) + 0;


        BubbleView temp1 = new BubbleView(mFrame.getContext(), ix, iy);
        Log.i("TAG", "BubbleView Created at " + ix + "and" + iy);
        mFrame.addView(temp1); // add it to the view so that it will draw on the screen
        Log.i("TAG", "BubbleView drawn");
        temp1.start();


        // iterate over all the bubbles currently being displayed
        for (int i = 0; i < mFrame.getChildCount(); i++) {
            BubbleView current = (BubbleView) mFrame.getChildAt(i);

            // if the tap intersects with a bubble, then we should "pop" it
            if (current.intersects(ix,iy)) {
                current.stop(true);        // tell the BubbleView to stop
                //addNew = false;
                break;
            }
        }

        // if it did not intersect with any, add a new bubble

    }

    public void playaudio(String playfile){

        if(MainActivity.GameSound == false)  {
            return;
        }

        player = MediaPlayer.create(PhysicalChallenge.this, getAudio(PhysicalChallenge.this, playfile));
        player.start();
    }

    public void stopaudio(){
        if(MainActivity.GameSound == false)  {
            return;
        }

        if(player.isPlaying() == true)  {
            player.stop();
        }
    }


    public static int getAudio(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);
        return context.getResources().getIdentifier(name,"raw", context.getPackageName());
    }

    //the asynctask class has been added on 25th April 2015 by Ishan
    class UpdateTimerTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            //start playing the background audio
            playaudio("escape");
        }

        @Override
        protected Integer doInBackground(Integer... resId) {
            //delay the thread for one second to show updation on the screen after one second
            sleep();
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            //stop the audio
            stopaudio();
            Toast.makeText(getApplicationContext(),"***Final Score = " + SCORE + " / " + TOTAL_SCORE + "***",Toast.LENGTH_LONG).show();
            //finish the activity
            //PhysicalChallenge.this.finish();

            Intent intent = new Intent(PhysicalChallenge.this, FinalScoreActivity.class) ;
            startActivity(intent);
        }

        private void sleep() {
            try {
                Thread.sleep(30000);
             } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void startgame(Boolean gamestart)    {

        if(gamestart == true) {
            return ;
        }

            Toast toast = Toast.makeText(PhysicalChallenge.this, "25 second challenge BEGINS!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            gamestarted = true ;

    }

}
