package ng.com.bitlab.micash.ui.addEmployment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.models.Profile;
import ng.com.bitlab.micash.models.ProfileRecord;
import ng.com.bitlab.micash.ui.common.BasePresenter;
import ng.com.bitlab.micash.utils.Constants;

/**
 * Created by paul on 12/2/17.
 */

public class AddEmploymentPresenter extends BasePresenter<AddEmploymentContract.View> implements AddEmploymentContract.Presenter {

    AppPreference mPref;


    public AddEmploymentPresenter(AddEmploymentContract.View view) {
        this.view = view;
    }

    @Override
    public void saveProfile(final Profile profile) {

        view.showDialog("Processing...");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mPref = MiCashApplication.getPreference();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference profileRef = db.getReference().child("profile");

        profileRef.child(user.getUid()).setValue(profile, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    //ProfileRecord p = new ProfileRecord(profile);
                    //p.save();
                    mPref.setEmploymentSaved(Constants.DONE);
                    view.hideDialog();
                    view.showToast("Your data was saved successfully.");
                    view.startNextActivity();
                } else {
                    view.hideDialog();
                    view.showErrorLayout(databaseError.getMessage());
                }
            }
        });



    }
}
