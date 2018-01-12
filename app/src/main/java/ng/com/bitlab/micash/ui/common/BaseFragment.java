package ng.com.bitlab.micash.ui.common;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;

import ng.com.bitlab.micash.R;

/**
 * Created by paul on 1/9/18.
 */

public abstract class BaseFragment extends Fragment implements IBaseVew {

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
                    isConnected = true;
                } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                    isConnected = false;
                }
            }
        }
    };


    @Override
    public void showDialog(String message) {

        mDialog = new ProgressDialog(getContext());
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(message);
        mDialog.setIndeterminate(true);

        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        dialogShowing = true;
        mDialog.show();
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
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

}
