package ng.com.bitlab.micash.ui.addBanking;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseDataListener;
import ng.com.bitlab.micash.models.Account;
import ng.com.bitlab.micash.models.AccountRecord;
import ng.com.bitlab.micash.models.Bank;
import ng.com.bitlab.micash.models.BankRecord;
import ng.com.bitlab.micash.models.Card;
import ng.com.bitlab.micash.models.Guarantee;
import ng.com.bitlab.micash.models.Guarantor;
import ng.com.bitlab.micash.models.Interest;
import ng.com.bitlab.micash.models.Loan;
import ng.com.bitlab.micash.models.Request;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.ui.common.IBaseVew;

/**
 * Created by paul on 12/3/17.
 */

public interface AddBankingContract {

    interface View extends IBaseVew{

        void showErrorLayout(String error);

        void startNextActivity();

        void onSubmitClicked();

        Loan getLoanFromBundle();

        void showProcessingLayout(String message);

        void showSuccessLayout();

        void updateProcessingMessage(String message);

        void showDialogMessage(String message);

        void initializeView(Loan loan);

        void initializeInterestSpinner();

        void initializeAccountSpinner();
    }

    interface Presenter extends IBasePresenter<View> {
        void saveBanking(Bank bank);

        Loan getLoan(String loanID);

        List<Interest> getInterests(String LoanID);

        List<BankRecord> getAccounts();

        void startLoanRequest(String amount, Loan loan, Interest interest, Bank bank, String email);


    }

    interface Repository {

        void getGuarantor(String emailMD5, FirebaseDataListener listener);

        void sendGuarantorRequest(Guarantor guarantor, Guarantee guarantee, FirebaseDataListener listener);

        void getGuarantorCount(String guarantor_uuid, FirebaseDataListener listener);

        void checkUserLoan(String userID, FirebaseDataListener listener);

        void submitLoanRequest(String userID, Request request, FirebaseDataListener listener);

        void saveBankAccount(String userID, Bank bank, FirebaseDataListener listener);

        void checkGuarantorEmail(String emailMD5, FirebaseDataListener listener);

    }
}
