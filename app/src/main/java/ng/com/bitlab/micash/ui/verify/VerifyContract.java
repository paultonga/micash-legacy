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

        void onSetProfilePictureClicked();

        void onSkipProfilePictureClicked();

        void showVerificationLayout();

        void showSetProfilePictureLayout();

        void showCompletedLayout();

        void showPhoneNumberLayout();

        void startMainActivity();

        void selectProfileImage();

        void getStartedClicked();


    }

    interface Presenter extends IBasePresenter<View> {

        void startPhoneVerification(String mPhone);

        boolean validateCode(String code);

        boolean validatePhoneNumber(String phone);

        void setProfilePicture();

        void uploadProfileImage(byte[] data);

        String parsePhoneNumber(String phone);

        void resendCode();

        void verifyAndMergeCredentials(String code);



    }

    interface Repository {

    }
}
