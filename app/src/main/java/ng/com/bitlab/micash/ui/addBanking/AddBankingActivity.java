package ng.com.bitlab.micash.ui.addBanking;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.satsuware.usefulviews.LabelledSpinner;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.models.Bank;
import ng.com.bitlab.micash.models.Interest;
import ng.com.bitlab.micash.models.Loan;
import ng.com.bitlab.micash.ui.common.BaseView;

import static java.lang.Long.parseLong;

public class AddBankingActivity extends BaseView implements AddBankingContract.View {

    AddBankingContract.Presenter mPresenter;


    @BindView(R.id.input_bank_name) EditText bankName;
    @BindView(R.id.input_bank_account) EditText bankAccount;
    @BindView(R.id.input_bank_bvn) EditText bankBVN;

    @BindView(R.id.processing_layout) RelativeLayout processingLayout;
    @BindView(R.id.processing_detail) TextView processingMessage;

    @BindView(R.id.success_layout) RelativeLayout successLayout;

    @BindView(R.id.bank_layout) LinearLayout bankLayout;
    @BindView(R.id.error_layout) RelativeLayout errorLayout;
    @BindView(R.id.error_title) TextView errorText;

    @BindView(R.id.select_account_layout) RelativeLayout selectAccountLayout;
    @BindView(R.id.loading_account_layout) RelativeLayout loadingAccountLayout;
    @BindView(R.id.enter_account_layout) LinearLayout enterAccountLayout;

    @BindView(R.id.select_interest_spinner) LabelledSpinner interestSpinner;
    @BindView(R.id.select_account_spinner) LabelledSpinner accounttSpinner;

    @BindView(R.id.guarantor_name) EditText guarantorACTV;
    @BindView(R.id.guarantor_name_layout) TextInputLayout guarantorLayout;

    @BindView(R.id.input_request_amount) EditText amountInput;
    @BindView(R.id.loan_repay) TextView loanRepay;
    @BindView(R.id.loan_period) TextView loanPeriod;
    @BindView(R.id.loan_total) TextView loanTotal;



