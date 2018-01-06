package ng.com.bitlab.micash.ui.message;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Message;

/**
 * Created by paul on 1/5/18.
 */

public class ThreadRepository implements ThreadContract.Repository {

    FirebaseDatabase mDatabase;

    public ThreadRepository() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void postMessage(Message message, String uuid, final FirebaseQueryListener listener) {
        listener.onStart();
        mDatabase.getReference()
                .child("messages")
                .child(uuid)
                .push()
                .setValue(message, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        listener.onFinish(null, databaseError);
                    }
                });
    }

    @Override
    public void fetchMessages(String uuid, final FirebaseQueryListener listener) {
        listener.onStart();

        mDatabase.getReference()
                .child("messages")
                .child(uuid)
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
}
