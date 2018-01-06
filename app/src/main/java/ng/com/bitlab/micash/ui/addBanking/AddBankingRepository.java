package ng.com.bitlab.micash.ui.addBanking;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ng.com.bitlab.micash.listeners.FirebaseDataListener;
import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Bank;
import ng.com.bitlab.micash.models.Guarantee;
import ng.com.bitlab.micash.models.Guarantor;
import ng.com.bitlab.micash.models.Request;

/**
 * Created by paul on 12/16/17.
 */

public class AddBankingRepository implements AddBankingContract.Repository {


    @Override
    public void getGuarantor(String emailMD5, final FirebaseDataListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference()
                .child("tokens")
                .child(emailMD5)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public void sendGuarantorRequest(Guarantor guarantor, Guarantee guarantee, final FirebaseDataListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference()
                .child("guarantees")
                .child(guarantor.getUuid())
                .child(guarantee.getRequester_uuid())
                .setValue(guarantee, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        listener.onComplete(databaseError);
                    }
                });
    }

    @Override
    public void getGuarantorCount(String guarantor_uuid, final FirebaseDataListener listener) {
        listener.onStart();

        FirebaseDatabase.getInstance().getReference()
                .child("guarantee").child(guarantor_uuid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public void checkUserLoan(String userID, final FirebaseDataListener listener) {
        listener.onStart();

        FirebaseDatabase.getInstance().getReference()
                .child("requests")
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public void checkGuarantorEmail(String emailMD5, final FirebaseDataListener listener) {
        listener.onStart();

        FirebaseDatabase.getInstance().getReference()
                .child("checkEmail")
                .child(emailMD5)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public void getBankAccounts(String uuid, final FirebaseQueryListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference()
                .child("accounts")
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
    public void submitLoanRequest(String userID, Request request, final FirebaseDataListener listener) {
        listener.onStart();

        FirebaseDatabase.getInstance().getReference()
                .child("requests")
                .child(userID)
                .setValue(request, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        listener.onComplete(databaseError);
                    }
                });
    }

    @Override
    public void saveBankAccount(String userID, Bank bank, final FirebaseDataListener listener) {
        listener.onStart();

        FirebaseDatabase.getInstance().getReference()
                .child("accounts")
                .child(userID)
                .push()
                .setValue(bank, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        listener.onComplete(databaseError);
                    }
                });
    }
}
