package ng.com.bitlab.micash.ui.processing;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import net.orange_box.storebox.StoreBox;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.models.Device;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.models.Verify;
import ng.com.bitlab.micash.utils.Constants;

import static com.mikepenz.iconics.Iconics.TAG;

/**
 * Created by Paul on 04/10/2017.
 */

public class ProcessingPresenter implements ProcessingContract.Presenter {


    private ProcessingContract.View mView;
    private AppPreference mPref;
    private FirebaseAuth mAuth;


    public ProcessingPresenter(ProcessingContract.View mView) {
        this.mView = mView;
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void createUserAccount() {
        mPref = MiCashApplication.getPreference();
        if(mPref.getEmail() != null && mPref.getPassword() != null) {
            String email = mPref.getEmail();
            String password = mPref.getPassword();

            mView.updateMessage("Processing", "We are creating your account. Please wait...");

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateDisplayName(mPref.getName(), user);
                                saveUsertoFirebase(user);
                            }
                            else {
                                String error = task.getException() == null ? "We encountered an error.": task.getException().getMessage();
                                mView.showErrorLayout(error);
                            }
                        }
                    });
        } else {
            mView.showErrorLayout("We can not locate user details stored in preferences.");
        }
    }

    private void saveUsertoPref(User user){
        mPref = MiCashApplication.getPreference();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mPref.setUser(json);
    }

    @Override
    public void saveUsertoFirebase(FirebaseUser firebaseUser) {
        mView.updateMessage("Saving Details", "We are saving your details. Please wait..");

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = db.getReference().child("users");
        final User mUser = getUser();

        userRef.child(firebaseUser.getUid()).setValue(mUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null){
                    mView.showErrorLayout(databaseError.getMessage());
                } else {
                    saveUsertoPref(mUser);
                    finalizeSetup();                }
            }
        });

    }


    @Override
    public void finalizeSetup() {
        mPref = MiCashApplication.getPreference();
        mView.updateMessage("Finalizing", "Putting finishing touches...Will be done in few seconds.");

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference checkEmail = db.getReference().child("checkEmail");
        final DatabaseReference checkPhone = db.getReference().child("checkPhone");
        final DatabaseReference deviceRef = db.getReference().child("devices");
        final DatabaseReference verifyRef = db.getReference().child("verifications");


        deviceRef.child(mUser.getUid()).setValue(getDevice());
        verifyRef.child(mUser.getUid()).setValue(getVerify());

        String email_md5 = getMD5(mPref.getEmail());

        checkEmail.child(email_md5).setValue(true, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null){
                    mView.showErrorLayout(databaseError.getMessage());
                } else {
                    checkPhone.child(mPref.getPhone()).setValue(true, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError != null){
                                mView.showErrorLayout(databaseError.getMessage());
                            }else {
                                mView.startUploadActivity();
                            }
                        }
                    });

                }
            }
        });

    }

    private String getMD5(String email) {
        StringBuffer _sb = new StringBuffer();

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(email.getBytes());

            byte byteData[] = md.digest();

            for(int i = 0; i<byteData.length; i++){
                _sb.append(Integer.toString((byteData[i] & 0xff)+0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return _sb.toString();
    }

    @Override
    public User getUser() {
        mPref = MiCashApplication.getPreference();
        FirebaseUser fu = FirebaseAuth.getInstance().getCurrentUser();
        User u = new User();
        u.setEmail(mPref.getEmail());
        u.setFullName(mPref.getName());
        u.setDateCreated(org.joda.time.DateTime.now().getMillis());
        u.setLastSeen(org.joda.time.DateTime.now().getMillis());
        u.setProfileImage(Constants.PROFILE_URL);
        u.setUuid(fu.getUid());
        u.setPhoneNumber(mPref.getPhone());


        return u;
    }

    @Override
    public Device getDevice() {

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

    @Override
    public Verify getVerify() {
        mPref = MiCashApplication.getPreference();
        Verify v = new Verify();

        v.setDateCreated(org.joda.time.DateTime.now().getMillis());
        v.setCode(mPref.getCode());
        v.setPhone(mPref.getPhone());

        return v;
    }

    public void updateDisplayName(String name, FirebaseUser user) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }
}
