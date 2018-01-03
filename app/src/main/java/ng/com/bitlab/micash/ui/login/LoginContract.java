package ng.com.bitlab.micash.ui.login;

import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by Paul on 15/08/2017.
 */

public interface LoginContract {

    interface View extends IBaseVew {

        void onLoginClicked();


        void onForgotPasswordClicked();


        void startRegisterActivity();

        void startMainActivity();


    }

    interface Presenter extends IBasePresenter<View> {


        void Login(String email, String password);

        User getUserFromPreference();


    }

    interface Repository {

    }
}
