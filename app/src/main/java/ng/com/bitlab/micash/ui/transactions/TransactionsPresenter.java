package ng.com.bitlab.micash.ui.transactions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ng.com.bitlab.micash.models.Request;
import ng.com.bitlab.micash.ui.common.BasePresenter;

/**
 * Created by paul on 1/6/18.
 */

public class TransactionsPresenter extends BasePresenter<TransactionsContract.View> implements
    TransactionsContract.Presenter {

    TransactionsContract.View mView;
    FirebaseDatabase mDatabase;

    public TransactionsPresenter(TransactionsContract.View mView) {
        this.mView = mView;
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void loadTransactions() {
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mView.showLoadingLayout();
        mDatabase.getReference()
                .child("transactions")
                .child(uuid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null){
                            if(dataSnapshot.exists()){
                                List<Request> requests = new ArrayList<>();
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    Request r = ds.getValue(Request.class);
                                    requests.add(r);
                                }
                                mView.showTransactionList(requests);
                            } else {
                                mView.showEmptyLayout();
                            }
                        }else { mView.showEmptyLayout(); }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mView.showEmptyLayout();
                        mView.showToast(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void deleteTransaction(Request request) {
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mView.showDialog("Processing...");
        mDatabase.getReference()
                .child("transactions")
                .child(uuid)
                .orderByChild("date_created")
                .equalTo(request.getDate_created())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null){
                            if(dataSnapshot.exists()){
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    ds.getRef().removeValue();
                                }
                                mView.showToast("Transaction record deleted.");
                                loadTransactions();
                            }else {mView.showEmptyLayout();}
                        }else { mView.showEmptyLayout(); }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mView.showEmptyLayout();
                        mView.showToast(databaseError.getMessage());
                    }
                });
    }
}
