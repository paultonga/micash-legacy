package ng.com.bitlab.micash.ui.loans;

import java.util.List;

import ng.com.bitlab.micash.data.SampleLoansData;
import ng.com.bitlab.micash.models.Loan;
import ng.com.bitlab.micash.ui.login.LoginContract;

/**
 * Created by Paul on 03/10/2017.
 */

public class LoansListPresenter implements LoansListContract.Presenter {

    LoansListContract.View mView;

    public LoansListPresenter(LoansListContract.View view){
        this.mView = view;
    }

    @Override
    public void loadLoans() {
        SampleLoansData data = new SampleLoansData();
        List<Loan> loans = data.getLoans();
        if(loans != null && loans.size() > 0){
            mView.showLoans(loans);
        } else {
            mView.showEmptyLayout();
        }


    }
}
