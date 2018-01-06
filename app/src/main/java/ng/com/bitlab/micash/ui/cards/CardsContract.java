package ng.com.bitlab.micash.ui.cards;

import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.AccountRecord;
import ng.com.bitlab.micash.models.Bank;
import ng.com.bitlab.micash.models.BankRecord;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by paul on 12/3/17.
 */

public interface CardsContract {

    interface View extends IBaseVew {

        void showEmptyLayout();

        void showCards(List<BankRecord> records);

        void showRecords(List<Bank> records);

        void showAddDialog();

        void showLoadingLayout();

    }

    interface Presenter extends IBasePresenter<View>{

        void loadData();

        void fetchData();

        void saveAccount(Bank bank, String userID);

        void deleteRecord(Bank bank);

        void checkAccount(Bank bank);
    }

    interface Repository {
        void fetchRecords(String userUUID, FirebaseQueryListener listener);

        void saveRecord(Bank bank, String userUUID, FirebaseQueryListener listener);

        void getRecord(Bank bank, String userUUID, FirebaseQueryListener listener);
    }
}
