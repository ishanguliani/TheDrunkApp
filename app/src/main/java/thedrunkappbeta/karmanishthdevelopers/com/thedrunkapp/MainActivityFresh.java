package thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import android.widget.ProgressBar;


import junit.framework.Assert;

import java.awt.font.NumericShaper;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivityFresh extends Activity {

    //TAG
    private static final String TAG = "DRUNK_TAG";

    //editted on 25th March
    //Add variable for maximum question number
    private static final int MAX_QUESTION_NUMBER = 10 ;

    /*ADDING VARIABLES FOR COUNTDOWN TIMER*/
    CountDownTimer countdowntimer;

    /*Addied by dhiraj for progress bar and media player*/
    private static final int PROGRESS = 0x1;
    private static Thread mThread;
    private static ProgressBar mProgress;
    private static int mProgressStatus = 0;
    private static MediaPlayer player;

    private static Handler mHandler ;

    /* Adeed by dhiraj till here*/

    private static final int MAX_TIME_QUESTION = 16000;    //Time Limit for each question
    private static final int FIRST_COLOR_CHANGE = 11000;    //Time at which first color change will happen
    private static final int SECOND_COLOR_CHANGE = 6000;   //Time at which second color change will happen
    private static final int TIME_DELAY = 1000;           //Time delay after which time will be displayed
    private static final int COLOR_BLACK_FIRST_DISPLAYED = Color.BLACK;  //Text color changed to BLACK
    private static final int COLOR_BLUE_SECOND_DISPLAYED = Color.BLUE;   //Text color changed to BLUE
    private static final int COLOR_RED_LAST_DISPLAYED = Color.RED;       //Text color changed to RED

    TextView mTextField = null ;
    /*VARIABLES END*/

    //get an instance to the vibrator
    //private Vibrator myVib;

    //random number
    public static Random ran ;
    //store random number
    private static int r ;

    //current difficulty level
    String CurrentDiffLevel = null ;

    //array to maintain the random numbers which have already been generated
    public static int[] NumbersGenerated = new int[100] ;

    private  String CorrectAnswer ;
    private  String OptionSelectedByUser ;

    private static int question_number = 1 ;

    private Context context = MainActivityFresh.this ;

    //textview to change the question statement
    public static TextView tx = null ;

    //array to store all the questions
    private  String[] array_questions = null ;
    //array to store the options
    private  String[] array_option_a = null ;
    private  String[] array_option_b = null ;
    private  String[] array_option_c = null ;
    private  String[] array_option_d = null ;

    //array to store penalty
    private  int[] array_penalty = null ;
    //array to store correct_answer
    private  String[] array_correct_answer = null ;

    //array to store difficulty level
    private  String[] array_level = null ;

    //private String ANSWER = "CORRECT" ;
    private final  Button SubmitButton = null ;

    private static RadioButton choice1 = null , choice2= null , choice3= null , choice4= null  ;
    private View.OnClickListener radioListener ;

    private  Boolean AnswerSelected = false ;
    private  Boolean CorrectAnswerSelected = false ;

    private  View contentView = null ;

    private static int CurrentQuestionNumber = 0 ;
    private  int CurrentQuestionPenalty = 0 ;
    private static int i = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          /*MAKE THE ACTIVITY FULL SCREEN*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_fresh);


        if( getIntent().getIntExtra("replay_activity" , 100) == 555 )   {

            question_number = 1 ;
            CorrectActivity.PenaltyScoredMax = 0 ;
            CorrectActivity.PenaltyScored = 0 ;

        }

        //link array to questions
        array_questions = getResources().getStringArray(R.array.questions) ;

        //link array to options
        array_option_a = getResources().getStringArray(R.array.option_a) ;
        array_option_b = getResources().getStringArray(R.array.option_b) ;
        array_option_c = getResources().getStringArray(R.array.option_c) ;
        array_option_d = getResources().getStringArray(R.array.option_d) ;

        //link array to correct_answer
        array_correct_answer = getResources().getStringArray(R.array.correct_answer) ;

        //link array to penalties
        array_penalty = getResources().getIntArray(R.array.penalty) ;

        //link array to difficulty level
        array_level = getResources().getStringArray(R.array.level) ;

        //get random number

        CurrentQuestionNumber = getRandomNumber() ;

        //
        CurrentQuestionPenalty = array_penalty[CurrentQuestionNumber] ;

        CurrentDiffLevel = array_level[CurrentQuestionNumber] ;

        //link all UI elements to the screen
       //LinkToScreen() ;
        TextView tv= (TextView) findViewById(R.id.textView);
        TextView ques_no= (TextView) findViewById(R.id.text_question_number);

        tv.setText(array_questions[CurrentQuestionNumber]);
        ques_no.setText("Question : " + question_number);

        CorrectAnswer = array_correct_answer[CurrentQuestionNumber] ;
        Log.i("my_app", "The Correct Answer is :" + CorrectAnswer) ;

        choice1 = (RadioButton)findViewById(R.id.option_A);
        choice2 = (RadioButton)findViewById(R.id.option_B);
        choice3 = (RadioButton)findViewById(R.id.option_C);
        choice4 = (RadioButton)findViewById(R.id.option_D);

        choice1.setText(array_option_a[CurrentQuestionNumber]);
        choice2.setText(array_option_b[CurrentQuestionNumber]);
        choice3.setText(array_option_c[CurrentQuestionNumber]);
        choice4.setText(array_option_d[CurrentQuestionNumber]);

        choice1.setChecked(false);
        choice2.setChecked(false);
        choice3.setChecked(false);
        choice4.setChecked(false);

        question_number = question_number + 1 ;

        //newedit 25th march
        //check if the current question number has reached the maximum number - if true
        //then finish the quiz and pass the control to the final score calculating activity
        if(question_number == MAX_QUESTION_NUMBER)   {

            Intent intent = new Intent(MainActivityFresh.this, FinalScoreActivity.class) ;
            intent.putExtra("final_score" , CorrectActivity.PenaltyScored) ;
            intent.putExtra("final_score_max" , CorrectActivity.PenaltyScoredMax) ;

            startActivity(intent);
        }

        //link the vibrator
        //myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);


        //////////Needs to be changed for a horizontal bar timer by dhiraj

        mProgress = (ProgressBar) findViewById(R.id.progressBar2);

        mProgress.setVisibility(View.VISIBLE);
        playaudio("tick");

        /*INITIALIZE THE HANDLER*/
        mHandler = new Handler() ;

        // Start lengthy operation in a background thread
        mThread = new Thread(){
                //(new Runnable() {
            @Override
            public void run(){
                while (mProgressStatus < 100) {

                    mProgressStatus = mProgressStatus + 100/15;

                    try{
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e){

                    }
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);

                            if(mProgressStatus >= 80)
                            {
                                stopaudio();
                                playaudio("timeover");
                            }
                                if(mProgressStatus >= 70)
                            {
                                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.redprogressbar));

                            }
                            else if (mProgressStatus >= 40)
                            {


                                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.blueprogressbar));
                            }
                            else
                            {
                                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.greenprogressbar));
                            }

                        }
                    }); //end of Runnable of mHandler

                } //end of while loop



                /*Now Stop this thread*/
                stopThread();
                Log.i(TAG, "stopThread() called after finishing timer");

            } //end of run method



        }; //end of the thread

        /*START THE THREAD INSIDE ONCREATE()*/
        mThread.setName("updateThread");
        mThread.start();

        //////Code changed till here by dhiraj


    /*LINK THE COUNTDOWN TIMER TEXTVIEW*/
