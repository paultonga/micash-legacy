package ng.com.bitlab.micash.ui.verify;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.models.Device;
import ng.com.bitlab.micash.models.Verify;
import ng.com.bitlab.micash.ui.common.BasePresenter;
import ng.com.bitlab.micash.utils.Constants;
import ng.com.bitlab.micash.utils.StringHolder;

import static com.mikepenz.iconics.Iconics.TAG;

/**
 * Created by Paul on 25/07/2017.
 */

public class VerifyPresenter extends BasePresenter<VerifyContract.View> implements
        VerifyContract.Presenter, VerificationListener {

    Verification mVerification;
    String mDisplayName;
    String mPhone = "";
    private String mVerificationID;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mCode;

    AppPreference mPref;


    public VerifyPresenter(VerifyContract.View view) {
        this.view = view;
    }

    @Override
    public void startAutoVerification(final String code) {

    }

    @Override
    public void startPhoneVerification(String phone) {

        view.showDialog("Requesting OTP...");
        mPref = MiCashApplication.getPreference();

        //mPref.setPhone(phone);


            mDisplayName = mPref.getName();
            mVerification = SendOtpVerification.createSmsVerification
                    (SendOtpVerification
                            .config(phone)
                            .context(view.getActivityContext())
                            .autoVerification(true)
                            .otplength("6")
                            .senderId("miCash")
                            .message("Hi " + mDisplayName + ", your OTP is ##OTP##.")
                            .expiry("5")
                            .build(), this);

            mVerification.initiate();


    }


    @Override
    public boolean validateCode(String code) {
        if (code != null)
            if (code.length() == 6)
                return true;

        return false;
    }

    @Override
    public boolean validatePhoneNumber(String phone) {
        if (phone != null) {
            if (phone.length() == 11) {
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
    public void verifyCode(final String code) {
        mPref = MiCashApplication.getPreference();
        mPref.setCode(code);

        view.showDialog("Verifying OTP...");
        mVerification.verify(mCode);

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
        //view.gotoUploadActivity();
        view.startProcessingActivity();
        view.showToast("Your number has been verified.");
    }

    @Override
    public void onVerificationFailed(Exception paramException) {
        view.hideDialog();
        view.showToast(paramException.getMessage());
    }

    @Override
    public void saveVerifyPreferences(String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getActivityContext());
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(key, value);
        editor.apply();

    }

    public void checkNumberAvailability(final String phone) {

        mPref = MiCashApplication.getPreference();

        //mPref.setPhone(phone);


        view.showDialog("Checking your number...");

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query query = db.child("checkPhone").child(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    view.hideDialog();
                    view.showToast("This phone number is already in use.");
                } else {
                    view.hideDialog();
                    startPhoneVerification(phone);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.hideDialog();
                view.showToast(databaseError.getMessage());
            }
        });
    }

}