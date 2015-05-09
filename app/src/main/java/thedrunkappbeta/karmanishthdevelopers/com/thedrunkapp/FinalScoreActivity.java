package thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        setContentView(R.layout.activity_final_score);

        final_score = MainActivityFresh.NetScore ;

        TextView txtview = (TextView) findViewById(R.id.textView);
        TextView txtview2 = (TextView) findViewById(R.id.textView2);

        DrunkPercentage = ((double)(800-final_score)/(double)final_score_max)*100.00;
        DrunkPercentage = (double)Math.round(DrunkPercentage * 100) / 100 ;


        if(DrunkPercentage <= 40.00 )   {
            txtview2.setTextColor(getResources().getColor(R.color.greencolorscore));
        }
        else if(DrunkPercentage > 40.00 && DrunkPercentage <= 70.00)   {
            txtview2.setTextColor(getResources().getColor(R.color.bluecolorsore));        }
        else if(DrunkPercentage > 70.00)    {
            txtview2.setTextColor(getResources().getColor(R.color.redcolorscore));        }


        //animation add

        txtview.setText(R.string.acc_to_john);
        txtview.startAnimation(AnimationUtils.loadAnimation(FinalScoreActivity.this, R.anim.animate));
        txtview2.setText("You Are " + DrunkPercentage + "% Drunk!");
        txtview2.startAnimation(AnimationUtils.loadAnimation(FinalScoreActivity.this, R.anim.percentage));
        ImageView share = (ImageView)findViewById(R.id.button_share) ;
        ImageView replay = (ImageView)findViewById(R.id.button_replay) ;

        share.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent() ;
                intent.setAction(Intent.ACTION_SEND) ;
                intent.putExtra(Intent.EXTRA_TEXT, "Hey ! Have you tried 'The Drunk App v2.0' yet? Love the newest gamified challenges ! I am " + DrunkPercentage + "% Drunk right now ! Let's see if you're sober than me. https://play.google.com/store/apps/details?id=thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp") ;
                intent.setType("text/plain") ;
                startActivity(intent);
            }
        });
        replay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalScoreActivity.this , MainActivityFresh.class) ;
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
