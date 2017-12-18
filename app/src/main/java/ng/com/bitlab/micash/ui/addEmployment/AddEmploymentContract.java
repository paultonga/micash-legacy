package ng.com.bitlab.micash.ui.addEmployment;

import ng.com.bitlab.micash.models.Profile;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by paul on 12/2/17.
 */

public interface AddEmploymentContract {

    interface View extends IBaseVew {

        void showErrorLayout(String error);


        void startNextActivity();

        void onSubmitClicked();

        boolean areInputsValid();



    }

    interface Presenter extends IBasePresenter<View> {

        void saveProfile(Profile profile);

    }
}
