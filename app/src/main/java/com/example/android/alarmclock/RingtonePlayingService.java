package com.example.android.alarmclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class RingtonePlayingService extends Service
{
    MediaPlayer media_song;

    int start_id;
    boolean isRunning;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LocalService", "Received start id " + startId + ": " + intent);

        // fetch the extra String value
        // state tells if its off/on
        // key is "extra"
        // what is intent.getExtras().getString?
        String state = intent.getExtras().getString("extra");



        Log.e("ringtone state:extra is", state);
//*/
        // notification
        // set up the notification service.
        //  NOTIFICATION_SERVICE vil give us the notification ??
        NotificationManager notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // set up an intent that goes to the main activity.
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

        // set up a pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this,0,intent_main_activity,0);

        //make the notification parameters.
        Notification notification_popup = new Notification.Builder(this)
                .setContentTitle("an alarm is going of!")
                .setContentText("Click me!")
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true)//when we click on it it aoutomaticly disappear
                .build();// requires API 16

        // set up notification call command
        notify_manager.notify(0,notification_popup);

//*/
        // assert state != null betyder. if it is a null pointer exeption dont go through this if else
        //assert state != null,  keeps it from crashing
        assert state != null;

        // this converts the extra strings from the intent
        // to start Id values 0 or 1.
        // switch mode:   'state' can be = "alarm on" or "alarm of"
        switch (state) {
            case "alarm on":
                startId = 1;
                break;

            case "alarm off":
                startId = 0;
                Log.e("Start ID is ",state);
                break;
            
            default:
                startId = 0;
                break;
        }

        // if else statements:

        // if there is no music playing and the user pressed alarm
        // music should start playing

        // this.is Running : maybe it means this referes to the class. if it is active isRunning = true
        if (!this.isRunning && startId==1)
        {
            Log.e("there is no music", "and you want start ");
            // create an instance of the media player
            media_song = MediaPlayer.create(this, R.raw.rain);

            //start media file:
            media_song.start();

            this.isRunning=true;
            this.start_id = 0;

        }

        // if there is music playing and the user pressed alarm off.
        // music should stop playing
        else if (this.isRunning && startId==0){
            Log.e("there is music", "and you pressed 'alarm off' ");

            //stop the ringtone
            media_song.stop();
            // reset the file.
            media_song.reset();

            this.isRunning=false;
            this.start_id= 0;

        }
        // these are if the user presses random buttons just to bug-proof the app.
        // if there is no music playing and the user presses the "alarm off"
        // do nothing
        else if (!this.isRunning && startId==0){
            Log.e("there no music", "and you pressed 'alarm off' ");

            this.isRunning=false;
            this.start_id= 0; // not needed.


        }
        // if the is music playing and the user presses "alarm on" anyway.
        // do nothing
        else if (this.isRunning && startId==1){
            Log.e("there is music", "and you pressed 'alarm on' ");

            this.isRunning=true;
            this.start_id= 1;
        }
        // just to catch odd events.
        else  {
            Log.e("else", "somehow you reached this");

        }






        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.e("Called:","destroy");
             // Tell the user we stopped.
        // super calls a method in the parent class/sperclass, here the method onDestroy()

        super.onDestroy();
        this.isRunning = false;

        //Toast.makeText(this, "on destroy called", Toast.LENGTH_SHORT).show();

    }





}
