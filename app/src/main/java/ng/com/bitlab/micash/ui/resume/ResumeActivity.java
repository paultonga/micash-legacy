package ng.com.bitlab.micash.ui.resume;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.ui.login.LoginActivity;
import ng.com.bitlab.micash.ui.recover.RecoverActivity;
import ng.com.bitlab.micash.ui.register.RegisterActivity;

public class ResumeActivity extends BaseView implements ResumeContract.View {

    @BindView(R.id.profile_image) CircleImageView profileImage;

    @BindView(R.id.resume_login_name) TextView loginName;
    @BindView(R.id.resume_login_email) TextView loginEmail;
    @BindView(R.id.input_resume_password) EditText passwordInput;

    ResumeContract.Presenter mPresenter;
    User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        ButterKnife.bind(this);
        mPresenter = new ResumePresenter(this);
        mUser = mPresenter.getUserFromPreference();

        passwordInput.setTransformationMethod(new PasswordTransformationMethod());
        passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        updateUI(mUser);
    }

    @Override
    public void updateUI(User user) {
        AppPreference mPref = MiCashApplication.getPreference();

        String name = user.getFullName();

        loginName.setText(mPref.getName());

        Picasso.with(this)
                .load(Uri.parse(user.getProfileImage()))
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(profileImage);
    }

    @Override
    @OnClick(R.id.btn_resume_login)
    public void onLoginClicked() {
        String password = passwordInput.getText().toString().trim();
        if(!isConnected){
            showSnackBar("There's no internet connection.");
        } else {
            if(password.isEmpty()){
                passwordInput.setError("Enter a valid password.");
            } else{
                mPresenter.Login(mUser.getEmail(), password);
            }
        }
    }

    @Override
    @OnClick(R.id.btn_new_login)
    public void onNewLoginClicked() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    @OnClick(R.id.btn_create_account)
    public void onRegisterClicked() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    @OnClick(R.id.btn_forgot_password)
    public void onForgotPasswordClicked() {
        startActivity(new Intent(this, RecoverActivity.class));

    }
}
