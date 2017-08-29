package ng.com.bitlab.micash.ui.verify;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import java.util.concurrent.TimeUnit;

import ng.com.bitlab.micash.ui.common.BasePresenter;
import static com.mikepenz.iconics.Iconics.TAG;

/**
 * Created by Paul on 25/07/2017.
 */

public class VerifyPresenter extends BasePresenter<VerifyContract.View> implements
                VerifyContract.Presenter, VerificationListener {

    private String mVerificationID;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mCode;
    Verification mVerification;
    String mDisplayName;
    String mPhone = "";



    public VerifyPresenter(VerifyContract.View view) {

        this.view = view;

    }

    @Override
    public void startAutoVerification(final String code) {
        view.updateVerificationLayout(code);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                verifyCode(code);
            }
        },800);

    }

    @Override
    public void startPhoneVerification(String phone) {
        view.showDialog("Sending OTP...");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            mDisplayName = user.getDisplayName();
        } else {
            view.hideDialog();
            view.showToast("Error getting user details.");
        }
        mVerification = SendOtpVerification.createSmsVerification
                (SendOtpVerification
                        .config(phone)
                        .context(view.getActivityContext())
                        .autoVerification(true)
                        .otplength("6")
                        .senderId("MiCash")
                        .message("Hi "+ mDisplayName + ", your OTP is ##OTP##.")
                        .expiry("5")
                        .build(), this);

        mVerification.initiate();

    }

    @Override
    public boolean validateCode(String code) {
        if(code != null)
            if(code.length() == 6)
                return true;

        return false;
    }

    @Override
    public boolean validatePhoneNumber(String phone) {
        if (phone != null){
            if (phone.length() == 11){
                return true;
            }
        }
        return false;
    }

    @Override
    public String parsePhoneNumber(String phone) {
        return "+234" + phone.substring(1);
    }

    @Override
    public void resendCode() {
        view.showDialog("Resending OTP...");
        mVerification.resend("text");
    }

    @Override
    public void verifyCode(String code) {
        view.showDialog("Verifying...");
        mVerification.verify(code);
    }

    @Override
    public void resume() {

    }

    @Override
    public void onInitiated(String response) {
        view.hideDialog();
        view.showVerificationLayout();
        view.showToast("OTP has been sent.");
    }


    @Override
    public void onInitiationFailed(Exception paramException) {
        view.hideDialog();
        view.showToast(paramException.getMessage());
    }

    @Override
    public void onVerified(String response) {
        view.hideDialog();
        view.gotoUploadActivity();
        view.showToast("Your number has been verified.");

    }

    @Override
    public void onVerificationFailed(Exception paramException) {
        view.hideDialog();
        view.showToast(paramException.getMessage());
    }
}