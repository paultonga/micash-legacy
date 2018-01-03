package ng.com.bitlab.micash.ui.guarantor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ng.com.bitlab.micash.listeners.FirebaseDataListener;

/**
 * Created by Paul Tonga on 21/12/2017.
 */

public class GuarantorRepository implements GuarantorContract.Repository {
    @Override
    public void loadGuarantorRequests(String uuid, final FirebaseDataListener listener) {
        listener.onStart();

        FirebaseDatabase.getInstance().getReference()
                .child("guarantees")
                .child(uuid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listener.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onFailed(databaseError);
                    }
                });
    }
}
