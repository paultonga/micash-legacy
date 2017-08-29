package ng.com.bitlab.micash.ui.register;

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

import ng.com.bitlab.micash.ui.common.BasePresenter;

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
    public void createFirebaseUser(String email, String password, final String name) {

        view.showDialog("Processing");

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
                            view.showToast("We encountered a problem.");
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
