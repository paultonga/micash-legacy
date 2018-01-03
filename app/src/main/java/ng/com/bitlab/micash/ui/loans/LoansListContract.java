package ng.com.bitlab.micash.ui.loans;

import java.util.List;

import ng.com.bitlab.micash.models.Loan;

/**
 * Created by Paul on 03/10/2017.
 */

public interface LoansListContract {

    interface Presenter {

        void loadLoans();

    }

    interface View {
        void showLoans(List<Loan> loans);

        void showEmptyLayout();
    }

    interface Repository {

    }
}
