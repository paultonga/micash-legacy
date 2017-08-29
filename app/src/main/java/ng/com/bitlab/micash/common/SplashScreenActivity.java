package ng.com.bitlab.micash.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.ui.intro.IntroActivity;
import ng.com.bitlab.micash.ui.login.LoginActivity;
import ng.com.bitlab.micash.ui.register.RegisterActivity;
import ng.com.bitlab.micash.ui.resume.ResumeActivity;
import ng.com.bitlab.micash.ui.upload.UploadActivity;
import ng.com.bitlab.micash.ui.verify.VerifyActivity;
import ng.com.bitlab.micash.ui.welcome.WelcomeActivity;
import ng.com.bitlab.micash.utils.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
                String appState = sp.getString(Constants.APP_STATE, "");
                String jsonUser = sp.getString(Constants.USER, null);

                if(jsonUser != null){
                    Intent intent = new Intent(SplashScreenActivity.this, ResumeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {

                    switch (appState) {
                        case "":
                            gotoWelcome();
                            break;
                        case Constants.START_APP_INTRO:
                            gotoAppIntro();
                            break;
                        case Constants.START_REGISTER:
                            gotoRegisterActivity();
                            break;
                        case Constants.START_PHONE_VERIFICATION:
                            gotoPhoneVerificationView();
                            break;
                        case Constants.START_UPLOAD_IMAGE:
                            gotoUploadImageView();
                            break;
                        case Constants.GET_STARTED_VIEW:
                            gotoGetStartedView();
                            break;
                        case Constants.DONE:
                            gotoLoginActivity();
                            break;
                        default:
                            gotoWelcome();

                    }
                }
                        
                    }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void gotoAppIntro() {
        startActivity(new Intent(this, IntroActivity.class));
        finish();
    }

    private void gotoPhoneVerificationView() {
        Intent i = new Intent(this, VerifyActivity.class);
        startActivity(i);
        finish();
    }
    

    private void gotoRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private void gotoWelcome() {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    private void gotoUploadImageView() {
        Intent i = new Intent(this, UploadActivity.class);
        startActivity(i);
        finish();
    }

    private void gotoGetStartedView() {
        Intent i = new Intent(this, UploadActivity.class);
        startActivity(i);
        finish();
    }

    private void gotoLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


}

