package ng.com.bitlab.micash.ui.notifications;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Notif;

/**
 * Created by paul on 1/9/18.
 */



public class NotificationsListRepository implements NotificationsListContract.Repository {

    private FirebaseDatabase mDatabase;

    public NotificationsListRepository() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void fetchNotifications(String userID, final FirebaseQueryListener listener) {
        listener.onStart();
        mDatabase.getReference()
                .child("notifications")
                .child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listener.onFinish(dataSnapshot, null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onFinish(null, databaseError);
                    }
                });
    }

    @Override
    public void deleteNotification(String userID, Notif notif, final FirebaseQueryListener listener) {
        listener.onStart();
        mDatabase.getReference()
                .child("notifications")
                .child(userID)
                .orderByChild("created")
                .equalTo(notif.getCreated())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listener.onFinish(dataSnapshot, null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onFinish(null, databaseError);
                    }
                });
    }

    @Override
    public void setIsRead(final String userID, Notif notif, final FirebaseQueryListener listener) {
        listener.onStart();
        final DatabaseReference ref = mDatabase.getReference();

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
                        ref.child("notifications").child(userID).child(key).child("read").setValue(true, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                listener.onFinish(null, databaseError);
                            }
                        });

                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFinish(null, databaseError);
            }
        });


    }

}
