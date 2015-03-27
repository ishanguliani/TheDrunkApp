package thedrunkappbeta.karmanishthdevelopers.com.thedrunkapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class mReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);

//                        RECEIVE THE MESSAGE TYPE
        // Filter messages based on message type. It is likely that GCM will be extended in the future
        // with new message types, so just ignore message types you're not interested in, or that you
        // don't recognize.
        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            // It's an error.
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            // Deleted messages on the server.
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            // It's a regular GCM message, do some work.
        }


    }

    /*private final String TAG = "Receiver1";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "INTENT RECEIVED");

//        Vibrator v = (Vibrator) context
//                .getSystemService(Context.VIBRATOR_SERVICE);
//        v.vibrate(500);

        Toast.makeText(context, "INTENT RECEIVED by Receiver1", Toast.LENGTH_LONG).show();
    }*/

}