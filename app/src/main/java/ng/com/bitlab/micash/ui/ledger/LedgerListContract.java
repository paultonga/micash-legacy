package ng.com.bitlab.micash.ui.ledger;

import java.util.List;

import ng.com.bitlab.micash.models.Ledger;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by Paul Tonga on 07/02/2018.
 */

public interface LedgerListContract {

    interface View extends IBaseVew {
        void onTouchPaidLedger();

        void onTouchUnpaidLedger();

        void showEmptyLayout();

        void showLoadingLayout();

        void showContentLayout();

        void showUnpaidLedgers(List<Ledger> unpaidLedgers);

        void showPaidLedgers(List<Ledger> paidLedgers);
    }

    interface Presenter extends IBasePresenter<View> {
        void getPaidLedgers();

        void getUnpaidLedgers();

        void setLedgerAsPaid(Ledger ledger);


        void loadData();
    }
}
