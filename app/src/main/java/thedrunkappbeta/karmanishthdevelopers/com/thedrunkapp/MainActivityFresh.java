package thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp;


//new github


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivityFresh extends Activity {

    //variable to calculate the score
    public static Integer NetScore = 0 ;

    //Boolean Variable
    Boolean b = null ;
    //set a flag to check if the time ran out
    private Boolean POST_EXECUTE_REACHED = false ; //will only get called if the time runs out

    //get an instance of UpdateTimerTask Asynctask
    UpdateTimerTask updatetimertask;

    //Timer thread sleep delay time
    private final int mDelay = 1000 ;

    //TAG
    private static final String TAG = "DRUNK_TAG";

    //editted on 25th March
    //Add variable for maximum question number
    private static final int MAX_QUESTION_NUMBER = 9 ;

    /*ADDING VARIABLES FOR COUNTDOWN TIMER*/
    CountDownTimer countdowntimer;

    /*Addied by dhiraj for progress bar and media player*/
    private static ProgressBar mProgress;
    private int mProgressStatus = 15;
    private static MediaPlayer player, player_fresh;

    //edit 7th April by Ishan
    private static final int MAX_PROGRESSBAR_LOWER_LIMIT = 0 ;
    //end of edit on 7th April by Ishan
    private static final int MAX_TIME_QUESTION = 16;    //Time Limit for each question
    private static final int FIRST_COLOR_CHANGE = 10;    //Time at which first color change will happen
    private static final int SECOND_COLOR_CHANGE = 6;   //Time at which second color change will happen
    private static final int TIME_DELAY = 1000;           //Time delay after which time will be displayed
    private static final int COLOR_BLACK_FIRST_DISPLAYED = Color.BLACK;  //Text color changed to BLACK
    private static final int COLOR_BLUE_SECOND_DISPLAYED = Color.BLUE;   //Text color changed to BLUE
    private static final int COLOR_RED_LAST_DISPLAYED = Color.RED;       //Text color changed to RED
    private static final int TIME_AUDIO_CHANGE = 5;    //Time at which first color change will
    private static final int TIME_INTERVAL_TIMER_INCREMENT = 1 ;

    //get an instance to the vibrator
    //private Vibrator myVib;

    //random number
    public Random ran ;
    //store random number
    private static Integer r  = 0;

    //array to maintain the random numbers which have already been generated
    public static Integer[] NumbersGenerated  = new Integer[100];

    private String CorrectAnswer ;

    private static int question_number = 1 ;

    private Context context = MainActivityFresh.this ;

    //array to store all the questions
    private String[] array_questions = null ;
    //array to store the options
    private String[] array_option_a = null ;
    private String[] array_option_b = null ;
    private String[] array_option_c = null ;
    private String[] array_option_d = null ;

    //array to store penalty
    private int[] array_penalty = null ;
    //array to store correct_answer
    private String[] array_correct_answer = null ;
    //link Radio Buttons to options
    private static RadioButton choice1 = null , choice2= null , choice3= null , choice4= null  ;
    private Boolean AnswerSelected = null ;
    private Boolean CorrectAnswerSelected = false ;

    private static int CurrentQuestionNumber = 0 ;
    private int CurrentQuestionPenalty = 0 ;
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
            NetScore = 0 ;
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

        //link and initialize the progressBar
        mProgress = (ProgressBar) findViewById(R.id.progressBar2);
        mProgress.setVisibility(ProgressBar.VISIBLE);
        mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.greenprogressbar));

        //get random number
        CurrentQuestionNumber = getRandomNumber() ;

        //initialize the media player
        player = new MediaPlayer() ;
        player_fresh = new MediaPlayer() ;

        CurrentQuestionPenalty = array_penalty[CurrentQuestionNumber] ;

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

        //check if the current question number has reached the maximum number - if true
        //then finish the quiz and pass the control to the final score calculating activity
        if(question_number < MAX_QUESTION_NUMBER)   {

            //instantiate the AsyncTask UpdateTimerTask
            updatetimertask = new UpdateTimerTask();
            //execute the Asynctask in the background to start the timer
            updatetimertask.execute(1);
        }

        else {

          //start the physical challenge
            Intent intent = new Intent(MainActivityFresh.this, PhysicalChallenge.class) ;
            startActivity(intent);
        }

        question_number = question_number + 1 ;
        //link the vibrator
        //myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

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

    public void playaudio(String playfile) {

        if (playfile.equals("timeover")) {
            player_fresh = MediaPlayer.create(MainActivityFresh.this, getAudio(MainActivityFresh.this, playfile));
            player_fresh.start();
        } else {
            player = MediaPlayer.create(MainActivityFresh.this, getAudio(MainActivityFresh.this, playfile));
            player.start();
        }
    }

    /*STOP THE RUNNING TIMER THREAD*/
    public void stopTask(){
        //stop the background audio
        stopaudio();
        //interrupt the currently running updatetimertask thread
        updatetimertask.cancel(true);
        //log the message
        Log.i(TAG, "Interrupt done");
    }

    public void stopaudio(){
        if(player.isPlaying() == true)  {
            player.stop();
        }
        if(player_fresh.isPlaying() == true)  {
            player_fresh.stop();
        }
    }

    //respond to the press of a radio button
    public void onRadioButtonPressed(View v)    {

        switch(v.getId())   {

            case R.id.option_A :

                AnswerSelected = true ;
                if(getResources().getResourceEntryName(R.id.option_A).equals(CorrectAnswer) == true )  {
                CorrectAnswerSelected = true ;

                }else CorrectAnswerSelected = false ;
                break ;


            case R.id.option_B :
                AnswerSelected = true ;
                if(getResources().getResourceEntryName(R.id.option_B).equals(CorrectAnswer) == true )  {
                CorrectAnswerSelected = true ;

                }else CorrectAnswerSelected = false ;
                break ;


            case R.id.option_C :
                AnswerSelected = true ;
                if(getResources().getResourceEntryName(R.id.option_C).equals(CorrectAnswer) == true )  {
                CorrectAnswerSelected = true ;

                }else CorrectAnswerSelected = false ;
                break ;


            case R.id.option_D :
                AnswerSelected = true ;
                if(getResources().getResourceEntryName(R.id.option_D).equals(CorrectAnswer) == true )  {
                CorrectAnswerSelected = true ;

                }else CorrectAnswerSelected = false ;
                break ;
        }
    }

    public int getRandomNumber()    {
        ran = new Random() ;
        switch(question_number) {

            case 1 :
                //INNOVATIVE
                //CAKEWALK 1
                //nextInt(UL-LL) + LL
                r = ran.nextInt(22) + 0 ;
                break;

            case 2 :
                //CAKEWALK 2
                //nextInt(UL-LL) + LL
                r = ran.nextInt(19) + 23 ;
                break;

            case 3 :
                //MODERATE 1
                //nextInt(UL-LL) + LL
                r = ran.nextInt(19) + 23 ;
                break;

            case 4 :
                //INNOVATIVE
                //MODERATE 2
                //nextInt(UL-LL) + LL
                r = ran.nextInt(22) + 0 ;
                break;

            case 5 :
                //DIFFICULT 1
                //nextInt(UL-LL) + LL
                r = ran.nextInt(19) + 43 ;
                break;

            case 6 :
                //DIFFICULT 2
                //nextInt(UL-LL) + LL
                r = ran.nextInt(19) + 43 ;
                break;

            case 7 :
                //INNOVATIVE
                //EXTREME 1
                //nextInt(UL-LL) + LL
                r = ran.nextInt(22) + 0 ;
                break;

            case 8 :

            case 9 :

            case 10 :

                //EXTREME 2
                //nextInt(UL-LL) + LL
                r = ran.nextInt(19) + 63 ;
                break;

                /*
            case 9 :
            case 10 :
                //generate a hardcore difficulty level question
                //first category in xml
                r = ran.nextInt(9) ;
                break ;*/
        }

        //check if the generated number has already been used
       //b = Arrays.asList(NumbersGenerated).contains(r);

        b = searchInArray(NumbersGenerated, r) ;
        //b = ArrayUtils.contains(NumbersGenerated, r) ;

        Log.i(TAG, "b assigned : " + b) ;
        if(b)  {
            Log.i(TAG, "Random Number matched " + r) ;
            getRandomNumber() ;
        }
        else {
            NumbersGenerated[i] = r ;
            Log.i(TAG, "" + r + " saved in index " + i) ;
            i++ ;
        }

        //Refresh the array NumbersGenerated if nearly full
        if(i > 70 )   {
            NumbersGenerated = null ;
            Log.i(TAG, "Array reassigned to NULL") ;
        }
        return(r) ;
    }

    private void NothingSelected()  {
        Toast.makeText(getApplicationContext()
                , R.string.toast_nothing_selected
                , Toast.LENGTH_SHORT)
                .show();
    }

    public void SubmitPressed(View v)   {
        //show the current time
       // ShowCurrentTime();
        if(AnswerSelected == null) {
            /*if no radio button has been pressed even once*/
            //NothingSelected();
            //check if the time ran out
            if(POST_EXECUTE_REACHED) {
                stopTask();
                timeover();
                ReturnNetScore("NO_SCORE");
            }
            else    {
                NothingSelected();
            }
        }

       else if (CorrectAnswerSelected == true){
            //stop the background task
            stopTask();
            //EDITTED 29th March
            correctanswer();
            //update score
            ReturnNetScore("NULL");
        }

        else if (CorrectAnswerSelected == false){
            //stop the background task
            stopTask();
            incorrectanswer();
            //  myVib.vibrate(800);
            //update score
            ReturnNetScore("NO_SCORE");
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

    public static int getAudio(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);
        return context.getResources().getIdentifier(name,"raw", context.getPackageName());
    }

    /*CALLED WHEN USER PRESSES SUBMIT BUTTON AFTER SELECTING INCORRECT ANSWER*/
    public void incorrectanswer()   {

        //if the user selected an answer and that answer is incorrect
        Intent intent = new Intent(context, CorrectActivity.class) ;
        intent.putExtra("my_app" , 0) ;
        intent.putExtra("my_penalty", CurrentQuestionPenalty) ;
        startActivity(intent) ;
        //play sound effect
        //playaudio("soundincorrect") ;
        //startNewTimerThread() ;
    }

    /*CALLED WHEN USER PRESSES SUBMIT BUTTON AFTER SELECTING CORRECT ANSWER*/
    public void correctanswer() {
        //if the user selected an answer and that answer is correct
        Intent intent = new Intent(context, CorrectActivity.class) ;
        intent.putExtra("my_app" , 1) ;
        intent.putExtra("my_penalty_correct", CurrentQuestionPenalty) ;
        startActivity(intent) ;
        //play sound effect
        //playaudio("soundcorrect");
    }

    /*CALLED WHEN USER PRESSES SUBMIT BUTTON WITHOUT SELECTING ANY ANSWER*/
    public void timeover()  {
        Intent intent = new Intent(context, CorrectActivity.class) ;
        intent.putExtra("my_app" , 2) ;
        intent.putExtra("my_penalty_correct", CurrentQuestionPenalty) ;
        startActivity(intent) ;
        //playaudio("soundtimeover") ;
    }

      //the asynctask class has been added on 25th April 2015 by Ishan
    class UpdateTimerTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            //start playing the background audio
           playaudio("tick");
            //DO SOMETHING HERE
           // mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.greenprogressbar));
            //mProgress.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... resId) {
            while (mProgressStatus >= MAX_PROGRESSBAR_LOWER_LIMIT) {
                // Update the progress bar
                publishProgress(mProgressStatus);
                //delay the thread for one second to show updation on the screen after one second
                sleep();

                //decremenet the timer by 1
                mProgressStatus = mProgressStatus - TIME_INTERVAL_TIMER_INCREMENT;

                if (mProgressStatus <= TIME_AUDIO_CHANGE) {
                    stopaudio();
                    playaudio("timeover");
                }

                //check if the thread has been interrupted manually
                if (isCancelled()) {
                    //if this is true, then come out of the while loop
                    break;
                }
            }   //end of while loop
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... mProgressStatus) {
            //update the progressbar
            mProgress.setProgress(mProgressStatus[0]);

            //editted by Ishan on 2nd May
            if ((mProgressStatus[0] <= FIRST_COLOR_CHANGE)&&(mProgressStatus[0] >= SECOND_COLOR_CHANGE)) {
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.blueprogressbar));
            } else if (mProgressStatus[0] < SECOND_COLOR_CHANGE) {
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.redprogressbar));
            } else {
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.greenprogressbar));
            } //end of while loops
        }

        @Override
        protected void onPostExecute(Integer result) {

            //log the report
            Log.i(TAG, "POST EXECUTE CALLED");
            //stop the audio
            //stopaudio();
            //Make the progress bar invisible
            //mProgress.setVisibility(ProgressBar.INVISIBLE);
            //FLAG
            POST_EXECUTE_REACHED = true ;
            //press the Submit button manually
            SubmitPressed(null);

        }

        private void sleep() {
            try {
                Thread.sleep(mDelay);
                //Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public static boolean searchInArray(Integer[] arr, Integer targetValue) {
        Set<Integer> set = new HashSet<Integer>(Arrays.asList(arr));
        return set.contains(targetValue);
    }

    /*function to Log/Toast the current value of mProgressStatus
    * as soon as the user presses the submit button*/
    public void ShowCurrentTime()   {
        Integer currentTime = mProgressStatus ;
        Toast.makeText(context, "Time is : " + currentTime, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Time is : " + currentTime ) ;
    }

    /*Function to return the current range out of the 3 ranges in which the timer sits when the user
    * submits the answer
    * The range can be one of the following :-
    * 1. RANGE_GREEN (mProgressStatus 0-5)
    * 2. RANGE_BLUE (mProgressStatus 6-10)
    * 3. RANGE_RED ()
    * */
    String ReturnCurrentTimerRange()    {

        Integer CurrentStatus = mProgressStatus + 1 ;


        if(CurrentStatus > 10 && CurrentStatus <= 15)   {
            Toast.makeText(context, "RETURNING GREEN", Toast.LENGTH_SHORT).show();
            return("RANGE_GREEN");
        }
        else if(CurrentStatus > 5 && CurrentStatus <= 10)   {
            Toast.makeText(context, "RETURNING BLUE", Toast.LENGTH_SHORT).show();
            return("RANGE_BLUE");
        }else   {
            Toast.makeText(context, "RETURNING RED", Toast.LENGTH_SHORT).show();
            return("RANGE_RED");
        }
    }

    /*Function to return the current score based on the current Answer RANGE
    * if RANGE_GREEN --> Score = 100(Cakewalk, Moderate, Difficult, Extreme)
    * if RANGE_BLUE --> Score = 50(Cakewalk) | 60(Moderate) | 70(Difficult) | 80(Extreme)
    * if RANGE_RED --> Score = 25(Cakewalk) | 30(Moderate) | 35(Difficult) | 40(Extreme)
    */
    void ReturnNetScore(String status)    {
        //if the timer ran out, assign ZERO score to NetScore
        if(status.equals("NO_SCORE".toString()))    {
            Toast.makeText(context
                    , "No Score Added!"
                    , Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String CurrentTimerRange = ReturnCurrentTimerRange() ;
        Log.i("ReceivedRange", "" + CurrentTimerRange) ;

        if(CurrentTimerRange.equals("RANGE_GREEN".toString()))  {

            NetScore = NetScore + 100 ;

            Toast.makeText(context
                    , "MARKS = "+ NetScore
                    , Toast.LENGTH_SHORT)
                    .show();

        }
        else if(CurrentTimerRange.equals("RANGE_BLUE".toString()))  {

            Integer CurrentQuestionWeightage = ReturnCurrentQuestionWeightage("RANGE_BLUE") ;

            NetScore = NetScore + CurrentQuestionWeightage;
            Toast.makeText(context
                    , "MARKS BLUE = " + NetScore
                    , Toast.LENGTH_SHORT)
                    .show();


        }
        else if(CurrentTimerRange.equals("RANGE_RED".toString()))  {

            Integer CurrentQuestionWeightage = ReturnCurrentQuestionWeightage("RANGE_RED") ;

            NetScore = NetScore + CurrentQuestionWeightage;
            Toast.makeText(context
                    , "MARKS RED = " + NetScore
                    , Toast.LENGTH_SHORT)
                    .show();

        }

        Log.i("ReceivedRange", "SCORE : " + NetScore) ;

    }

    Integer ReturnCurrentQuestionWeightage(String status)    {

        Integer weightage = 0 ;
        Integer CurrentQuestion = question_number - 1 ;
        if(status.equals("RANGE_BLUE".toString()))  {

            switch(CurrentQuestion) {
                case 1:
                    weightage = 50 ;
                    break;

                case 2:
                    weightage = 50 ;
                    break;

                case 3:
                    weightage = 60 ;
                    break;

                case 4:
                    weightage = 60 ;
                    break;

                case 5:
                    weightage = 70 ;
                    break;

                case 6:
                    weightage = 70 ;
                    break;

                case 7:
                    weightage = 70 ;
                    break;

                case 8:
                    weightage = 80 ;
                    break;

                case 9:
                    weightage = 80 ;
                    break;
            }
        }

        else    {
            switch(CurrentQuestion) {
                case 1:
                    weightage = 25 ;
                    break;

                case 2:
                    weightage = 25 ;
                    break;

                case 3:
                    weightage = 30 ;
                    break;

                case 4:
                    weightage = 30 ;
                    break;

                case 5:
                    weightage = 35 ;
                    break;

                case 6:
                    weightage = 35 ;
                    break;

                case 7:
                    weightage = 35 ;
                    break;

                case 8:
                    weightage = 40 ;
                    break;

                case 9:
                    weightage = 40 ;
                    break;
            }
        }
    return weightage ;
    }


}

