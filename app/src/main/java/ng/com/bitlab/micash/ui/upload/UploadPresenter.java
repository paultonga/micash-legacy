package ng.com.bitlab.micash.ui.upload;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.Date;

import ng.com.bitlab.micash.models.Device;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.common.BasePresenter;
import ng.com.bitlab.micash.utils.Constants;

import static com.mikepenz.iconics.Iconics.TAG;

/**
 * Created by Paul on 08/08/2017.
 */

public class UploadPresenter extends BasePresenter<UploadContract.View>
                implements UploadContract.Presenter{

    private FirebaseStorage mStorage;
    User mUser;


    public UploadPresenter(UploadContract.View view){
        this.view = view;
    }

    @Override
    public void uploadProfileImage(byte[] data) {
        String imageName = getImageName();

        view.showDialog("Uploading image...");
        mStorage = FirebaseStorage.getInstance();

        StorageReference storageReference = mStorage.getReference();
        StorageReference profileImage = storageReference.child("profile/"+imageName);
        UploadTask uploadTask;

        uploadTask = profileImage.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                view.hideDialog();
                view.showToast(exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                saveImageToProfile(downloadUrl);
            }
        });

    }
    @Override
    public void saveImageToProfile(Uri s) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(s)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            view.hideDialog();
                            view.showToast("Image saved successfully.");
                            view.showGetStartedLayout();
                        } else {
                            view.hideDialog();
                            view.showToast("We encountered an error.");
                        }
                    }
                });
    }

    @Override
    public String getImageName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String username = user.getDisplayName();
            return username.replaceAll(" ", "_").toLowerCase() + ".jpg";
        }
        return "anonymous.jpg";
    }

    @Override
    public void saveUser(){
        view.showDialog("Finalizing...");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUser = new User();

        if(user != null){
            mUser.setUuid(user.getUid());
            mUser.setFullName(user.getDisplayName());
            mUser.setEmail(user.getEmail());
            mUser.setPhoneNumber(user.getPhoneNumber());
            mUser.setProfileImage(user.getPhotoUrl().toString());
            mUser.setLastSeen(org.joda.time.DateTime.now().getMillis());
            mUser.setDateCreated(org.joda.time.DateTime.now().getMillis());
            mUser.device = getDevice();

            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference("micash/users");

            ref.setValue(mUser, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null){
                        view.hideDialog();
                        view.showToast(databaseError.getMessage());
                    } else {
                        Gson gson = new Gson();
                        String json = gson.toJson(mUser);

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getActivityContext());
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString(Constants.USER, json);
                        editor.apply();

                        view.hideDialog();
                        view.startMainActivity();
                        view.showToast("Your details have been saved.");
                    }
                }
            });

        }
    }

    private Device getDevice() {
        Device d = new Device();

        d.brand = Build.BRAND;
        d.fingerprint = Build.FINGERPRINT;
        d.manufacturer = Build.MANUFACTURER;
        d.model = Build.MODEL;
        d.product = Build.PRODUCT;
        d.serial = Build.SERIAL;
        d.os = Build.VERSION.CODENAME;

        return d;
    }
}
