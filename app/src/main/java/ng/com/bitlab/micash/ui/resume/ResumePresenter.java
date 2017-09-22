package ng.com.bitlab.micash.ui.resume;

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

import ng.com.bitlab.micash.models.Device;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.common.BasePresenter;
import ng.com.bitlab.micash.utils.Constants;

/**
 * Created by Paul on 18/08/2017.
 */

public class ResumePresenter extends BasePresenter<ResumeContract.View>
        implements ResumeContract.Presenter {


    public ResumePresenter(ResumeContract.View view) {
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
                            saveUserToPreference(mAuth.getCurrentUser());
                            view.hideDialog();
                            view.showToast("Login successful.");
                            view.startMainActivity();
                        } else {
                            view.hideDialog();
                            view.showToast("Login failed.");
                        }
                    }
                });
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

    @Override
    public void saveUserToPreference(FirebaseUser user) {
        User mUser = new User();
        mUser.setUuid(user.getUid());
        mUser.setFullName(user.getDisplayName());
        mUser.setEmail(user.getEmail());
        mUser.setPhoneNumber(user.getPhoneNumber());
        mUser.setProfileImage(user.getPhotoUrl().toString());
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
