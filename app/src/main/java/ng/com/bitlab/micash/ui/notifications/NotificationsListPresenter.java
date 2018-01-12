package ng.com.bitlab.micash.ui.notifications;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Notif;
import ng.com.bitlab.micash.ui.common.BasePresenter;

/**
 * Created by paul on 11/14/17.
 */

public class NotificationsListPresenter extends BasePresenter<NotificationsListContract.View> implements NotificationsListContract.Presenter {

    NotificationsListContract.View mView;
    NotificationsListContract.Repository mRepository;

    public NotificationsListPresenter(NotificationsListContract.View mView) {
        this.mView = mView;
        this.mRepository = new NotificationsListRepository();
    }

    @Override
    public void loadNotifications() {
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRepository.fetchNotifications(uuid, new FirebaseQueryListener() {
            @Override
            public void onStart() {
                mView.showLoadingLayout();
            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                if(databaseError == null){
                    if(dataSnapshot.exists()){
                        List<Notif> notifs = new ArrayList<>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Notif n = ds.getValue(Notif.class);
                            notifs.add(n);
                        }
                        mView.showNotifications(notifs);
                    }else {
                        mView.showEmptyLayout();
                    }
                }else {
                    mView.showEmptyLayout();
                    mView.showToast(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void setIsRead(Notif notif) {
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRepository.setIsRead(uuid, notif, new FirebaseQueryListener() {
            @Override
            public void onStart() {
                //nothing to do
            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                if (databaseError != null){
                    mView.showToast(databaseError.getMessage());
                }
            }
        });

    }

    @Override
    public void delete(Notif n) {
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRepository.deleteNotification(uuid, n, new FirebaseQueryListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                if(databaseError == null){
                    if(dataSnapshot.exists()){
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        loadNotifications();
                    }
                } else {
                    mView.showToast(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void clearAll() {
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRepository.fetchNotifications(uuid, new FirebaseQueryListener() {
            @Override
            public void onStart() {
                mView.showDialog("Processing...");
            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                if(databaseError == null){
                    loadNotifications();
                } else {
                    mView.showEmptyLayout();
                    mView.showToast(databaseError.getMessage());
                }
            }
        });
    }
}
