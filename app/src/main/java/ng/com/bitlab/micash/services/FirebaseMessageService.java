package ng.com.bitlab.micash.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.listeners.OnNotificationReceivedListener;
import ng.com.bitlab.micash.models.Notification;

/**
 * Created by paul on 11/11/17.
 */

public class FirebaseMessageService extends FirebaseMessagingService {

    private static OnNotificationReceivedListener mListener;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Message", "Message received ["+ remoteMessage +"]");
        storeNotification("",remoteMessage.toString());
        showNotification(remoteMessage);

    }

    private void storeNotification(String title, String detail){
        Notification n = new Notification();

        n.save();
        mListener.OnNotificationReceived("Welcome to miCash","Please add your details to enable you request loans.");

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
