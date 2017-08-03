package ng.com.bitlab.micash.ui.verify;

import android.net.Uri;
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

import java.util.concurrent.TimeUnit;

import ng.com.bitlab.micash.ui.common.BasePresenter;
import static com.mikepenz.iconics.Iconics.TAG;

/**
 * Created by Paul on 25/07/2017.
 */

public class VerifyPresenter extends BasePresenter<VerifyContract.View> implements
                VerifyContract.Presenter {

    private String mVerificationID;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mCode;
    private FirebaseStorage mStorage;


    public VerifyPresenter(VerifyContract.View view) {
        this.view = view;
    }

    @Override
    public void startPhoneVerification(String mPhone) {

        view.showDialog("Requesting OTP");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhone,
                60,
                TimeUnit.SECONDS,
                (AppCompatActivity) view.getActivityContext(),
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        view.hideDialog();
                        view.showVerificationLayout();
                        view.showToast("OTP code has been sent");
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        view.hideDialog();
                        view.showToast(e.getMessage());
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        mVerificationID = s;
                        mResendToken = forceResendingToken;
                    }
                }
        );
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
    public void setProfilePicture() {

    }

    @Override
    public void uploadProfileImage(byte[] data) {

        String imageName = getImageName();

        view.showDialog("Uploading");
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

    private void saveImageToProfile(Uri s) {
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
                            view.showCompletedLayout();
                        } else {
                            view.hideDialog();
                            view.showToast("We encountered an error.");
                        }
                    }
                });
    }

    private String getImageName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String username = user.getDisplayName();
            return username.replaceAll(" ", "_").toLowerCase() + ".jpg";
        }
        return "anonymous.jpg";
    }

    @Override
    public String parsePhoneNumber(String phone) {
        return "+234" + phone.substring(1);
    }

    @Override
    public void resendCode() {
    }

    @Override
    public void verifyAndMergeCredentials(String code) {

        view.showDialog("Verifying");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, code);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
                user.linkWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    view.hideDialog();
                                    view.showSetProfilePictureLayout();
                                } else {
                                    view.hideDialog();
                                    view.showToast(task.getException().getMessage());
                                    Log.w(TAG, "linkWithCredential:failure", task.getException());
                                }

                            }
                        });
        }
    }
}