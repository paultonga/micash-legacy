package ng.com.bitlab.micash.ui.ledger;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.models.Ledger;
import ng.com.bitlab.micash.ui.common.BasePresenter;

/**
 * Created by Paul Tonga on 07/02/2018.
 */

public class LedgerListPresenter extends BasePresenter<LedgerListContract.View> implements
        LedgerListContract.Presenter{
    LedgerListContract.View mView;
    AppPreference mPref;

    public LedgerListPresenter(LedgerListContract.View mView) {
        mPref = MiCashApplication.getPreference();
        this.mView = mView;
    }

    @Override
    public void getPaidLedgers() {

    }

    @Override
    public void getUnpaidLedgers() {

    }

    @Override
    public void setLedgerAsPaid(Ledger ledger) {
        mView.showDialog("Processing...");
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("ledgers")
                .child(mPref.getUUID())
                .orderByChild("dateDue")
                .equalTo(ledger.getDateDue())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            DataSnapshot ds = dataSnapshot.getChildren().iterator().next();
                            String key = ds.getKey();
                            ref.child("ledgers").child(mPref.getUUID()).child(key).child("paymentSent").setValue(true, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    mView.hideDialog();
                                    loadData();
                                }
                            });
                        }else{
                            mView.hideDialog();
                            mView.showToast("Ledger not found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mView.hideDialog();
                        mView.showToast(databaseError.getMessage());
                    }
                });

    }

    @Override
    public void loadData() {
        mView.showLoadingLayout();
        FirebaseDatabase.getInstance()
                .getReference("ledgers")
                .child(mPref.getUUID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            List<Ledger> unpaid = new ArrayList<>();
                            List<Ledger> paid = new ArrayList<>();
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Ledger l = ds.getValue(Ledger.class);
                                if (l.isPaymentReceived()){
                                    paid.add(l);
                                } else if (!l.isPaymentReceived()){
                                    unpaid.add(l);
                                }
                            }
                            if(paid.isEmpty() && unpaid.isEmpty()){
                                mView.showEmptyLayout();
                            } else {
                                mView.showContentLayout();
                                if(!paid.isEmpty()){
                                    mView.showPaidLedgers(paid);
                                }
                                if(!unpaid.isEmpty()){
                                    mView.showUnpaidLedgers(unpaid);
                                }
                            }
                        }else {
                            mView.showEmptyLayout();
                            //mView.showToast("No ledger found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mView.showEmptyLayout();
                        mView.showToast(databaseError.getMessage());
                    }
                });

    }
}
