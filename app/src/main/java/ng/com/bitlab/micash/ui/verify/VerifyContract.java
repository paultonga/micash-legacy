package ng.com.bitlab.micash.ui.verify;

import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by Paul on 25/07/2017.
 */

public interface VerifyContract {

    interface View extends IBaseVew {

        void onRequestCodeClicked();

        void onVerifyClicked();

        void onResendCodeClicked();

        void showVerificationLayout();

        void updateVerificationLayout(String code);

        void gotoUploadActivity();

        void showPhoneNumberLayout();

        String getNoticeString(String phone);

        void showEditNumberDialog();

    }

    interface Presenter extends IBasePresenter<View> {

        void startPhoneVerification(String mPhone);

        boolean validateCode(String code);

        boolean validatePhoneNumber(String phone);


        String parsePhoneNumber(String phone);

        void resendCode();

        void verifyCode(String code);

        void startAutoVerification(String code);


        void resume();
    }

    interface Repository {

    }
}
