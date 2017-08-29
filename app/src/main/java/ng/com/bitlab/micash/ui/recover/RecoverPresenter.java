package ng.com.bitlab.micash.ui.recover;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ng.com.bitlab.micash.ui.common.BasePresenter;

/**
 * Created by Paul on 18/08/2017.
 */

public class RecoverPresenter extends BasePresenter<RecoverContract.View> implements
        RecoverContract.Presenter{


    public RecoverPresenter(RecoverContract.View view) {
        this.view = view;
    }

    @Override
    public void requestPasswordReset(String email) {
        view.showDialog("Resetting password...");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            view.hideDialog();
                            view.showToast("Password reset has been sent to your email.");
                            view.startLoginActivity();
                        } else {
                            view.hideDialog();
                            view.showToast("Error resetting your password. Please check the emall address.");
                        }
                    }
                });
    }
}
