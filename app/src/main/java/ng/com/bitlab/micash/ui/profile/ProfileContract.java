package ng.com.bitlab.micash.ui.profile;

import android.net.Uri;

import ng.com.bitlab.micash.models.Profile;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by paul on 11/23/17.
 */

public interface ProfileContract {

    interface View extends IBaseVew {
        void showAddEmploymentDialog();

        void showEmptyLayout();

        void showDataLayout(Profile profile);

        void setPhoneNumber(String phone);

        void onProfileImageTouched();

        void showLoadingLayout();

        void refreshImage();

    }

    interface Presenter extends IBasePresenter<View> {

        void getProfile();

        String getPhoneNumber();

        void uploadProfileImage(byte[] data);

        String getImageName();

        void updateUserProfile(Uri s);

        void updateUserAccount(String uri);

    }
}
