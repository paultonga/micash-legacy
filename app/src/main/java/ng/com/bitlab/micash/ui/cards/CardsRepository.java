package ng.com.bitlab.micash.ui.cards;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Bank;

/**
 * Created by paul on 1/3/18.
 */

public class CardsRepository implements CardsContract.Repository {

    FirebaseDatabase mDatabase;

    public CardsRepository() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void fetchRecords(String userUUID, final FirebaseQueryListener listener) {

        listener.onStart();
        mDatabase.getReference()
                .child("accounts")
                .child(userUUID)
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
    public void saveRecord(Bank bank, String userUUID, final FirebaseQueryListener listener) {
        listener.onStart();

        mDatabase.getReference()
                .child("accounts")
                .child(userUUID)
                .push()
                .setValue(bank, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        listener.onFinish(null, databaseError);
                    }
                });
    }

    @Override
    public void getRecord(Bank bank, String userUUID, final FirebaseQueryListener listener) {
        listener.onStart();

        mDatabase.getReference()
                .child("accounts")
                .child(userUUID)
                .orderByChild("number")
                .equalTo(bank.getNumber())
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
}
