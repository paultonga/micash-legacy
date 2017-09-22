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
import java.util.HashMap;
import java.util.Map;

import ng.com.bitlab.micash.models.Device;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.models.Verify;
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
            mUser.setPhoneNumber(getVerify().getPhone());
            mUser.setProfileImage(getPhotoUrl());
            mUser.setLastSeen(org.joda.time.DateTime.now().getMillis());
            mUser.setDateCreated(org.joda.time.DateTime.now().getMillis());


            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            final DatabaseReference userRef = db.getReference().child("users");
            final DatabaseReference deviceRef = db.getReference().child("devices");
            final DatabaseReference verifyRef = db.getReference().child("verifications");


            userRef.child(mUser.getUuid()).setValue(mUser, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null){
                        view.hideDialog();
                        view.showToast(databaseError.getMessage());
                    } else {
                        Gson gson = new Gson();
                        String json = gson.toJson(mUser);
                        deviceRef.child(mUser.getUuid()).setValue(getDevice());
                        verifyRef.child(getVerify().getPhone()).setValue(getVerify());

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getActivityContext());
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString(Constants.USER, json);
                        editor.apply();

                        view.hideDialog();
                        view.showToast("Your details have been saved.");
                        view.startMainActivity();
                    }
                }
            });

        }
    }

    private Device getDevice() {
        Device d = new Device();

        d.uuid = mUser.getUuid();
        d.brand = Build.BRAND;
        d.fingerprint = Build.FINGERPRINT;
        d.manufacturer = Build.MANUFACTURER;
        d.model = Build.MODEL;
        d.product = Build.PRODUCT;
        d.serial = Build.SERIAL;
        d.os = Build.VERSION.CODENAME;

        return d;
    }

    private String getPhotoUrl(){
        final String defaultUrl = "https://firebasestorage.googleapis.com/v0/b/micash-e8219.appspot.com/o/profile%2Fprofile.png?alt=media&token=36fa184b-72f8-464d-88fe-cb379ead6fcf";
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if(u.getPhotoUrl() == null){
            return defaultUrl;
        } else{
            return u.getPhotoUrl().toString();
        }
    }

    private Verify getVerify(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getActivityContext());
        String phone = sp.getString(Constants.PHONE, null);
        String code = sp.getString(Constants.CODE, null);


            Verify v = new Verify();
            v.setPhone(phone);
            v.setCode(code);
            v.setDateCreated(org.joda.time.DateTime.now().getMillis());
            v.setUuid(user.getUid());

            return v;

    }
}
