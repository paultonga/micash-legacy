package ng.com.bitlab.micash.ui.verify;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.FirebaseException;
import com.google.firebase.storage.UploadTask;


import org.w3c.dom.Text;

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
import ng.com.bitlab.micash.ui.upload.UploadActivity;
import ng.com.bitlab.micash.utils.Constants;
import ng.com.bitlab.micash.utils.SMSListener;
import ng.com.bitlab.micash.utils.SMSReceiver;

public class VerifyActivity extends BaseView implements VerifyContract.View {


    @BindView(R.id.phone_number_layout) RelativeLayout phoneNumberLayout;
    @BindView(R.id.code_layout) RelativeLayout codeLayout;


    @BindView(R.id.editTextPhone) com.github.reinaldoarrosi.maskededittext.MaskedEditText phoneInput;
    @BindView(R.id.editTextCode) com.github.reinaldoarrosi.maskededittext.MaskedEditText codeInput;


    @BindView(R.id.tv_resend) TextView textViewResend;
    @BindView(R.id.tv_verify_notice) TextView verifyNotice;

    @BindView(R.id.btn_request_code) Button btnRequestCode;
    @BindView(R.id.btn_verify) Button btnVerify;

    private FirebaseAuth mAuth;
    final int PERMISSION_REQUEST_CODE = 123;


    static VerifyContract.Presenter mPresenter;

    String mPhone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);

        mPresenter = new VerifyPresenter(this);
        phoneInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        codeInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        if(Build.VERSION.SDK_INT < 23){
            SMSReceiver.bindListener(new SMSListener() {
                @Override
                public void messageReceived(String code) {
                    mPresenter.startAutoVerification(code);
                }
            });
        }else {
            requestSMSPermission();
        }


    }

    private void requestSMSPermission() {
        int hasSMSPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if(hasSMSPermission != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]   {Manifest.permission.RECEIVE_SMS}, PERMISSION_REQUEST_CODE);
        }else {
            //Toast.makeText(AddContactsActivity.this, "Contact Permission is already granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SMSReceiver.bindListener(new SMSListener() {
                        @Override
                        public void messageReceived(String code) {
                            mPresenter.startAutoVerification(code);
                        }
                    });
                } else {
                    showToast("MiCash will not be able to automatically verify you.");
                }
                break;
        }
    }

    @Override
    public void updateVerificationLayout(String code) {
        codeInput.setText(code);
    }

    @Override
    @OnClick(R.id.btn_request_code)
    public void onRequestCodeClicked() {
        String rawPhone = phoneInput.getText(true).toString().trim();
        if(!mPresenter.validatePhoneNumber(rawPhone)){
            phoneInput.setError("Enter a valid mobile phone number.");
        } else {
            String parsedPhone = mPresenter.parsePhoneNumber(rawPhone);
            mPhone = parsedPhone;
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
            mPresenter.verifyCode(code);
        }
    }

    @Override
    @OnClick(R.id.tv_resend)
    public void onResendCodeClicked() {
        mPresenter.resendCode();
    }


    @Override
    public void showVerificationLayout() {
        savePreferences(Constants.START_CODE_VERIFICATION);
        codeLayout.setVisibility(View.VISIBLE);
        verifyNotice.setText(getNoticeString(mPhone));
        phoneNumberLayout.setVisibility(View.GONE);

    }

    @Override
    @OnClick(R.id.tv_edit_number)
    public void showEditNumberDialog() {

        new MaterialDialog.Builder(this)
                .title("Edit number")
                .content("Please enter your mobile phone number and click submit.")
                .inputRangeRes(11, 11, R.color.md_red_500)
                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER)
                .input("Enter phone number", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        showToast(input.toString());
                    }
                }).show();

    }

    @Override
    public void showPhoneNumberLayout() {
        savePreferences(Constants.START_PHONE_VERIFICATION);
        codeLayout.setVisibility(View.GONE);
        phoneNumberLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void gotoUploadActivity() {
        Intent i = new Intent(this, UploadActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public String getNoticeString(String phone) {
        SpannableStringBuilder sb = new SpannableStringBuilder("We have sent an OTP to "+ phone + ". Please enter the code. miCash will attempt to verify the code automatically.");
        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);

        sb.setSpan(bold, 23, 23 + phone.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return sb.toString();
    }

    private void savePreferences(String s) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(Constants.APP_STATE, s);
        editor.apply();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}