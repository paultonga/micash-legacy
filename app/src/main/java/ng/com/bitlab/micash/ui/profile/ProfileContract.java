package ng.com.bitlab.micash.ui.profile;

import ng.com.bitlab.micash.models.Profile;
import ng.com.bitlab.micash.models.ProfileRecord;

/**
 * Created by paul on 11/23/17.
 */

public interface ProfileContract {

    interface View {
        void showAddEmploymentDialog();

        void showEmptyLayout();

        void showDataLayout(Profile profile);

        void setPhoneNumber(String phone);

    }

    interface Presenter {

        void getProfile();

        String getPhoneNumber();

    }
}
