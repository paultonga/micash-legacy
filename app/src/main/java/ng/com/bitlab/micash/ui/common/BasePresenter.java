package ng.com.bitlab.micash.ui.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Created by Paul on 22/07/2017.
 */

public class BasePresenter<ViewT> implements IBasePresenter<ViewT> {

    protected ViewT view;

    @Override
    public void onViewActive(ViewT view) {
        this.view = view;
    }

    @Override
    public void onViewInactive() {
        this.view = null;
    }

    @Override
    public boolean isValidEmail(String email) {
        return (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }

    @Override
    public boolean isValidPassword(String password) {
        return (password != null  && password.length() > 6);
    }

}
