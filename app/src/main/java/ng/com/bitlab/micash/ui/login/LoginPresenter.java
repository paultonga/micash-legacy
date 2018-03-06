package ng.com.bitlab.micash.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.models.Device;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.common.BasePresenter;
import ng.com.bitlab.micash.utils.Constants;

/**
 * Created by Paul on 15/08/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements
            LoginContract.Presenter {

    AppPreference mPref;
    public LoginPresenter(LoginContract.View view){
        mPref = MiCashApplication.getPreference();
        this.view = view;
    }


    @Override
    public void Login(String email, String password) {
        view.showDialog("Logging in...");
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            view.hideDialog();
                            view.showToast("Login successful.");
                            //saveUserToPreferences(mAuth.getCurrentUser());
                            savePreferences();
                            view.startMainActivity();
                        } else {
                            view.hideDialog();
                            view.showToast("Login failed.");
                        }
                    }
                });
    }

    private void savePreferences(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            mPref.setProfile(user.getPhotoUrl().toString());
            mPref.setName(user.getDisplayName());
            mPref.setUUID(user.getUid());
            mPref.setEmail(user.getEmail());
        }
    }

    private void saveUserToPreferences(FirebaseUser user) {
        User mUser = new User();
        mUser.setUuid(user.getUid());
        mUser.setFullName(user.getDisplayName());
        mUser.setEmail(user.getEmail());
        mUser.setPhoneNumber(user.getPhoneNumber());
        mUser.setProfileImage(user.getPhotoUrl() == null ? Constants.PROFILE_URL : user.getPhotoUrl().toString() );
        mUser.setLastSeen(org.joda.time.DateTime.now().getMillis());
        mUser.setDateCreated(org.joda.time.DateTime.now().getMillis());
        //mUser.device = getDevice();

        Gson gson = new Gson();
        String json = gson.toJson(mUser);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getActivityContext());
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(Constants.USER, json);
        editor.apply();
    }

    @Override
    public User getUserFromPreference() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getActivityContext());
        String user = sp.getString(Constants.USER, null);
        if (user != null){
            Gson gson = new Gson();
            User u = gson.fromJson(user, User.class);
            return u;
        }
        return null;
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
