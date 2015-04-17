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
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.mariux.teleport.lib.TeleportClient;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class WearActivity extends Activity implements SensorEventListener{

    private static final String TAG = WearActivity.class.getName();

    private TextView rate;
    private TextView accuracy;
    private TextView sensorInformation;
    private static final int SENSOR_TYPE_HEARTRATE = 65562;
    private Sensor mHeartRateSensor;
    private SensorManager mSensorManager;
    private CountDownLatch latch;

    TeleportClient mTeleportClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = this.getIntent();
        if(intent != null && intent.getExtras() != null && intent.getExtras().containsKey("keep")){
            boolean keep = intent.getExtras().getBoolean("keep");
            if(!keep)
            {
                finish();
                //startactivity only code goes here
            }
        }
        //instantiate the TeleportClient with the application Context
        mTeleportClient = new TeleportClient(this);
        //let's set the two task to be executed when an item is synced or a message is received
        mTeleportClient.setOnGetMessageTask(new ShowToastFromOnGetMessageTask());
//        latch = new CountDownLatch(1);
//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//                rate = (TextView) stub.findViewById(R.id.rate);
//                rate.setText("Reading...");
//
//                accuracy = (TextView) stub.findViewById(R.id.accuracy);
//                sensorInformation = (TextView) stub.findViewById(R.id.sensor);
//
//                latch.countDown();
//            }
//        });

        mSensorManager = ((SensorManager)getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(SENSOR_TYPE_HEARTRATE); // using Sensor Lib2 (Samsung Gear Live)
        //mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE); // using Sensor Lib (Samsung Gear Live)
        if(mHeartRateSensor == null)
            Log.d(TAG, "heart rate sensor is null");


    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        if(intent.getExtras() != null && intent.getExtras().containsKey("keep")) {
            boolean keep = intent.getExtras().getBoolean("keep");
            if (!keep) {
                finish();
            }
        }
    }

    //Task that shows the path of a received message
    public class ShowToastFromOnGetMessageTask extends TeleportClient.OnGetMessageTask {

        @Override
        protected void onPostExecute(String  path) {

            //Toast.makeText(getApplicationContext(), "Message - " + path, Toast.LENGTH_SHORT).show();
            //let's reset the task (otherwise it will be executed only once)
            mTeleportClient.setOnGetMessageTask(new ShowToastFromOnGetMessageTask());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mTeleportClient != null)
            mTeleportClient.connect();
        mSensorManager.registerListener(this, this.mHeartRateSensor, 3);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        try {
////            latch.await();
////            if(sensorEvent.values[0] > 0){
////                mTeleportClient.sendMessage(String.valueOf(sensorEvent.values[0]), null);
////                Log.d(TAG, "sensor event: " + sensorEvent.accuracy + " = " + sensorEvent.values[0]);
////                //rate.setText(String.valueOf(sensorEvent.values[0]));
////                //accuracy.setText("Accuracy: "+sensorEvent.accuracy);
////                //sensorInformation.setText(sensorEvent.sensor.toString());
//            }
//
//        } catch (InterruptedException e) {
//            Log.e(TAG, e.getMessage(), e);
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

//        Log.d(TAG, "accuracy changed: " + i);
//        accuracy.setText("Accuracy: " + Integer.toString(i));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mTeleportClient != null)
         mTeleportClient.disconnect();
         mSensorManager.unregisterListener(this);
    }


    public void showNotification(){
        // Build an intent for an action to view a map
        Intent mapIntent = new Intent(this , SelectActivity.class);
        Intent viewIntent = new Intent(this, WearActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

//        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
//        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.eureka_blue_fill)
                        .setContentTitle("Dummy Notif")
                        .setContentText("Some dummy text here ")
                        .setContentIntent(viewPendingIntent)
                        .addAction(R.drawable.eureka_blue_fill,
                                "Get Help", mapPendingIntent);

        Notification notif = notificationBuilder.build();

       // notificationBuilder.notify();
        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Issue the notification with notification manager.
        notificationManager.notify(5, notif);
    }


}
