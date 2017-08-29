package ng.com.bitlab.micash.ui.recover;

import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by Paul on 18/08/2017.
 */

public interface RecoverContract {

    interface View extends IBaseVew {

        void resetPasswordClicked();

        void startLoginActivity();

    }

    interface Presenter extends IBasePresenter<View> {

        void requestPasswordReset(String email);

    }

    interface Repository {

    }
}
