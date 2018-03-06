package ng.com.bitlab.micash.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.events.NotificationEvent;
import ng.com.bitlab.micash.models.Notif;

import static ng.com.bitlab.micash.core.MiCashApplication.bus;

/**
 * Created by Paul Tonga on 19/01/2018.
 */

public class NotificationService extends Service {

    AppPreference pref;
    private static final String TAG = "NotificationService";
    private static final String NOTIFICATION_EVENT  = "com.ng.bitlab.micash.CUSTOM_EVENT";
    private static final String NOTIFICATION_COUNT  = "com.ng.bitlab.micash.CUSTOM_COUNT";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bus.register(this);
        pref = MiCashApplication.getPreference();

        if(pref.getUUID() != null) {
            Log.d(TAG, pref.getUUID());

            //check for new notifications
            checkNotifications();

            //check for new messages
            checkMessages();
        }
    }

    private void checkMessages(){
        FirebaseDatabase.getInstance()
                .getReference("messages")
                .child(pref.getUUID())
                .orderByChild("seen")
                .equalTo(false)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            long count = dataSnapshot.getChildrenCount();
                            postMessageNotifications(count);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void postMessageNotifications(long count){
        if(count > 0){
            String body = count == 1 ? "You have 1 unread message" : "You have "+count+" unread messages";
            String title = count == 1 ? "New message" : "New messages";
            pref.setMessageCount(count);

            Intent intent = new Intent(MiCashApplication.getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                    intent, PendingIntent.FLAG_ONE_SHOT);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                    NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());


        }
    }

    private void checkNotifications(){
        FirebaseDatabase.getInstance()
                .getReference("notifications")
                .child(pref.getUUID())
                .orderByChild("open")
                .equalTo(false)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            List<Notif> notifs = new ArrayList<>();
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Notif n = ds.getValue(Notif.class);
                                notifs.add(n);
                            }
                            postNotification(notifs);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
    }

    private void broadcastIntent(int count) {

        Intent intent = new Intent(NOTIFICATION_EVENT);
        intent.putExtra(NOTIFICATION_COUNT, count);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);

    }

    private void postNotification(List<Notif> notifs){
        if(notifs != null && !notifs.isEmpty()) {

            broadcastIntent(notifs.size());
            //build notification
            Intent intent = new Intent(MiCashApplication.getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                    intent, PendingIntent.FLAG_ONE_SHOT);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            for (Notif n : notifs) {
                NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                        NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic)
                        .setContentTitle(n.getTitle())
                        .setContentText(n.getDescription())
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(uniqueInt(), notificationBuilder.build());
                setNotificationOpened(n);

            }
        }

    }

    private int uniqueInt(){
        return (int)((new Date().getTime() /1000L) % Integer.MAX_VALUE);
    }

    private void setNotificationOpened(Notif notif){
        final  String userID = pref.getUUID();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("notifications")
                .child(userID)
                .orderByChild("created")
                .equalTo(notif.getCreated())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            DataSnapshot ds = dataSnapshot.getChildren().iterator().next();
                            String key = ds.getKey();
                            ref.child("notifications")
                                    .child(userID)
                                    .child(key)
                                    .child("open")
                                    .setValue(true, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                }
                            });

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                });


    }


}
