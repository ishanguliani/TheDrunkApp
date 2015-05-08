package thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FinalScoreActivity extends Activity {
    private double DrunkPercentage = 50 ;
    private int final_score ;
    private int final_score_max = 800 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         /*MAKE THE ACTIVITY FULL SCREEN*/
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/

        setContentView(R.layout.activity_final_score);

        //final_score = getIntent().getIntExtra("final_score" , -9999) ;
        final_score = MainActivityFresh.NetScore ;
        Log.i("my_app", "Final Score is : " + final_score) ;

        // final_score_max = getIntent().getIntExtra("final_score_max" , -9999) ;
        //  final_score_max+=final_score ;
        Log.i("my_app", "Final Score Max is : " + final_score_max) ;


        DrunkPercentage = ((double)(800-final_score)/(double)final_score_max)*100.00;
        DrunkPercentage = (double)Math.round(DrunkPercentage * 100) / 100 ;

        if(DrunkPercentage <= 25.00 )   {

        }
        else    {

        }


        //animation add
        TextView txtview = (TextView) findViewById(R.id.textView);
        txtview.setText(R.string.acc_to_john);
        txtview.startAnimation(AnimationUtils.loadAnimation(FinalScoreActivity.this, R.anim.animate));

       /* TextView txtview1 = (TextView) findViewById(R.id.textView1);
        txtview1.setText(R.string.you_are);
        txtview1.startAnimation(AnimationUtils.loadAnimation(FinalScoreActivity.this, R.anim.your_drunk));
*/

        TextView txtview2 = (TextView) findViewById(R.id.textView2);
        txtview2.setText("You Are " + DrunkPercentage + "% Drunk!");
        txtview2.startAnimation(AnimationUtils.loadAnimation(FinalScoreActivity.this, R.anim.percentage));

/*
        final TextView txtview3 = (TextView) findViewById(R.id.textView3);
        txtview3.setText(R.string.drunk);
        txtview3.startAnimation(AnimationUtils.loadAnimation(FinalScoreActivity.this, R.anim.your_drunk));
*/

        ImageView share = (ImageView)findViewById(R.id.button_share) ;
        ImageView replay = (ImageView)findViewById(R.id.button_replay) ;

        share.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent() ;
                intent.setAction(Intent.ACTION_SEND) ;
                intent.putExtra(Intent.EXTRA_TEXT, "Hey ! Have you tried 'The Drunk App' yet? I am " + DrunkPercentage + "% Drunk right now ! Let's see if you're sober than me. https://play.google.com/store/apps/details?id=thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp") ;
                intent.setType("text/plain") ;
                startActivity(intent);


            }
        });

        replay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FinalScoreActivity.this , MainActivityFresh.class) ;
                //intent.setAction(Intent.ACTION_SEND) ;
                //intent.putExtra(Intent.EXTRA_TEXT, "Hey ! Have you tried 'The Drunk App' yet? I am " + DrunkPercentage + "% Drunk right now ! Let's see if you're sober than me. ")
                //intent.setType("text/plain") ;
                intent.putExtra("replay_activity" , 555) ;

                Toast.makeText(getApplicationContext()

                        , "Let's do this again Drunky !"
                        , Toast.LENGTH_SHORT).show();


                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.final_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // if (id == R.id.action_rate_us)

        switch(id) {


            case R.id.action_rate_us: {

                //open the play store to rate the app
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
                return true;
            }


            case R.id.action_share_app : {


                Intent intent = new Intent() ;
                intent.setAction(Intent.ACTION_SEND) ;
                intent.putExtra(Intent.EXTRA_TEXT, "Hey ! Have you tried 'The Drunk App' yet? I am " + DrunkPercentage + "% Drunk right now ! Let's see if you're sober than me. https://play.google.com/store/apps/details?id=thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp") ;
                intent.setType("text/plain") ;
                startActivity(intent);


            }


        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        //do nothing
    }
}
