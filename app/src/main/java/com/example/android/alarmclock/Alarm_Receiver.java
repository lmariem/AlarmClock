package com.example.android.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *  *  * Created by martin on 2/22/16.
 */
public class Alarm_Receiver extends BroadcastReceiver
{


    @Override
    public void onReceive(Context context, Intent intent)
    {
        // will write this message in the terminal.
        Log.e("hi","it works the receiver");

        // fetch extra string from the intent.
        // the key value is extra.
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("what is the key?",get_your_string);



        // create an intent to the ringtone service
        // the intent is is send to the RingtonePlayingService class
        Intent service_intent = new Intent(context, RingtonePlayingService.class );

        // pass the extra string from main Activity to the ringtone playing Service
        service_intent.putExtra("extra",get_your_string);


        //start the ringtone service
        context.startService(service_intent);



    }
}
