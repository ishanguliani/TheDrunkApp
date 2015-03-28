package thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends Activity {

    ImageView mImageView = null ;

    //EDIT
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    //this is a test comment added on 29th March 2015

    //EDIT - constants
    public static final String EXTRA_MESSAGE = "message";
    public static String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "608350588885";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "gcm";

    //EDIT - variable to store the registration id received by the GCM Server
    String finalid = null ;

    //EDIT - more constants
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;



    //  Animation mAnimation = null ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


                         /*MAKE THE ACTIVITY FULL SCREEN*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_home_screen);

                        /*ANIMATED HOME SCREEN SNIPPET*/

        //EDIT - get the application context
        context = getApplicationContext();


        //EDIT START
//                        REGISTER TO RECEIVE BROADCAST MESSAGES FROM GOOGLE CLOUD MESSAGING SERVER
        // Check device for Play Services APK.
        try {

            if (checkPlayServices()) {
                // If this check succeeds, proceed with normal processing.
                // Otherwise, prompt user to get valid Play Services APK.
                gcm = GoogleCloudMessaging.getInstance(this);
                regid = getRegistrationId(context);
                finalid=regid ;

                if (regid.isEmpty()) {
                    registerInBackground();

                }

            } else {
                Log.e("==========================", "=========================");
                Log.e("regid", regid);
                Log.e("==========================", "=========================");
                Log.i("==========================", "=========================");
                Log.i("mygcm", regid);
                Log.i("==========================", "=========================");
            }
        }catch(Exception ex){
        Log.i(TAG,"Play Services Error");

    }



        //EDIT END

        //  mAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.animn2);
                        //   final Button btn = (Button) findViewById(R.id.button);

                        final TextView txtview = (TextView) findViewById(R.id.textView2);
                        txtview.setText(R.string.firstscreen_hi);
                        txtview.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.animn));



                                            final TextView txtview1 = (TextView) findViewById(R.id.textView3);
                                            txtview1.setText(R.string.firstscreen_letstake);
                                            txtview1.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.animn1));



                final Button button = (Button) findViewById(R.id.button_start);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        method_start_playing(v);


                    }
                });



        // button.setText(R.string.firstscreen_startonlemon);
        button.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.animn2));



        /* LINK THE LEMON IMAGE TO IMAGEVIEW OBJECT THEN START ANIMATION */

    mImageView = (ImageView)findViewById(R.id.image_lemon) ;

            //EDIT -- send the id via share intent
        /*mImageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent() ;
                intent.setAction(Intent.ACTION_SEND) ;
                intent.putExtra(Intent.EXTRA_TEXT, "id is : " + finalid) ;
                intent.setType("text/plain") ;
                startActivity(intent);


            }
        });*/
     mImageView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             method_start_playing(v) ;


         }
     });


        //start animation
        mImageView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_lemon_rotate));

    }



    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
        try {
            checkPlayServices();
        }catch(Exception ex) {
            Log.i(TAG,"Play Services Error");
        }
    }



    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
//  Added to Documentation
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
//    Added to Documentation
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
//    Added to Documentation
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    //START ASYNCTASK METHOD

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
   /* private void registerInBackground() {
        new AsyncTask() {
            *//*@Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }*//*

            @Override
            protected String doInBackground(Void... params) {

                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                mDisplay.append(msg + "\n");
                Toast.makeText(context,msg+'\n',Toast.LENGTH_SHORT);
            }


        }.execute(null, null, null);

    }*/



    private void registerInBackground() {
        new AsyncTask() {




            @Override
            protected String doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i(TAG + "regid is " ,msg);

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, regid);

                }catch (IOException ex)   {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return msg;
            }


            @Override
            protected void onPostExecute(Object m) {
//                super.onPostExecute(m);
//                mDisplay.append(msg + "\n");
                Toast.makeText(context,m.toString()+'\n',Toast.LENGTH_LONG).show();
                //EDIT - set the final global reg id variable
                finalid=m.toString();

            }

        }.execute(null, null, null);

    }
    //END ASYNCTASK METHOD

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public void onClick(final View view) {
        if (view == findViewById(R.id.textView2)) {
            new AsyncTask() {


                @Override
                protected void onPostExecute(Object m) {
                    Toast.makeText(context,m.toString()+'\n',Toast.LENGTH_SHORT);
                }

                @Override
                protected String doInBackground(Object[] params) {

                    String msg = "";
                    try {
                        Bundle data = new Bundle();
                        data.putString("my_message", "Hello World");
                        data.putString("my_action",
                                "com.google.android.gcm.demo.app.ECHO_NOW");
                        String id = Integer.toString(msgId.incrementAndGet());
                        gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
                        msg = "Sent message";
                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                    }
                    return msg;
                }
            }.execute(null, null, null);
        } else if (view == findViewById(R.id.textView3)) {
            Toast.makeText(context,"ALL CLEARED",Toast.LENGTH_SHORT);
//            mDisplay.setText("");
        }
    }



    private void method_start_playing(View v)    {

        Toast.makeText(getApplicationContext(), "Let's do this!" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainActivityFresh.class) ;
        intent.putExtra("fresh_call", 99) ;
        startActivity(intent) ;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
