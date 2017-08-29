package ng.com.bitlab.micash.ui.resume;

import com.google.firebase.auth.FirebaseUser;

import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by Paul on 18/08/2017.
 */

public interface ResumeContract {

    interface View extends IBaseVew {

        void onLoginClicked();

        void onNewLoginClicked();

        void onRegisterClicked();

        void startMainActivity();

        void updateUI(User user);
    }

    interface  Presenter extends IBasePresenter<View> {

        void Login(String email, String password);

        User getUserFromPreference();

        void saveUserToPreference(FirebaseUser user);
    }
}
