package ng.com.bitlab.micash.ui.upload;

import android.net.Uri;

import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by Paul on 08/08/2017.
 */

public interface UploadContract {

    interface View extends IBaseVew {

        void selectProfileImage();

        void uploadButtonClicked();

        void getStartedButtonClicked();

        void showGetStartedLayout();

        void showImageUploadLayout();

        void startMainActivity();
    }

    interface Presenter extends IBasePresenter<View> {

        void uploadProfileImage(byte[] data);

        void saveImageToProfile(Uri s);

        String getImageName();

        void saveUser();


    }

    interface Repository {

    }
}
