/*
 * Copyright (C) 2014 Marc Lester Tan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunildhaker.watch.heart;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.CircledImageView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.mariux.teleport.lib.TeleportClient;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectActivity extends Activity {

    private static final String TAG = ConnectActivity.class.getName();

    String spokenText = "";
    boolean gotText = false;
    CircledImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btn = (CircledImageView) findViewById(R.id.btnConnect);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gotText)
                    displaySpeechRecognizer();
                else {

                    Timer t = new Timer();
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            showNotification();
                        }
                    };
                    t.schedule(tt, 5000);

                   System.exit(0);
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null && intent.getExtras().containsKey("keep")) {
            boolean keep = intent.getExtras().getBoolean("keep");
            if (!keep) {
                finish();
            }
        }
    }

    //Task that shows the path of a received message
    public class ShowToastFromOnGetMessageTask extends TeleportClient.OnGetMessageTask {

        @Override
        protected void onPostExecute(String path) {


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            spokenText = results.get(0);
            gotText = true;
            btn.setImageDrawable(getResources().getDrawable(R.drawable.send));
            // Do something with spokenText
            Toast.makeText(this, spokenText, Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "Didn't caught anything :(", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void showNotification() {
        // Build an intent for an action to view a map
        Intent select = new Intent(this, SelectActivity.class);
        Intent viewIntent = new Intent(this, WearActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

//        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
//        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, select, 0);
//
//        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
//        bigStyle.bigText("Do you wish to connect to somebody ?");
//        bigStyle.setBigContentTitle("You seem troubled.");


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.conversations_blue)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(), R.drawable.background_image))
                        .setContentTitle("Preeti")
                        .setContentText("You seem troubled.")
                        .setContentIntent(viewPendingIntent)
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine("You seem troubled")
                                .addLine("Do you wish to")
                                .addLine("connect to somebody?"))

                                //.setStyle(bigStyle)
                        .addAction(R.drawable.right_grey, "Get Help", mapPendingIntent);


        Notification notif = notificationBuilder.build();
        // notificationBuilder.notify();
        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Issue the notification with notification manager.
        notificationManager.notify(5, notif);
    }

}
