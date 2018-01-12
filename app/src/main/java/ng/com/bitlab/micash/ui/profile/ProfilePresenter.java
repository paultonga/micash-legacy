package ng.com.bitlab.micash.ui.profile;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.models.Profile;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.common.BasePresenter;

import static com.mikepenz.iconics.Iconics.TAG;

/**
 * Created by paul on 12/3/17.
 */

public class ProfilePresenter extends BasePresenter<ProfileContract.View> implements ProfileContract.Presenter {

    ProfileContract.View view;
    String mPhone;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void getProfile() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference()
                .child("profile")
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null){
                            Profile profile = dataSnapshot.getValue(Profile.class);
                            view.showDataLayout(profile);
                        }else {
                            view.showEmptyLayout();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    @Override
    public void uploadProfileImage(byte[] data) {
        String filename = getImageName();
        final AppPreference mPref = MiCashApplication.getPreference();

        view.showDialog("Uploading image...");
        FirebaseStorage.getInstance().getReference()
                .child("profile/"+filename)
                .putBytes(data)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Uri downloadUrl = task.getResult().getDownloadUrl();
                            mPref.setProfile(downloadUrl.toString());
                            updateUserProfile(downloadUrl);
                        } else{
                          view.hideDialog();
                          view.showToast(task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public String getImageName() {
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if(!username.isEmpty()){
            return username.replaceAll(" ", "_").toLowerCase() + ".jpg";
        }
        return "anonymous.jpg";
    }

    @Override
    public void updateUserProfile(Uri s) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String url = s.toString();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(s)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateUserAccount(url);
                        } else {
                            view.hideDialog();
                            view.showToast("We encountered an error. Please try again.");
                        }
                    }
                });
    }

    @Override
    public void updateUserAccount(final String uri) {
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final AppPreference pref = MiCashApplication.getPreference();

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(uuid)
                .child("profileImage")
                .setValue(uri)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            view.hideDialog();
                            pref.setProfile(uri);
                            view.showToast("Profile image updated");
                            view.refreshImage();
                        } else {
                            view.hideDialog();
                            view.showToast(task.getException().getMessage());
                        }

                    }
                });
    }

    private String getmPhone(){
        return mPhone;
    }
    private void setmPhone(String phone){
        mPhone = phone;
    }
}
