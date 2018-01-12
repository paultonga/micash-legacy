package ng.com.bitlab.micash.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.events.NotificationEvent;
import ng.com.bitlab.micash.models.Notif;
import ng.com.bitlab.micash.ui.guarantor.GuarantorActivity;
import ng.com.bitlab.micash.ui.message.ThreadActivity;

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

        //storeNotification(title, detail);
        saveNotif(remoteMessage);
        //showNotification(remoteMessage);
        //broadcastIntent();

    }

    private void broadcastIntent() {

        Intent intent = new Intent();
        intent.setAction("com.ng.bitlab.micash.CUSTOM_EVENT");
        LocalBroadcastManager.getInstance(MiCashApplication.getContext())
                .sendBroadcast(intent);

    }

    private void storeNotification(String title, String detail){


    }

    private Notif getNotif(String  title, String description){
        return  new Notif(title, description,
                DateTime.now().getMillis(), false);
    }

    public void publishNotif(){

        EventBus.getDefault().post(new NotificationEvent());
    }

    private void saveNotif(final RemoteMessage rm){
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Notif notif = new Notif(rm.getNotification().getTitle(), rm.getNotification().getBody(),
                DateTime.now().getMillis(), false);
        FirebaseDatabase.getInstance().getReference()
                .child("notifications")
                .child(userID)
                .push()
                .setValue(notif, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null){
                            //publishNotif();
                            broadcastIntent();
                            //showNotification(rm);
                        }else {
                            Log.d("Saving Notif", databaseError.getMessage());
                        }
                    }
                });
    }

    private void showNotification(RemoteMessage remoteMessage){
        Intent intent = new Intent(MiCashApplication.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic)
                .setContentTitle("miCash - "+remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }

    private Intent getUserIntent(String identifier) {

        switch (identifier){
            case "":
                return new Intent(MiCashApplication.getContext(), MainActivity.class);
            case "welcome":
                return new Intent(MiCashApplication.getContext(), MainActivity.class);
            case "guarantee":
                return new Intent(MiCashApplication.getContext(), GuarantorActivity.class);
            case "message":
                return new Intent(MiCashApplication.getContext(), ThreadActivity.class);
            default:
                return new Intent(MiCashApplication.getContext(), MainActivity.class);
        }
    }
}
