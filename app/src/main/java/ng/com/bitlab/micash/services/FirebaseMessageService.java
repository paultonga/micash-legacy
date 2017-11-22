package ng.com.bitlab.micash.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.listeners.OnNotificationReceivedListener;
import ng.com.bitlab.micash.models.Notification;

/**
 * Created by paul on 11/11/17.
 */

public class FirebaseMessageService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Message", "Message received ["+ remoteMessage +"]");
        String title = remoteMessage.getNotification().getTitle();
        String detail = remoteMessage.getNotification().getBody();

        storeNotification(title, detail);
        showNotification(remoteMessage);
        broadcastIntent();

    }

    private void broadcastIntent() {

        Intent intent = new Intent();
        intent.setAction("com.ng.bitlab.micash.CUSTOM_EVENT");
        LocalBroadcastManager.getInstance(MiCashApplication.getContext())
                .sendBroadcast(intent);

    }

    private void storeNotification(String title, String detail){

        Notification notification = new Notification(title,
                detail,
                org.joda.time.DateTime.now().getMillis(),
                false,
                "welcome");
        notification.save();

    }

    private void showNotification(RemoteMessage remoteMessage){
        //Create Notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic)
                .setContentTitle("miCash")
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }
}
