package ng.com.bitlab.micash.ui.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;

import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.ui.intro.IntroActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends BaseView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);


    }


    public void showIntro(View view) {
        Intent i = new Intent(this, IntroActivity.class);
        startActivity(i);
        finish();


    }
}