    Loan mLoan;
    Interest mInterest;
    List<Interest> mInterests;
    List<Bank> mBanks;
    Bank mBank;
    private static String naira = "\u20a6";
    private boolean isNewAccount = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banking);

        ButterKnife.bind(this);
        mPresenter = new AddBankingPresenter(this);

        mLoan = getLoanFromBundle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(mLoan != null) {
            getSupportActionBar().setTitle(mLoan.getTitle());
            getSupportActionBar().setSubtitle(mLoan.getDescription());
        } else {
            getSupportActionBar().setTitle("Error");
            getSupportActionBar().setSubtitle("Unable to load data.");
            errorLayout.setVisibility(View.VISIBLE);
            bankLayout.setVisibility(View.GONE);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mInterests = mPresenter.getInterests(mLoan.getId());
        mInterest = mInterests.get(0);
        //mBanks = mPresenter.getAccounts();
        if(mBanks != null && !mBanks.isEmpty())
            mBank = mBanks.get(0);


        //initializeAccountSpinner();
        mPresenter.getAccounts();
        initializeInterestSpinner();


        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    refreshView();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                amountInput.removeTextChangedListener(this);

                try {
                    String originalString = editable.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    amountInput.setText(formattedString);
                    amountInput.setSelection(amountInput.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }


                amountInput.addTextChangedListener(this);


            }
        });

        interestSpinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                mInterest = mInterests.get(position);
                refreshView();
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        accounttSpinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                mBank = mBanks.get(position);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });


    }

    @OnClick(R.id.tv_enter_new_account)
    public void enterNewAccount(){
        isNewAccount = true;
        selectAccountLayout.setVisibility(View.GONE);
        enterAccountLayout.setVisibility(View.VISIBLE);
        loadingAccountLayout.setVisibility(View.GONE);
    }

    private void refreshView(){

        String input = amountInput.getText().toString().replaceAll(",", "");
        DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = numberFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        numberFormat.setDecimalFormatSymbols(symbols);
        numberFormat.setMinimumFractionDigits(0);

        if(input.length() > 3) {

            double amountEntered = Double.parseDouble(input);
            int period = mInterest.getMonths();

            double i = mInterest.getPercentage();
            double interestAmount = amountEntered * i / 100;
            double total_interest = interestAmount * period;
            double total = amountEntered + total_interest;
            double payback = total / period;

            String t = naira + numberFormat.format(total);
            loanTotal.setText(t);

            String m = period + " months";
            loanPeriod.setText(m);

            String p = naira + numberFormat.format(payback);
            loanRepay.setText(p);
        } else {

            String t = naira + 0;
            loanTotal.setText(t);

            String m = 0 + " months";
            loanPeriod.setText(m);

            String p = naira + 0;
            loanRepay.setText(p);
        }
    }


    @Override
    public void showErrorLayout(String error) {
        bankLayout.setVisibility(View.GONE);
        processingLayout.setVisibility(View.GONE);
        successLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorText.setText(error);
    }

    @Override
    @OnClick({R.id.done_button, R.id.try_again_button})
    public void startNextActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    @OnClick(R.id.banking_save_continue_button)
    public void onSubmitClicked() {
        if(isConnected) {
            if (isAmountValid() && isEmailValid()) {
                if(getBank() != null) {
                    mPresenter.startLoanRequest(amountInput.getText().toString(),
                            mLoan, mInterest,
                            getBank(),
                            guarantorACTV.getText().toString());
                }else{
                    showToast("Please check your bank inputs. Cannot continue.");
                }
            }
        } else {
            showSnackBar("Please check your internet connection.");
        }

    }

    @Override
    public Loan getLoanFromBundle() {
        String loan_id;
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            return null;
        }else {
            loan_id = extras.getString("loan", null);
            return mPresenter.getLoan(loan_id);

        }
    }

    @Override
    public void showProcessingLayout(String message) {
        bankLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        processingLayout.setVisibility(View.VISIBLE);
        processingMessage.setText(message);
    }

    @Override
    public void showSuccessLayout() {
        bankLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        processingLayout.setVisibility(View.GONE);
        successLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateProcessingMessage(String message) {
        if(processingLayout.getVisibility() == View.VISIBLE)
            processingMessage.setText(message);
    }

    @Override
    public boolean isNewAccount(){ return isNewAccount; }

    @Override
    public void showDialogMessage(String message) {

    }

    @Override
    public void initializeView(Loan loan) {

    }

    @Override
    public void initializeInterestSpinner() {
        List<Interest> interests = mInterests;

        if(interests != null){
            List<String> items = new ArrayList<>();
            for(Interest interest : interests){
                items.add(interest.getDescription());
            }
            interestSpinner.setItemsArray(items);
        }
    }

    @Override
    public void initializeAccountSpinner(List<Bank> records) {

        if(records != null && !records.isEmpty()){
            mBanks = records;
            mBank = mBanks.get(0);
            selectAccountLayout.setVisibility(View.VISIBLE);
            enterAccountLayout.setVisibility(View.GONE);
            loadingAccountLayout.setVisibility(View.GONE);

            List<String> items = new ArrayList<>();
            for(Bank record : records){
                items.add(record.getNumber() + " - " + record.getName());
            }
            accounttSpinner.setItemsArray(items);
        } else {
            isNewAccount = true;
            selectAccountLayout.setVisibility(View.GONE);
            loadingAccountLayout.setVisibility(View.GONE);
            enterAccountLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showLoadingAccount() {
        selectAccountLayout.setVisibility(View.GONE);
        loadingAccountLayout.setVisibility(View.VISIBLE);
        enterAccountLayout.setVisibility(View.GONE);
    }

    private boolean isAmountValid() {
        if(amountInput.getText().toString().isEmpty()){
            amountInput.setError("Amount should not be empty");
            return false;
        } else {
            double max = mLoan.getMaximum();
            String string = amountInput.getText().toString().replaceAll(",", "");
            double input = Double.parseDouble(string);

            if(input > max){
                amountInput.setError("Exceeds maximum of "+ naira + max);
                return false;
            } else {
                return true;
            }
        }

    }

    private boolean isEmailValid(){
        String email = guarantorACTV.getText().toString();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        boolean flag = true;

        if (email.isEmpty()) {
            guarantorACTV.setError("Guarantor email is required.");
            flag = false;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            guarantorACTV.setError("Enter a valid email address.");
            flag = false;
        }

        if(email.equalsIgnoreCase(userEmail)){
            guarantorACTV.setError("You cannot use your email.");
            flag = false;
        }
        return flag;
    }

    private boolean validateBankInputs(){
        boolean flag = true;

        if(bankAccount.getText().length() != 10){
            bankAccount.setError("Enter a valid account");
            flag = false;
        }
        if(bankName.getText().length() < 4){
            bankName.setError("Enter a valid bank name");
            flag = false;
        }

        if(bankBVN.getText().length() != 11){
            bankBVN.setError("Enter a valid BVN");
            flag = false;
        }

        return flag;
    }

    private Bank getBank(){
        if(selectAccountLayout.getVisibility() == View.VISIBLE){
            //get account from list
            Bank bank = new Bank(mBank.getName(), mBank.getNumber(), mBank.getBvn());
            return bank;
        } else {
            if(validateBankInputs()){
                //get account from form
                Bank bank = new Bank(bankName.getText().toString(),
                        bankAccount.getText().toString(),
                        bankBVN.getText().toString());
                return bank;
            } else {
                return null;
            }
        }
    }
}
