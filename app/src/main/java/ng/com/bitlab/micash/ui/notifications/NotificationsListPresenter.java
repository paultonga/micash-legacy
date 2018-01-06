package ng.com.bitlab.micash.ui.notifications;

import java.util.List;

import ng.com.bitlab.micash.models.Notification;

/**
 * Created by paul on 11/14/17.
 */

public class NotificationsListPresenter implements NotificationsListContract.Presenter {

    NotificationsListContract.View mView;

    public NotificationsListPresenter(NotificationsListContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadNotifications() {

        List<Notification> notifications = Notification.listAll(Notification.class);
        if(notifications != null && notifications.size() > 0) {
            //show loans
            mView.showNotifications(notifications);
        } else{
            //show empty
            mView.showEmptyLayout();
        }
    }

    @Override
    public void delete(Notification n) {
        n.delete();
        loadNotifications();
    }
}
