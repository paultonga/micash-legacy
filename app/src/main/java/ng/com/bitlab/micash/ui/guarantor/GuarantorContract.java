package ng.com.bitlab.micash.ui.guarantor;

import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseDataListener;
import ng.com.bitlab.micash.models.Guarantee;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by Paul Tonga on 21/12/2017.
 */

public interface GuarantorContract {

    interface View extends IBaseVew {

        void showLoadingLayout();

        void showEmptyDataLayout();

        void showRecyclerView(List<Guarantee> guarantees);

    }

    interface Presenter extends IBasePresenter<View> {

        void fetchGuarantorRequests();

        void getSingleRequest();

        void approveRequest();

        void rejectRequest();
    }

    interface Repository{

        void loadGuarantorRequests(String uuid, FirebaseDataListener listener);
    }
}
