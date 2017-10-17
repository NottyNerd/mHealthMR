package io.nottynerd.mhealthmr.notifications;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

import io.nottynerd.mhealthmr.MainActivity;
import io.nottynerd.mhealthmr.R;
import io.nottynerd.mhealthmr.Splash;


/**
 * Created by NottyNerd on 06/02/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Map data;
    RemoteMessage.Notification notification;
    String msg ="";
    String body="";
    String title="";
    String link="";
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "From: " + remoteMessage.getNotification());
        data = remoteMessage.getData();

        notification = remoteMessage.getNotification();
        // Check if message contains a data payload.
        if (data.size() > 0) {
            Log.d(TAG, "Message data payload: " + data);
            msg = ""+ data.get("push");
            body = ""+data.get("body");
            title = ""+data.get("title");
            link = ""+data.get("link");

        }

        // Check if message contains a notification payload.
        if (notification != null) {
            Log.d(TAG, "Message Notification Body: " + notification.getBody());
            body = notification.getBody();
            title = notification.getTitle();
        }


        showToast();
        sendNotification(msg, body,title);
    }
    // [END receive_message]


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String body, String title) {

        Log.d(TAG, "CURRENTLY HERE");
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.d("current task :", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClass().getSimpleName());
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if(componentInfo.getPackageName().equalsIgnoreCase("io.nottynerd.mhealthmr")){
            if(!TextUtils.isEmpty(messageBody)){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("push", messageBody);
                intent.putExtra("body", body);
                intent.putExtra("title", title);
                intent.putExtra("link",link);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        else {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("push", messageBody);
            intent.putExtra("body", body);
            intent.putExtra("title", title);
            intent.putExtra("link",link);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle().bigText(messageBody).setBigContentTitle(title);
            // Build notification
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(messageBody)
                    .setSound(defaultSoundUri)
                    .setStyle(style)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000})
                    .setLights(0xFF001042, 300, 1000);


            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }


    }

    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                Log.d(TAG, data.toString());
                title = ""+data.get("title");
                Toast.makeText(getApplicationContext(), "message received::: "+title, Toast.LENGTH_LONG).show();

            }
        });

    }
}
