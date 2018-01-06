package ng.com.bitlab.micash.ui.notifications;

import java.util.List;

import ng.com.bitlab.micash.models.Notification;

/**
 * Created by paul on 11/14/17.
 */

public interface NotificationsListContract {

    interface Presenter {

        void loadNotifications();

        void delete(Notification n);
    }

    interface View {

        void showEmptyLayout();

        void showNotifications(List<Notification> notifications);
    }
}
