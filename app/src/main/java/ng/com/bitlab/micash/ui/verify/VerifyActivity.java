package ng.com.bitlab.micash.ui.verify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.FirebaseException;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.utils.Constants;

public class VerifyActivity extends BaseView implements VerifyContract.View {


    @BindView(R.id.phone_number_layout) RelativeLayout phoneNumberLayout;
    @BindView(R.id.profile_image_layout) RelativeLayout profileImageLayout;
    @BindView(R.id.code_layout) RelativeLayout codeLayout;
    @BindView(R.id.completed_layout) RelativeLayout completedLayout;


    @BindView(R.id.editTextPhone) com.github.reinaldoarrosi.maskededittext.MaskedEditText phoneInput;
    @BindView(R.id.editTextCode) com.github.reinaldoarrosi.maskededittext.MaskedEditText codeInput;
    @BindView(R.id.profile_image) CircleImageView profileImage;


    @BindView(R.id.tv_skip) TextView textViewSkip;
    @BindView(R.id.tv_resend) TextView textViewResend;


    @BindView(R.id.btn_request_code) Button btnRequestCode;
    @BindView(R.id.btn_set_profile_image) Button btnSaveProfileImage;
    @BindView(R.id.btn_verify) Button btnVerify;
    @BindView(R.id.btn_get_started) Button btnGetStarted;

    private FirebaseAuth mAuth;
    private String mVerificationID;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private int PICK_IMAGE_REQUEST = 1;
    String imageName;
    String imagePath;
    Drawable defaultImage;

    VerifyContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);

        mPresenter = new VerifyPresenter(this);
        phoneInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        codeInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        defaultImage = profileImage.getDrawable();
        showPhoneNumberLayout();
    }


    @Override
    @OnClick(R.id.btn_request_code)
    public void onRequestCodeClicked() {
        String rawPhone = phoneInput.getText(true).toString().trim();
        if(!mPresenter.validatePhoneNumber(rawPhone)){
            phoneInput.setError("Enter a valid mobile phone number.");
        } else {
            String parsedPhone = mPresenter.parsePhoneNumber(rawPhone);
            mPresenter.startPhoneVerification(parsedPhone);
        }
    }

    @Override
    @OnClick(R.id.btn_verify)
    public void onVerifyClicked() {
        String code = codeInput.getText(true).toString().trim();
        if(!mPresenter.validateCode(code)){
            codeInput.setError("Please enter 6-digit OTP.");
        } else {
            mPresenter.verifyAndMergeCredentials(code);
        }
    }

    @Override
    @OnClick(R.id.tv_resend)
    public void onResendCodeClicked() {

    }

    @Override
    @OnClick(R.id.btn_set_profile_image)
    public void onSetProfilePictureClicked() {
        if(isConnected) {

            if (profileImage.getDrawable() != defaultImage) {
                profileImage.setDrawingCacheEnabled(true);
                profileImage.buildDrawingCache();
                Bitmap bitmap = profileImage.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                mPresenter.uploadProfileImage(data);
            } else {
                showSnackBar("Please select an image");
            }
        } else {
            showSnackBar("There's no internet connection.");
        }
    }

    @Override
    @OnClick(R.id.tv_skip)
    public void onSkipProfilePictureClicked() {
        showCompletedLayout();
    }

    @Override
    public void showVerificationLayout() {
        codeLayout.setVisibility(View.VISIBLE);
        phoneNumberLayout.setVisibility(View.GONE);
        profileImageLayout.setVisibility(View.GONE);
        completedLayout.setVisibility(View.GONE);
    }

    @Override
    public void showSetProfilePictureLayout() {
        codeLayout.setVisibility(View.GONE);
        phoneNumberLayout.setVisibility(View.GONE);
        profileImageLayout.setVisibility(View.VISIBLE);
        completedLayout.setVisibility(View.GONE);
    }

    @Override
    public void showCompletedLayout() {
        codeLayout.setVisibility(View.GONE);
        phoneNumberLayout.setVisibility(View.GONE);
        profileImageLayout.setVisibility(View.GONE);
        completedLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPhoneNumberLayout() {
        codeLayout.setVisibility(View.GONE);
        phoneNumberLayout.setVisibility(View.VISIBLE);
        profileImageLayout.setVisibility(View.GONE);
        completedLayout.setVisibility(View.GONE);

    }

    @Override
    @OnClick(R.id.btn_get_started)
    public void startMainActivity() {
        setUpPreferences();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    @OnClick(R.id.profile_image)
    public void selectProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    @OnClick(R.id.btn_get_started)
    public void getStartedClicked() {
        startMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            imageName = uri.getLastPathSegment();
            imagePath = uri.getPath();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpPreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(Constants.HAS_LOGGED_IN_BEFORE, true);
        editor.apply();
    }

}