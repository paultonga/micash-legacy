package ng.com.bitlab.micash.ui.register;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.ui.common.BasePresenter;
import ng.com.bitlab.micash.ui.intro.IntroActivity;

import static com.mikepenz.iconics.Iconics.TAG;

/**
 * Created by Paul on 22/07/2017.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements
                    RegisterContract.Presenter {

    FirebaseAuth mAuth;

    public RegisterPresenter(RegisterContract.View view){
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean isValidName(String name) {
        return  (name != null && name.length() >= 3);
    }


    @Override
    public void checkEmail(final String email, final String password, final String name) {
        view.showDialog("Checking your email...");

        String email_md5 = getMD5(email);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        Query query = db.child("checkEmail").child(email_md5);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    view.hideDialog();
                    view.showToast("This email is already in use.");
                } else {
                    savePref(email, password, name);
                    view.hideDialog();
                    view.startVerifyActivity();
                }
            }

            private void savePref(String email, String password, String name){
                AppPreference mPref = MiCashApplication.getPreference();

                mPref.setEmail(email);
                mPref.setPassword(password);
                mPref.setName(name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.hideDialog();
                view.showToast(databaseError.getMessage());
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
    public void createFirebaseUser(String email, String password, final String name) {

        view.showDialog("Creating your account...");

        mAuth.createUserWithEmailAndPassword(email,password)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateDisplayName(name, user);
                        }
                        else {
                            view.hideDialog();
                            view.showToast(task.getException().getMessage());
                        }
                   }
               });

    }



    @Override
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
                            view.hideDialog();
                            view.showToast("Your account has been created.");
                            view.startVerifyActivity();
                        }
                    }
                });
    }
}
