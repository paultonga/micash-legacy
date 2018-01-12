package ng.com.bitlab.micash.ui.cards;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Bank;
import ng.com.bitlab.micash.ui.common.BasePresenter;

/**
 * Created by paul on 12/3/17.
 */

public class CardsPresenter extends BasePresenter<CardsContract.View> implements CardsContract.Presenter {

    CardsContract.View view;
    CardsContract.Repository mRepository;

    public CardsPresenter(CardsContract.View view) {
        this.view = view;
        mRepository = new CardsRepository();
    }


    @Override
    public void fetchData() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRepository.fetchRecords(userID, new FirebaseQueryListener() {
            @Override
            public void onStart() {
                view.showLoadingLayout();
            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                if(databaseError == null){
                    if(dataSnapshot.exists()){
                        List<Bank> banks = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            Bank bank = ds.getValue(Bank.class);
                            banks.add(bank);
                        }
                        view.showRecords(banks);
                    }else {
                        view.showEmptyLayout();
                    }
                }else {
                    view.showEmptyLayout();
                    view.showToast(databaseError.getMessage());
                }
            }
        });

    }

    @Override
    public void saveAccount(Bank bank, String userID) {

        mRepository.saveRecord(bank, userID, new FirebaseQueryListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                view.hideDialog();
                if(databaseError == null) {
                    fetchData();
                } else {
                    view.showToast(databaseError.getMessage());
                }
            }
        });

    }

    @Override
    public void checkAccount(final Bank bank) {
        final String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRepository.getRecord(bank, userUUID, new FirebaseQueryListener() {
            @Override
            public void onStart() {
                view.showDialog("Processing...");
            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                if(databaseError == null){
                    if(dataSnapshot.exists()){
                        view.hideDialog();
                        view.showToast("This account is already saved.");
                    } else {
                        saveAccount(bank, userUUID);
                    }
                }else {
                    view.hideDialog();
                    view.showToast(databaseError.getMessage());
                }
            }
        });

    }

    @Override
    public void deleteRecord(Bank bank) {
        String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRepository.getRecord(bank, userUUID, new FirebaseQueryListener() {
            @Override
            public void onStart() {
                view.showDialog("Deleting...");
            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                if(databaseError == null){
                    for(DataSnapshot bankSnapshot: dataSnapshot.getChildren()){
                        bankSnapshot.getRef().removeValue();
                    }
                    view.hideDialog();
                    view.showToast("Account deleted");
                    fetchData();
                }else {
                    view.hideDialog();
                    view.showToast(databaseError.getMessage());
                }
            }
        });
    }
}
