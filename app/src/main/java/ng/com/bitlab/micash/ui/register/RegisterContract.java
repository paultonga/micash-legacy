package ng.com.bitlab.micash.ui.register;

import com.google.firebase.auth.FirebaseUser;

import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by Paul on 22/07/2017.
 */

public interface RegisterContract {

    interface View extends IBaseVew {

        void onRegisterClicked();

        void onTermsAndConditionsClicked();

        boolean validateInputs();

        void startVerifyActivity();

    }

    interface Presenter extends IBasePresenter<View> {


        boolean isValidEmail(String email);

        boolean isValidName(String name);

        boolean isValidPassword(String password);

        void createFirebaseUser(String email, String password, String displayName);

        void updateDisplayName(String name, FirebaseUser user);

    }

    interface Repository{

    }
}
