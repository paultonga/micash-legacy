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
                boolean isIntroduced = sp.getBoolean(Constants.INTRODUCED, false);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    //user is present and has been introduced
                    //go to main activity
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                } else {
                    if(isIntroduced){
                        startActivity(new Intent(SplashScreenActivity.this, RegisterActivity.class));
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, WelcomeActivity.class));
                        savePreferences();
                    }
                }

                    }
        }, SPLASH_DISPLAY_LENGTH);

    }

    public void savePreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Constants.IS_FIRST_TIME, false);
    }

}

