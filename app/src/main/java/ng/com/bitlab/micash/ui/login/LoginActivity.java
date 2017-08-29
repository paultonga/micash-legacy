package ng.com.bitlab.micash.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.ui.recover.RecoverActivity;
import ng.com.bitlab.micash.ui.register.RegisterActivity;
import ng.com.bitlab.micash.utils.Constants;

public class LoginActivity extends BaseView implements LoginContract.View {




    //inputs
    @BindView(R.id.input_login_email) EditText loginEmail;
    @BindView(R.id.input_login_password) EditText loginPassword;

    //buttons
    @BindView(R.id.btn_login) Button loginButton;
    @BindView(R.id.btn_create_account) Button createAccountButton;

    LoginContract.Presenter mPresenter;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mPresenter = new LoginPresenter(this);

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


        loginPassword.setTransformationMethod(new PasswordTransformationMethod());
        loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }


    @Override
    @OnClick(R.id.btn_login)
    public void onLoginClicked() {
        if(isConnected){
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();
            if(validateLoginInputs()){
                mPresenter.Login(email, password);
            }
        } else {
            showSnackBar("There's no internet connection.");
        }
    }

    private boolean validateLoginInputs() {
        boolean flag = true;
        if(!mPresenter.isValidEmail(loginEmail.getText().toString().trim())) {
            loginEmail.setError("Enter a valid email");
            flag = false;
        }

        if(!mPresenter.isValidPassword(loginPassword.getText().toString().trim())) {
            loginPassword.setError("Enter a valid password");
            flag = false;
        }
        return flag;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }




    @Override
    @OnClick(R.id.btn_forgot_password)
    public void onForgotPasswordClicked() {
        startActivity(new Intent(this, RecoverActivity.class));
    }

    @OnClick(R.id.btn_create_account)
    @Override
    public void startRegisterActivity(){
        startActivity(new Intent(this, RegisterActivity.class));
    }

}
