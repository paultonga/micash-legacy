package ng.com.bitlab.micash.ui.notifications;

import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Notif;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by paul on 11/14/17.
 */

public interface NotificationsListContract {

    interface Presenter extends IBasePresenter<View> {

        void loadNotifications();

        void delete(Notif n);

        void clearAll();

        void setIsRead(Notif notif);
    }

    interface View extends IBaseVew {

        void showEmptyLayout();

        void showLoadingLayout();

        void showNotifications(List<Notif> notifications);

        void onNotificationReceived();

        void onClearAllClicked();

        void showClearAllWarning();
    }

    interface Repository {

        void fetchNotifications (String userID, FirebaseQueryListener listener);

        void deleteNotification(String userID, Notif notif, FirebaseQueryListener listener);

        void setIsRead(String userID, Notif notif, FirebaseQueryListener listener);

    }
}
