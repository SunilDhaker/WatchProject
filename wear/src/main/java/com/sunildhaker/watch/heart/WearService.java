package com.sunildhaker.watch.heart;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.mariux.teleport.lib.TeleportService;

import java.util.Timer;
import java.util.TimerTask;

public class WearService extends TeleportService {

    private static final String TAG = WearService.class.getName();

    // Teleport config
    public static final String START_ACTIVITY = "startActivity";
    public static final String STOP_ACTIVITY = "stopActivity";

    @Override
    public void onCreate() {
        super.onCreate();
        setOnGetMessageTask(new ActivityManagementTask());

    }

    //Task that shows the path of a received message
    public class ActivityManagementTask extends OnGetMessageTask {


        @Override
        protected void onPostExecute(String  path) {

            showNotification();
//            if (path.equals(START_ACTIVITY)){
//                Intent startIntent = new Intent(getBaseContext(), WearActivity.class);
//                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startIntent.putExtra("keep", true);
//                startActivity(startIntent);
//            } else if (path.equals(STOP_ACTIVITY)) {
//                Intent stopIntent = new Intent(getBaseContext(), WearActivity.class);
//                stopIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                stopIntent.putExtra("keep", false);
//                startActivity(stopIntent);
//            } else {
//                Log.d(TAG, "Got a message with path: " + path);
//            }
            //let's reset the task (otherwise it will be executed only once)
            setOnGetMessageTask(new ActivityManagementTask());
        }
    }


    public void showNotification(){
        // Build an intent for an action to view a map
        Intent select = new Intent(this , SelectActivity.class);
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