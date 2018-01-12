package ng.com.bitlab.micash.ui.transactions;

import java.util.List;

import ng.com.bitlab.micash.models.Request;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by paul on 1/6/18.
 */

public interface TransactionsContract {

    interface View extends IBaseVew {

        void showEmptyLayout();

        void showTransactionList(List<Request> requests);

        void showLoadingLayout();
    }

    interface Presenter extends IBasePresenter<View>{

        void loadTransactions();

        void deleteTransaction(Request request);
    }
}