/*        mTextField = (TextView) findViewById(R.id.text_countdown) ;
      *//*ADD COUNTDOWN TIMER*//*
        countdowntimer = new CountDownTimer(MAX_TIME_QUESTION, TIME_DELAY) {

            //onTick is invoked when the countdown is ticking
            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished > 0 && millisUntilFinished < SECOND_COLOR_CHANGE) {
                    mTextField.setTextColor(COLOR_RED_LAST_DISPLAYED);
                } else if (millisUntilFinished > SECOND_COLOR_CHANGE && millisUntilFinished < FIRST_COLOR_CHANGE) {
                    mTextField.setTextColor(COLOR_BLUE_SECOND_DISPLAYED);
                } else if (millisUntilFinished > FIRST_COLOR_CHANGE && millisUntilFinished < MAX_TIME_QUESTION) {
                    mTextField.setTextColor(COLOR_BLACK_FIRST_DISPLAYED);

                }

                if ((millisUntilFinished / TIME_DELAY) < 10) {
                    mTextField.setText("00:0" + millisUntilFinished / TIME_DELAY);
                } else {

                    mTextField.setText("00:" + millisUntilFinished / TIME_DELAY);
                }

            }

            //onFinish is invoked when the countdown is finished
            public void onFinish() {

                //We want to show that the user has not selected any answer out of the four and hence does not get any
                //points
                AnswerSelected = true ;
                CorrectAnswerSelected = false ;
                SubmitPressed(null);


            }
        }.start();*/

    }  /*End of Oncreate()*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_fresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void playaudio(String playfile){

        player = MediaPlayer.create(MainActivityFresh.this, getAudio(MainActivityFresh.this, playfile) );
        player.start();

    }


    /*STOP THE RUNNING THREAD*/
    public void stopThread(){

        Log.i(TAG, "stopThread() called") ;

        if(mThread.getName().equals("updateThread".toString()))   {

            Log.i(TAG, "inside if block");
            mThread.interrupt();

            /*set the value of the variables to show that nothing has been selected*/
            AnswerSelected = true ;
            CorrectAnswerSelected = false ;

            /*explicitly call the SubmitPressed() method*/
            SubmitPressed(null);

            /*stop the audio*/
            stopaudio();



        }
        Log.i(TAG, "Interrupt done");

        //Thread.interrupted();
    }

    public void stopaudio(){

        //player = MediaPlayer.create(MainActivityFresh.this, getAudio(MainActivityFresh.this, playfile) );


        //ADDED 29th March
        if(player.isPlaying() == true)  {

            player.stop();
        }


    }

    //respond to the press of a radio button
    public void onRadioButtonPressed(View v)    {


        boolean checked = ((RadioButton)v).isChecked() ;


        switch(v.getId())   {

            case R.id.option_A :     AnswerSelected = true ;

      //          test_toast(getResources().getResourceEntryName(R.id.option_A));

                if(getResources().getResourceEntryName(R.id.option_A).equals(CorrectAnswer) == true )  {

                    CorrectAnswerSelected = true ;

                }else CorrectAnswerSelected = false ;
                break ;


            case R.id.option_B :     AnswerSelected = true ;

        //        test_toast(getResources().getResourceEntryName(R.id.option_B));

                if(getResources().getResourceEntryName(R.id.option_B).equals(CorrectAnswer) == true )  {

                    CorrectAnswerSelected = true ;

                }else CorrectAnswerSelected = false ;
                break ;


            case R.id.option_C :     AnswerSelected = true ;

          //      test_toast(getResources().getResourceEntryName(R.id.option_C));

                if(getResources().getResourceEntryName(R.id.option_C).equals(CorrectAnswer) == true )  {

                    CorrectAnswerSelected = true ;

                }else CorrectAnswerSelected = false ;
                break ;


            case R.id.option_D :     AnswerSelected = true ;

            //    test_toast(getResources().getResourceEntryName(R.id.option_D));

                if(getResources().getResourceEntryName(R.id.option_D).equals(CorrectAnswer) == true )  {

                    CorrectAnswerSelected = true ;

                }else CorrectAnswerSelected = false ;
                break ;


        }
    }

    public int getRandomNumber()    {
        ran = new Random();

        switch(question_number) {

            case 1 :
            case 2 :
                //generate a cakewalk difficulty level question
                r = ran.nextInt(9) + 10 ;
                break;

            case 3 :
            case 4 :
                //generate a moderate difficulty level question
                r = ran.nextInt(9) + 30 ;
                break;

            case 5 :
            case 6 :
                //generate an extreme difficulty level question
                r = ran.nextInt(9) + 20 ;
                break;

            case 7 :
            case 8 :
            case 9 :
            case 10 :
                //generate a hardcore difficulty level question
                r = ran.nextInt(9) ;
                break ;
        }

        boolean b = Arrays.asList(NumbersGenerated).contains(r);
        if( b == true)  {
            Log.i("my_app", "Random Number matched " + r) ;
            getRandomNumber() ;
        }
        else {
            NumbersGenerated[i] = r ;
            //editted on 12TH mARCH EDIT
            // i++ ;
        }
        Log.i("my_app", "Random Number method entered : " + r) ;
        return(r) ;
    }

    private void NothingSelected()  {

        Toast.makeText(getApplicationContext()
                , R.string.toast_nothing_selected
                , Toast.LENGTH_SHORT)
                .show();
    }

    //newedit 25th march
    public void SubmitPressed(View v)   {

       /* stopThread();
        Log.i(TAG, "stopThread called inside SubmitPressed()") ;*/

        /*ENSURE THAT NO AUDIO IS PLAYING FROM THE PREVIOUS QUESTION*/
        stopaudio();

        if(AnswerSelected == null)
            /*if no radio button has been pressed even once*/
            NothingSelected();

        else if(AnswerSelected == false)    {

            /*if time ran out and the user selected nothing*/
            timeover();
        }
        else if (CorrectAnswerSelected == true){

            //if the user selected an answer and that answer is correct
            Intent intent = new Intent(context, CorrectActivity.class) ;
            intent.putExtra("my_app" , 1) ;
            intent.putExtra("my_penalty_correct", CurrentQuestionPenalty) ;
            startActivity(intent) ;
        }
        else if (CorrectAnswerSelected == false){

            //if the user selected an answer and that answer is incorrect
            Intent intent = new Intent(context, CorrectActivity.class) ;
            intent.putExtra("my_app" , 0) ;
            intent.putExtra("my_penalty", CurrentQuestionPenalty) ;
            startActivity(intent) ;
            //  myVib.vibrate(800);
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
        Toast.makeText(getApplicationContext()
                ,"Didn't they teach you to Never turn back? DRUNKY!"
                ,Toast.LENGTH_SHORT
        ).show();
    }

    //stops the countdown timer thread running in the background
    public void stopCountDownTimer()  {

        if(countdowntimer != null) {
            countdowntimer.cancel();
           // countdowntimer.purge();
            countdowntimer = null;
        }
    }

    public static int getAudio(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,"raw", context.getPackageName());
    }

    public void timeover()  {
        Intent intent = new Intent(context, CorrectActivity.class) ;
        intent.putExtra("my_app" , 2) ;
        intent.putExtra("my_penalty_correct", CurrentQuestionPenalty) ;
        startActivity(intent) ;

    }


}
