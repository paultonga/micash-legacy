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

        void showTermsAndConditions();

    }

    interface Presenter extends IBasePresenter<View> {

        boolean isValidName(String name);

        void createFirebaseUser(String email, String password, String displayName);

        void updateDisplayName(String name, FirebaseUser user);

        void checkEmail(String email, String password, String name);

    }

    interface Repository{

    }
}
