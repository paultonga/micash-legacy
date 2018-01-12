package ng.com.bitlab.micash.ui.guarantor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ng.com.bitlab.micash.listeners.FirebaseDataListener;
import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Guarantee;

/**
 * Created by Paul Tonga on 21/12/2017.
 */

public class GuarantorRepository implements GuarantorContract.Repository {
    @Override
    public void loadGuarantorRequests(String uuid, final FirebaseQueryListener listener) {
        listener.onStart();

        FirebaseDatabase.getInstance().getReference()
                .child("guarantees")
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

    @Override
    public void saveResponse(Guarantee guarantee, String uuid, final FirebaseQueryListener listener) {
        listener.onStart();

        FirebaseDatabase.getInstance().getReference()
                .child("guarantees")
                .child(uuid)
                .child(guarantee.getRequester_uuid())
                .setValue(guarantee, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        listener.onFinish(null, databaseError);
                    }
                });
    }
}
