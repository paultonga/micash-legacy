package ng.com.bitlab.micash.ui.recover;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.ui.login.LoginActivity;

public class RecoverActivity extends BaseView implements RecoverContract.View {

    @BindView(R.id.input_forgot_email) EditText passwordInput;
    @BindView(R.id.btn_reset_password) Button passwordResetButton;

    RecoverContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        ButterKnife.bind(this);
        mPresenter = new RecoverPresenter(this);

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
    }

    @Override
    @OnClick(R.id.btn_reset_password)
    public void resetPasswordClicked() {
        String email = passwordInput.getText().toString().trim();
        if(!isConnected){
            showSnackBar("There's no internet connection.");
        } else{
            if(!mPresenter.isValidEmail(email)){
                passwordInput.setError("Enter a valid email.");
            } else {
                mPresenter.requestPasswordReset(email);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void startLoginActivity() {
        this.finish();
    }
}
