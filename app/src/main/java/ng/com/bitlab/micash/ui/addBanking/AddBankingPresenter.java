package ng.com.bitlab.micash.ui.addBanking;

import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.transition.Visibility;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.joda.time.DateTime;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.data.SampleLoansData;
import ng.com.bitlab.micash.listeners.FirebaseDataListener;
import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
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
import ng.com.bitlab.micash.ui.common.BasePresenter;
import ng.com.bitlab.micash.ui.common.IBasePresenter;
import ng.com.bitlab.micash.utils.Constants;

/**
 * Created by paul on 12/3/17.
 */

public class AddBankingPresenter extends BasePresenter<AddBankingContract.View> implements AddBankingContract.Presenter {

    AppPreference mPref;
    SampleLoansData mData;

    private boolean isValid;
    private long count = 0;
    private AddBankingContract.Repository mRepository;


    public AddBankingPresenter(AddBankingContract.View view) {
        this.view = view;
        mData = new SampleLoansData();
        mRepository = new AddBankingRepository();
    }


    @Override
    public void saveBanking(final Bank bank) {

        if (view.isNewAccount()) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            mRepository.saveBankAccount(userID, bank, new FirebaseDataListener() {
                @Override
                public void onStart() {
                    view.updateProcessingMessage("Saving your banking details and finalizing");
                }

                @Override
                public void onSuccess(DataSnapshot ds) {

                }

                @Override
                public void onFailed(DatabaseError de) {
                    view.showErrorLayout(de.getMessage());
                }

                @Override
                public void onComplete(DatabaseError de) {
                    if (de == null) {
                        view.showSuccessLayout();
                    } else {
                        view.showErrorLayout(de.getMessage());
                    }
                }
            });
        }else {
            view.showSuccessLayout();
        }
    }

    @Override
    public Loan getLoan(String loanID) {
        SampleLoansData loans = new SampleLoansData();
        return loans.getLoan(loanID);
    }

    @Override
    public List<Interest> getInterests(String LoanID) {
        SampleLoansData loans = new SampleLoansData();
        return loans.getInterestForLoan(LoanID);
    }

    @Override
    public void getAccounts() {

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRepository.getBankAccounts(uuid, new FirebaseQueryListener() {
            @Override
            public void onStart() {
                view.showLoadingAccount();
            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                if (databaseError == null){
                    List<Bank> banks = new ArrayList<>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        Bank bank = ds.getValue(Bank.class);
                        banks.add(bank);
                    }
                    view.initializeAccountSpinner(banks);
                } else {
                    view.initializeAccountSpinner(null);
                    view.showToast(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void startLoanRequest(final String amount, final Loan loan, final Interest interest, final Bank bank, final String email) {

        view.showProcessingLayout("Initializing...");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRepository.checkUserLoan(userID, new FirebaseDataListener() {
            @Override
            public void onStart() {
                view.updateProcessingMessage("Checking your eligibility...");
            }

            @Override
            public void onSuccess(DataSnapshot ds) {
                if(ds.exists()){
                    view.showErrorLayout("Sorry, you already have an active loan that is either being processed or pending refund.");
                } else {
                    isEmailValid(email, amount, loan, interest, bank);
                }
            }

            @Override
            public void onFailed(DatabaseError de) {
                view.showErrorLayout(de.getMessage());
            }

            @Override
            public void onComplete(DatabaseError de) {

            }
        });

    }

    private void sendGuarantorRequest(final Guarantor guarantor, final String amount, final Loan loan, final Interest interest, final Bank bank) {
        Guarantee guarantee = new Guarantee();
        guarantee.setAmount(amount);
        guarantee.setEmail(guarantor.getEmail());
        guarantee.setUuid(guarantor.getUuid());
        guarantee.setToken(guarantor.getInstanceID());
        guarantee.setRequester_name(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        guarantee.setRequester_uuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        guarantee.setDate_created(DateTime.now().getMillis());
        guarantee.setDecided(false);
        guarantee.setApproved(false);

        mRepository.sendGuarantorRequest(guarantor, guarantee, new FirebaseDataListener() {
            @Override
            public void onStart() {
                view.updateProcessingMessage("Sending request to your guarantor");
            }

            @Override
            public void onSuccess(DataSnapshot ds) {

            }

            @Override
            public void onFailed(DatabaseError de) {

            }

            @Override
            public void onComplete(DatabaseError de) {
                if(de == null){
                    Request req = new Request();
                    req.setAmount(amount);
                    req.setLoan(loan);
                    req.setInterest(interest);
                    req.setBank(bank);
                    req.setGuarantor(guarantor.getUuid());
                    req.setDate_created(DateTime.now().getMillis());
                    req.setToken(FirebaseInstanceId.getInstance().getToken());
                    req.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    req.setApproved(false);
                    req.setRepaid(false);
                    req.setNew(true);
                    req.setDecided(false);
                    requestLoan(req);
                } else {
                    view.showErrorLayout(de.getMessage());
                }

            }
        });


    }

    private void requestLoan(final Request req) {
        mRepository.submitLoanRequest(req.getUser_id(), req, new FirebaseDataListener() {
            @Override
            public void onStart() {
                view.updateProcessingMessage("Requesting your loan...");
            }

            @Override
            public void onSuccess(DataSnapshot ds) {

            }

            @Override
            public void onFailed(DatabaseError de) {
                view.showErrorLayout(de.getMessage());
            }

            @Override
            public void onComplete(DatabaseError de) {
                if(de == null){
                    saveBanking(req.getBank());
                } else {
                    view.showErrorLayout(de.getMessage());
                }
            }
        });
    }

    private void getGuarantor(final String emailMD5, final String amount, final Loan loan, final Interest interest, final Bank bank) {

        mRepository.getGuarantor(emailMD5, new FirebaseDataListener() {
            @Override
            public void onStart() {
                view.updateProcessingMessage("Checking guarantor details...");
            }

            @Override
            public void onSuccess(DataSnapshot ds) {
                Guarantor g = new Guarantor();
                g.setEmail(emailMD5);
                g.setUuid(ds.child("uuid").getValue(String.class));
                g.setInstanceID(ds.child("instanceID").getValue(String.class));
                guaranteeCount(g.getUuid(), g, amount, loan, interest, bank);
            }

            @Override
            public void onFailed(DatabaseError de) {
                view.showErrorLayout(de.getMessage());
            }

            @Override
            public void onComplete(DatabaseError de) {

            }
        });

    }

    private void guaranteeCount(String guarantor_uuid, final Guarantor g, final String amount, final Loan loan, final Interest interest, final Bank bank){

        mRepository.getGuarantorCount(guarantor_uuid, new FirebaseDataListener() {
            @Override
            public void onStart() {
                view.updateProcessingMessage("Checking guarantor details...");
            }

            @Override
            public void onSuccess(DataSnapshot ds) {
                long count = ds.getChildrenCount();
                if(count >= 0 && count < 3){
                    sendGuarantorRequest(g, amount, loan, interest,bank);
                } else {
                    view.showErrorLayout("Your guarantor has reached maximum number of requests. Please yse a different guarantor.");
                }
            }

            @Override
            public void onFailed(DatabaseError de) {
                view.showErrorLayout(de.getMessage());
            }

            @Override
            public void onComplete(DatabaseError de) {

            }
        });

    }

    private void isEmailValid(final String email, final String amount, final Loan loan, final Interest interest, final Bank bank){
        final String email_md5 = getMD5(email);
        mRepository.checkGuarantorEmail(email_md5, new FirebaseDataListener() {
            @Override
            public void onStart() {
                view.updateProcessingMessage("Checking guarantor's email address");
            }

            @Override
            public void onSuccess(DataSnapshot ds) {
                if(ds.exists()) {
                    getGuarantor(email_md5, amount, loan, interest, bank);
                } else {
                    view.showErrorLayout("The guarantor email you provided is not registered on miCash.");
                }
            }

            @Override
            public void onFailed(DatabaseError de) {

            }

            @Override
            public void onComplete(DatabaseError de) {

            }
        });

    }

    private static String getMD5(String email) {

        StringBuffer _sb = new StringBuffer();

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(email.getBytes());

            byte byteData[] = md.digest();

            for(int i = 0; i<byteData.length; i++){
                _sb.append(Integer.toString((byteData[i] & 0xff)+0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return _sb.toString();
    }

    private void setIsValid(boolean value){
        isValid = value;
    }

    private boolean getIsValid(){ return isValid; }

    private void setCount(long value){ count = value; }

    private long getCount(){ return count; }



}
