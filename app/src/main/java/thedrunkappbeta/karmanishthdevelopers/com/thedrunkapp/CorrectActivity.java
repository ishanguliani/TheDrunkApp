package thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.os.Handler;
import android.widget.Toast;


public class CorrectActivity extends Activity {


    public static int PenaltyScored = 0 ;
    public static int PenaltyScoredMax = 0 ;
    public static int MyScore = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

           /*MAKE THE ACTIVITY FULL SCREEN*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if(getIntent().getIntExtra("my_app" , 99) == 0)    {
            setContentView(R.layout.activity_incorrect);

            PenaltyScored += getIntent().getIntExtra("my_penalty", -9999) ;

        }


        //check if correct or incorrect based on the value received from the intent
        else if(getIntent().getIntExtra("my_app" , 99) == 1) {
            setContentView(R.layout.activity_correct);

            MyScore += getIntent().getIntExtra("my_penalty_correct" , -9999) ;
            // PenaltyScoredMax += getIntent().getIntExtra("my_penalty_correct" , -9999) ;

        }


        //if time ran out
        else if(getIntent().getIntExtra("my_app" , 99) == 2) {
            setContentView(R.layout.activity_timeover);

            // PenaltyScoredMax += getIntent().getIntExtra("my_penalty_correct" , -9999) ;

        }






        Runnable mMyRunnablenew = new Runnable()
        {
            @Override
            public void run()
            {
                // onrestartfragment();                  //Change state here
                Intent intent = new Intent(CorrectActivity.this, MainActivityFresh.class) ;
                startActivity(intent) ;

            }
        };


        Handler myHandler = new Handler();
        myHandler.postDelayed(mMyRunnablenew, 2000);//Message will be delivered in 2 seconds.

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.correct, menu);
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



    @Override
    public void onBackPressed() {

        //do nothing
    }



    public int getPenaltyScored()   {
        return PenaltyScored ;
    }
}
