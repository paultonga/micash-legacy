package ng.com.bitlab.micash.ui.profile;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orm.query.Select;

import ng.com.bitlab.micash.models.ProfileRecord;
import ng.com.bitlab.micash.models.User;

/**
 * Created by paul on 12/3/17.
 */

public class ProfilePresenter implements ProfileContract.Presenter {

    ProfileContract.View view;
    String mPhone;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
    }

    @Override
    public ProfileRecord getProfile() {
        return Select.from(ProfileRecord.class).first();
    }

    @Override
    public String getPhoneNumber(){
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final Query query = ref.child("users").child(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    User u = dataSnapshot.getValue(User.class);
                    //Log.d("mphone", u.getPhoneNumber());
                    view.setPhoneNumber(u.getPhoneNumber());
                }else {
                    view.setPhoneNumber("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                setmPhone("");
            }
        });
        return getmPhone();
    }

    private String getmPhone(){
        return mPhone;
    }
    private void setmPhone(String phone){
        mPhone = phone;
    }
}
