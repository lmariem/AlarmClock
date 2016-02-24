package com.example.android.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // to make our alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    // what dose context do?
    Context context;
    PendingIntent pending_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //"this" is used because the Activity class is a subclass of Context ? tror jeg.
        this.context = this; // hvad gør den her? det ved hun ikke i videoen..


        //initiallize our alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initiallize our timepicker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        //initiallize our update textbox
        update_text = (TextView) findViewById(R.id.update_text);

        // create an instance of a calender. why final?
        final Calendar calendar = Calendar.getInstance();

        // create an intent to the alarm Receiver class
        // What is an intent?? ts most significant use is in the launching of activities, where it
        // can be thought of as the glue between activities
        // my_intent is an intent that talks with the alarm_Receiver class.

        // the intent is send to the Alarm_Receiver class
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        // initialize start button
        Button alarm_on = (Button) findViewById(R.id.alarm_on);



        // create onClick listener to start the alarm
        alarm_on.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // call method that changes the update text box
                set_alarm_text("Alarm on!"); // (create methodes automaticly by alt + enter -> create method -> main activity)


            }
        });



        // initialize off button
        Button alarm_off = (Button) findViewById(R.id.alarm_off);


        // when the button is clicked the alarm is canceled.
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                set_alarm_text("Alarm is canceled:) ");

                //cancel the alarm
                alarm_manager.cancel(pending_intent);

                // put extra string into my_intent
                // tells the clock that you pressed the 'alarm off' button
                my_intent.putExtra("extra","alarm off");

                // stop the ringtone
                // this sends a "signal" to the alarm receiver class. witch sends a "signal" to the RingtonePlayingService class
                sendBroadcast(my_intent);


                String TAG = "alarmLog";

                // just a mesage in the terminal to check if stuff is working
                Log.e(TAG, "onClick : alarm canceled ");
            }
        });

        // create onClick listener to turn off the alarm

        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // setting calendar instance with the hour and minutes that we picked.
                // on the time picker.
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                // get the int value of the hour and the minute.
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                // convert the int values to strings
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                // convert 24-hour time to 12 hour time.
                if (hour> 12)
                {
                    hour_string = String.valueOf(hour-12); // covereting integer to string.
                }
                if(minute<10)
                {
                    // 10:7 --> 10:07
                    minute_string = "0" + String.valueOf(minute);
                }

                //call the method that tell that the alarm is off.
                set_alarm_text("Alarm set to " + hour_string+":" + minute_string);

                //put in extra string into my_intent
                // tells the clock that you pressed the alarm button

                my_intent.putExtra("extra","alarm on");


                // create a pending intent that delays the intent
                // until the specified calendar time.
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // set the alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

            }
        });


    }

    // sets the texts the is being updated
    private void set_alarm_text(String output)
    {
        // we pass it the string "Alarm on!" and nam it output.
        //output= "Alarm on!"
        update_text.setText(output);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
