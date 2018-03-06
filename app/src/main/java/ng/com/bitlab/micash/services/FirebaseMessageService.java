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

import org.joda.time.DateTime;

import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.events.NotificationEvent;
import ng.com.bitlab.micash.listeners.NotificationReceivedListener;
import ng.com.bitlab.micash.models.Notif;
import ng.com.bitlab.micash.ui.guarantor.GuarantorActivity;
import ng.com.bitlab.micash.ui.message.ThreadActivity;

import static ng.com.bitlab.micash.core.MiCashApplication.bus;

/**
 * Created by paul on 11/11/17.
 */

public class FirebaseMessageService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Message", "Message received ["+ remoteMessage +"]");
        startService(new Intent(this, NotificationService.class));

    }
}
