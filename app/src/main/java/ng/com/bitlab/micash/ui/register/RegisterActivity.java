package ng.com.bitlab.micash.ui.register;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.ui.login.LoginActivity;
import ng.com.bitlab.micash.ui.verify.VerifyActivity;
import ng.com.bitlab.micash.utils.Constants;


public class RegisterActivity extends BaseView implements RegisterContract.View {


    @BindView(R.id.editTextPassword) EditText passwordEditText;
    @BindView(R.id.editTextName) EditText nameEditText;
    @BindView(R.id.editTextEmail) EditText emailEditText;

    String mEmail;
    String mPassword;
    String mName;

    private RegisterContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        if(!isTaskRoot()) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
        passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        mPresenter = new RegisterPresenter(this);
        setUpPreferences();

    }




    @Override
    @OnClick(R.id.btn_register)
    public void onRegisterClicked() {
        if(isConnected) {
            if (validateInputs()) {
                //mPresenter.createFirebaseUser(mEmail, mPassword, mName);
                mPresenter.checkEmail(mEmail, mPassword, mName);
            }
        } else {
            showSnackBar("There's no internet connection.");
        }
    }

    @Override
    public void onTermsAndConditionsClicked() {

    }

    @Override
    public boolean validateInputs() {
        boolean flag = true;
        mEmail = emailEditText.getText().toString().trim();
        mName = nameEditText.getText().toString().trim();
        mPassword = passwordEditText.getText().toString().trim();

        if(!mPresenter.isValidPassword(mPassword)){
            passwordEditText.setError("Please enter password (at least 6 chars).");
            flag = false;
        }
        if(!mPresenter.isValidName(mName)){
            nameEditText.setError("Please enter your name.");
            flag = false;
        }
        if(!mPresenter.isValidEmail(mEmail)){
            emailEditText.setError("Please enter a valid email address.");
            flag = false;
        }
        return flag;
    }

    @Override
    public void startVerifyActivity() {
        Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    @OnClick(R.id.tv_agreement)
    public void showTermsAndConditions() {
        final Dialog fullscreenDialog = new Dialog(this, R.style.DialogFullscreen);
        fullscreenDialog.setContentView(R.layout.dialog_fullscreen);
        ImageView img_dialog_fullscreen_close = (ImageView) fullscreenDialog.findViewById(R.id.img_dialog_fullscreen_close);
        TextView tv = (TextView) fullscreenDialog.findViewById(R.id.tv_terms_and_conditions);
        tv.setMovementMethod(new ScrollingMovementMethod());
        img_dialog_fullscreen_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenDialog.dismiss();
            }
        });
        fullscreenDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setUpPreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(Constants.APP_STATE, Constants.START_REGISTER);
        editor.apply();
    }
}
