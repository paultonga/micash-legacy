package ng.com.bitlab.micash.ui.loans;

import ng.com.bitlab.micash.models.Loan;

/**
 * Created by paul on 11/23/17.
 */

public interface LoanDetailsContract {

    interface Presenter {

        void loadLoan();

        void requestLoan(Loan loan);

    }

    interface View {

        void onRequestLoanClicked();

        void showIncompleteProfileError();

        void showIncompleteBankingError();

        void showBankingInputDialog();

        void ahowPersonalDetailInputDialog();
    }
}
