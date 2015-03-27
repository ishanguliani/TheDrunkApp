package thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp.R.*;

/**
 * Demonstrates a "card-flip" animation using custom fragment transactions ({@link
 * android.app.FragmentTransaction#setCustomAnimations(int, int)}).
 *
 * <p>This sample shows an "info" action bar button that shows the back of a "card", rotating the
 * front of the card out and the back of the card in. The reverse animation is played when the user
 * presses the system Back button or the "photo" action bar button.</p>
 */
public class CardFlipActivity extends Activity
        implements FragmentManager.OnBackStackChangedListener {


    private static String CorrectAnswer ;
    private static String OptionSelectedByUser ;

    private static int question_number = 1 ;

    private Context context = CardFlipActivity.this ;

    //textview to change the question statement
    public static TextView tx = null ;

    //array to store all the questions
    private static String[] array_questions = null ;
    //array to store the options
    private static String[] array_option_a = null ;
    private static String[] array_option_b = null ;
    private static String[] array_option_c = null ;
    private static String[] array_option_d = null ;

    //array to store penalty
    private static String[] array_penalty = null ;
    //array to store correct_answer
    private static String[] array_correct_answer = null ;

    //private String ANSWER = "CORRECT" ;
    private final static Button SubmitButton = null ;


    private static RadioButton choice1 = null , choice2= null , choice3= null , choice4= null  ;
    private View.OnClickListener radioListener ;

    private static Boolean AnswerSelected = null ;
    private static Boolean CorrectAnswerSelected = null ;

    private static View contentView = null ;


    private static int CurrentQuestionNumber = 0 ;

    /**
     * A handler object, used for deferring UI operations.
     */
    private Handler mHandler = new Handler();

    /**
     * Whether or not we're showing the back of the card (otherwise showing the front).
     */
    private boolean mShowingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         /*MAKE THE ACTIVITY FULL SCREEN*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(layout.activity_card_flip);


       if ( getIntent().getIntExtra("fresh_call" , 0) == 99 )   {

           AnswerSelected = null ;
           CorrectAnswerSelected = null ;
       }

    //generate a random number
       // Random ran = new Random();
      //  CurrentQuestionNumber = ran.nextInt(5 - 1 + 1) + 1;


        //link array to questions
        array_questions = getResources().getStringArray(array.questions) ;

        //link array to options
        array_option_a = getResources().getStringArray(array.option_a) ;
        array_option_b = getResources().getStringArray(array.option_b) ;
        array_option_c = getResources().getStringArray(array.option_c) ;
        array_option_d = getResources().getStringArray(array.option_d) ;

        //link array to correct_answer
        array_correct_answer = getResources().getStringArray(array.correct_answer) ;

        //link array to penalties
        array_penalty = getResources().getStringArray(array.penalty) ;

        //array_questions.addAll()

        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing the
            // front of the card to this activity. If there is saved instance state,
            // this fragment will have already been added to the activity.
            getFragmentManager()
                    .beginTransaction()
                    .add(id.container, new CardFrontFragment())
                    .commit();

          //  contentView = View.inflate(this, R.layout.fragment_card_front, null);
 /*
        if(choice1.isChecked() == true )    {


            //show toast
            Toast.makeText(getApplicationContext()
                    , "A selected"
                    , Toast.LENGTH_SHORT)
                    .show();

            OptionSelectedByUser = choice1.getId() ;
        }   else  if(choice2.isChecked() == true )    {


            //show toast
            Toast.makeText(getApplicationContext()
                    , "B selected"
                    , Toast.LENGTH_SHORT)
                    .show();

            OptionSelectedByUser = choice2.getId() ;
        }else  if(choice3.isChecked() == true )    {


            //show toast
            Toast.makeText(getApplicationContext()
                    , "C selected"
                    , Toast.LENGTH_SHORT)
                    .show();

            OptionSelectedByUser = choice3.getId() ;
        }else  if(choice4.isChecked() == true )    {


            //show toast
            Toast.makeText(getApplicationContext()
                    , "D selected"
                    , Toast.LENGTH_SHORT)
                    .show();

            OptionSelectedByUser = choice4.getId() ;
        }

*/

          //  tx = (TextView)contentView.findViewById(R.id.textView) ;
           // tx.setText("Hello JI");
        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }

        // Monitor back stack changes to ensure the action bar shows the appropriate
        // button (either "photo" or "info").
        getFragmentManager().addOnBackStackChangedListener(this);


      /*  Button button = (Button)findViewById(R.id.button_submit) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });*/


        // Define a generic listener for all three RadioButtons in the RadioGroup


       /* View.OnClickListener radioListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) v;

                //mark that something has been selected by the user
                AnswerSelected = true ;

                //disable all other checkboxes
                DisableOtherOptions();

                    //check if the selection matches the correct selection

                if(R.id.option_A == rb.getId())    {
                    CorrectAnswerSelected = true ;
                }else   {
                    CorrectAnswerSelected = false ;
                }



                // tv.setText(rb.getText() + " chosen");
            }
        }; */

      /*  contentView = View.inflate(this, R.layout.fragment_card_front, null);



        tx = (TextView)contentView.findViewById(R.id.textView) ;


*/

        /*Toast.makeText(getApplicationContext()
                , array_questions[1]
                , Toast.LENGTH_SHORT)
                .show();
*/

      //  contentView.invalidate();

       //// final View viewgroup1 = View.inflate(this, R.id.radioGroup1, null) ;
      // final  View viewgroup2 = View.inflate(this, R.id.radioGroup2, null) ;


        /// Define a generic listener for all three RadioButtons in the RadioGroup
       /* final View.OnClickListener radioListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(v.getId() == R.id.radioGroup1 || v.getId() == R.id.radioGroup2)  {

                    Toast.makeText(getApplicationContext()
                            , "Caught the view"
                            , Toast.LENGTH_SHORT)
                            .show();
                }
                Toast.makeText(getApplicationContext()
                        , "Entered listener"
                        , Toast.LENGTH_SHORT)
                        .show();
                RadioButton rb = (RadioButton) v;
                AnswerSelected = true ;
                CorrectAnswerSelected = true ;
                DisableOtherOptions(rb);
                //tv.setText(rb.getText() + " chosen");
            }
        };*/

       // ATTACH LISTENER TO THE RADIOBUTTON


       //  choice1 = (RadioButton) contentView.findViewById(id.option_A);
         //Called when RadioButton choice1 is clicked
       /* choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext()
                        , "Entered listener"
                        , Toast.LENGTH_SHORT)
                        .show();
               // DisableOtherOptions(v);
            }
        }); */

     //    choice2 = (RadioButton) contentView.findViewById(id.option_B);
        // Called when RadioButton choice2 is clicked
      //  choice2.setOnClickListener(radioListener);

      //   choice3 = (RadioButton) contentView.findViewById(id.option_C);
        // Called when RadioButton choice3 is clicked
       // choice3.setOnClickListener(radioListener);

       // choice4 = (RadioButton) contentView.findViewById(id.option_D);
        // Called when RadioButton choice3 is clicked
     //   choice3.setOnClickListener(radioListener);



        /*// ATTACH LISTENER TO THE RADIOBUTTON

        choice1 = (RadioButton) findViewById(R.id.option_A);
        //Called when RadioButton choice1 is clicked
        choice1.setOnClickListener(radioListener);

        choice2 = (RadioButton) findViewById(R.id.option_B);
        // Called when RadioButton choice2 is clicked
        choice2.setOnClickListener(radioListener);

        choice3 = (RadioButton) findViewById(R.id.option_C);
        // Called when RadioButton choice3 is clicked
        choice3.setOnClickListener(radioListener);

        choice4 = (RadioButton) findViewById(R.id.option_D);
        // Called when RadioButton choice3 is clicked
        choice3.setOnClickListener(radioListener);

*/

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Add either a "photo" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
                mShowingBack
                        ? R.string.action_photo
                        : R.string.action_info);
        item.setIcon(mShowingBack
                ? R.drawable.ic_action_photo
                : R.drawable.ic_action_info);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_flip:
                flipCard();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }


        //add delay
        //Here's a runnable/handler combo
        Runnable mMyRunnable = new Runnable()
        {
            @Override
            public void run()
            {
               // onBackPressed();                    //Change state here

                getFragmentManager()
                        .beginTransaction()

                                // Replace the default fragment animations with animator resources representing
                                // rotations when switching to the back of the card, as well as animator
                                // resources representing rotations when flipping back to the front (e.g. when
                                // the system Back button is pressed).
                        .setCustomAnimations(
                                anim.card_flip_right_in, anim.card_flip_right_out,
                                anim.card_flip_left_in, anim.card_flip_left_out)

                                // Replace any fragments currently in the container view with a fragment
                                // representing the next page (indicated by the just-incremented currentPage
                                // variable).
                        .replace(id.container, new CardFrontFragment())

                                // Add this transaction to the back stack, allowing users to press Back
                                // to get to the front of the card.
                        .addToBackStack(null)

                                // Commit the transaction.
                        .commit();


                AnswerSelected = null ;
                CorrectAnswerSelected = null ;
            }
        };


        //deliberate runnable
        //add delay
        //Here's a runnable/handler combo
        Runnable mMyRunnablenew = new Runnable()
        {
            @Override
            public void run()
            {
                // onrestartfragment();                  //Change state here


                AnswerSelected = null ;
                CorrectAnswerSelected = null ;
            }
        };

        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        if(CorrectAnswerSelected == true) {




           /* //add delay
            //Here's a runnable/handler combo
             Runnable mMyRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    onBackPressed();                    //Change state here
                }
            };

*/         /* if(question_number % 2 == 0) {

                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnablenew, 2000);//Message will be delivered in 2 seconds.

                } else

                */{
                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnablenew, 2000);//Message will be delivered in 2 seconds.
                //end delay
            }

            getFragmentManager()
                    .beginTransaction()

                            // Replace the default fragment animations with animator resources representing
                            // rotations when switching to the back of the card, as well as animator
                            // resources representing rotations when flipping back to the front (e.g. when
                            // the system Back button is pressed).
                    .setCustomAnimations(
                            anim.card_flip_right_in, anim.card_flip_right_out,
                            anim.card_flip_left_in, anim.card_flip_left_out)

                            // Replace any fragments currently in the container view with a fragment
                            // representing the next page (indicated by the just-incremented currentPage
                            // variable).
                    .replace(id.container, new CardBackFragmentCorrect())

                            // Add this transaction to the back stack, allowing users to press Back
                            // to get to the front of the card.
                    .addToBackStack(null)

                            // Commit the transaction.
                    .commit();





        }else if(CorrectAnswerSelected == null)  {

          /*  if(question_number % 2 == 0) {

                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnablenew, 2000);//Message will be delivered in 2 seconds.

            } else

             */{
                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnablenew, 2000);//Message will be delivered in 2 seconds.
                //end delay
            }

            getFragmentManager()
                    .beginTransaction()

                            // Replace the default fragment animations with animator resources representing
                            // rotations when switching to the back of the card, as well as animator
                            // resources representing rotations when flipping back to the front (e.g. when
                            // the system Back button is pressed).
                    .setCustomAnimations(
                            anim.card_flip_right_in, anim.card_flip_right_out,
                            anim.card_flip_left_in, anim.card_flip_left_out)

                            // Replace any fragments currently in the container view with a fragment
                            // representing the next page (indicated by the just-incremented currentPage
                            // variable).
                    .replace(id.container, new CardBackFragmentIncorrect())

                            // Add this transaction to the back stack, allowing users to press Back
                            // to get to the front of the card.
                    .addToBackStack(null)

                            // Commit the transaction.
                    .commit();


        }

        // Defer an invalidation of the options menu (on modern devices, the action bar). This
        // can't be done immediately because the transaction may not yet be committed. Commits
        // are asynchronous in that they are posted to the main thread's message loop.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }



    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }

    /**
     * A fragment representing the front of the card.
     */
    public static class CardFrontFragment extends Fragment {
        public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            /*Toast.makeText(getApplicationContext()
                    , "onCreateView() Called"
                    , Toast.LENGTH_SHORT)
                    .show();
            */

            View view = inflater.inflate(layout.fragment_card_front,
                    container, false);


            //generate a random number
             Random ran = new Random();
             CurrentQuestionNumber = ran.nextInt(45) ;

            TextView tv= (TextView) view.findViewById(id.textView);
            TextView ques_no= (TextView) view.findViewById(id.text_question_number);



            tv.setText(array_questions[CurrentQuestionNumber]);
            ques_no.setText("Question : " + question_number);
            question_number = question_number + 1 ;


            CorrectAnswer = array_correct_answer[CurrentQuestionNumber] ;

            choice1.setText(array_option_a[CurrentQuestionNumber]);
            choice2.setText(array_option_b[CurrentQuestionNumber]);
            choice3.setText(array_option_c[CurrentQuestionNumber]);
            choice4.setText(array_option_d[CurrentQuestionNumber]);


             choice1 = (RadioButton) view.findViewById(id.option_A);
              choice2 = (RadioButton) view.findViewById(id.option_B);
             choice3 = (RadioButton) view.findViewById(id.option_C);
              choice4 = (RadioButton) view.findViewById(id.option_D);

            choice1.setChecked(false);
            choice2.setChecked(false);
            choice3.setChecked(false);
            choice4.setChecked(false);



            //  return inflater.inflate(layout.fragment_card_front, container, false);


            return view ;
           // tv.setText(rb.getText() + " chosen");
                }
    }

    /**
     * A fragment representing the back of the card.
     */
    public static class CardBackFragmentCorrect extends Fragment {
        public CardBackFragmentCorrect() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            AnswerSelected = null ;
          CorrectAnswerSelected = null ;
            return inflater.inflate(layout.fragment_card_back_correct, container, false);



        }
    }


    /**
     * A fragment representing the back of the card.
     */
    public static class CardBackFragmentIncorrect extends Fragment {
        public CardBackFragmentIncorrect() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            AnswerSelected = null ;
           CorrectAnswerSelected = null ;
            return inflater.inflate(layout.fragment_card_back_incorrect, container, false);

        }
    }



    /*
    METHOD TO IMPLEMENT RADIOGROUPS AND CHEck IF THE ANSWER CHOSEN BY THE USER IS TRUE
     */

    private boolean checkanswer(int UserOption) {
       /* final TextView tv = (TextView) findViewById(R.id.textView);

        /// Define a generic listener for all three RadioButtons in the RadioGroup
        final View.OnClickListener radioListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) v;
                tv.setText(rb.getText() + " chosen");
            }
        };

        //final RadioButton choice1 = (RadioButton) findViewById(R.id.choice1);
        // Called when RadioButton choice1 is clicked
        //choice1.setOnClickListener(radioListener);

        //final RadioButton choice2 = (RadioButton) findViewById(R.id.choice2);
        // Called when RadioButton choice2 is clicked
        //choice2.setOnClickListener(radioListener);

        //final RadioButton choice3 = (RadioButton) findViewById(R.id.choice3);
        // Called when RadioButton choice3 is clicked
        //choice3.setOnClickListener(radioListener);


      //  if( ... == rb.getText())
      */

        //if(OptionSelectedByUser.getId )


      //  if(UserOption == CorrectAnswer)

        if (true || false)
            return false ;
      else return true ;
    }


    /*
    MEtHOD TO USE MYRIAD-PRO LIGHT FONt STYKE
     */

   /* private void setfont()  {

        Typeface tf1 = Typeface.createFromAsset(getAssets(),
                "MYRIADPROREGULAR.ttf");
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "MyriadPro-Light.ttf");
        //tv2 = (TextView) findViewById(R.id.textView2);
        //tv1 = (TextView) findViewById(R.id.textView1);
        //tv1.setTypeface(tf1);
        //tv2.setTypeface(tf);
    }
*/



    private void NothingSelected()  {

        Toast.makeText(getApplicationContext()
                , string.toast_nothing_selected
                , Toast.LENGTH_SHORT)
                .show();
    }

    public void DisableOtherOptions(View optionSelected)  {



      //  RadioButton optionSelected = (RadioButton)view ;
        Toast.makeText(getApplicationContext()
                , "Entered method"
                , Toast.LENGTH_SHORT)
                .show();

        if(optionSelected.getId() != choice1.getId()) {
            choice1.setChecked(false);

            choice1.setSelected(false);
        }

        if(optionSelected.getId() != choice2.getId())
        choice2.setChecked(false);

        if(optionSelected.getId() != choice3.getId())
        choice3.setChecked(false);

        if(optionSelected.getId() != choice4.getId())
        choice4.setChecked(false);

    }


    public void onRadioButtonPressed(View v)    {


        boolean checked = ((RadioButton)v).isChecked() ;


        switch(v.getId())   {

            case id.option_A :     AnswerSelected = true ;
                if(getResources().getResourceEntryName(id.option_A).equals(CorrectAnswer) == true )  {

                    CorrectAnswerSelected = true ;

               }

                break ;

            case id.option_B :     AnswerSelected = true ;
                if(getResources().getResourceEntryName(id.option_B).equals(CorrectAnswer) == true )  {

                    CorrectAnswerSelected = true ;

                }

                break ;

            case id.option_C :     AnswerSelected = true ;
                if(getResources().getResourceEntryName(id.option_C).equals(CorrectAnswer) == true )  {

                    CorrectAnswerSelected = true ;

                }

                break ;

            case id.option_D :     AnswerSelected = true ;
                if(getResources().getResourceEntryName(id.option_D).equals(CorrectAnswer) == true )  {

                    CorrectAnswerSelected = true ;

                }

                break ;


        }
    }

    public void SubmitPressed(View v)   {



        if(AnswerSelected == null)
            NothingSelected();
        else {
            flipCard();
        }
    }


    public void onrestartfragment()
    {
        getFragmentManager()
                .beginTransaction()
                .add(id.container, new CardFrontFragment())
                .commit();


        // code here to show dialog
       // super.onBackPressed();  // optional depending on your needs
    }


}
