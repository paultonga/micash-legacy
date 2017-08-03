package ng.com.bitlab.micash.ui.common;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


/**
 * Created by Paul on 22/07/2017.
 */

public abstract class BaseView extends AppCompatActivity implements IBaseVew  {

    MaterialDialog.Builder mBuilder;
    boolean dialogShowing = false;
    protected boolean isConnected = false;

    static ProgressDialog mDialog;

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras() != null) {
                final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

                if (ni != null && ni.isConnectedOrConnecting()) {
                    //Log.i(TAG, "Network " + ni.getTypeName() + " connected");
                    isConnected = true;
                    showSnackBar("You're connected :)");
                } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                    //Log.d(TAG, "There's no network connectivity");
                    isConnected = false;
                    //showSnackBar("You've lost internet connection.");
                }
            }
        }
    };



    @Override
    public void showDialog(String message) {

        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(message);
        mDialog.setIndeterminate(true);

        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        dialogShowing = true;
        mDialog.show();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    public void hideDialog() {
        if(mDialog != null && mDialog.isShowing() && dialogShowing) {
            mDialog.dismiss();
        }
        dialogShowing = false;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

}
