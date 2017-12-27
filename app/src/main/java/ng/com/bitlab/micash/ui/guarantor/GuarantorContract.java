package ng.com.bitlab.micash.ui.guarantor;

import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseDataListener;
import ng.com.bitlab.micash.models.Guarantee;

/**
 * Created by Paul Tonga on 21/12/2017.
 */

public interface GuarantorContract {

    interface View {

        void showLoadingLayout();

        void showEmptyDataLayout();

        void showRecyclerView(List<Guarantee> guarantees);

    }

    interface Presenter {

        void fetchGuarantorRequests();

        void getSingleRequest();

        void approveRequest();

        void rejectRequest();
    }

    interface Repository{

        void loadGuarantorRequests(String uuid, FirebaseDataListener listener);
    }
}
