package ng.com.bitlab.micash.listeners;

import ng.com.bitlab.micash.models.Notif;

/**
 * Created by Paul Tonga on 17/01/2018.
 */

public interface NotificationReceivedListener {
    void onItemReceived(Notif notif);
}
