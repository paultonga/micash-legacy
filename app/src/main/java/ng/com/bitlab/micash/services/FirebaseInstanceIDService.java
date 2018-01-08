package ng.com.bitlab.micash.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.utils.Utility;

/**
 * Created by paul on 11/11/17.
 */


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private AppPreference mPref;
    @Override
    public void onTokenRefresh() {
        mPref = MiCashApplication.getPreference();
        String token = FirebaseInstanceId.getInstance().getToken();
        //Log.d("Token", "Refreshed token: " + token);
        mPref.setToken(token);
        Utility.updateInstanceID();
    }
}
