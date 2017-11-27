package ng.com.bitlab.micash.ui.addBanking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.ui.addContact.AddContactActivity;
import ng.com.bitlab.micash.utils.CreditCardEditText;
import ng.com.bitlab.micash.utils.CreditCardExpiryTextWatcher;
import ng.com.bitlab.micash.utils.CreditCardFormattingTextWatcher;
import ng.com.bitlab.micash.utils.IMMResult;

public class AddBankingActivity extends AppCompatActivity {

    @BindView(R.id.card_number_input) CreditCardEditText cardNumber;
    @BindView(R.id.card_validity_input) CreditCardEditText cardExpiry;
    @BindView(R.id.card_cvc_input) CreditCardEditText cardCVC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banking);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Banking Details");


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cardCVC.setOnBackButtonListener(new CreditCardEditText.BackButtonListener() {
            @Override
            public void onBackClick() {
                InputMethodManager imm = (InputMethodManager) AddBankingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(isSoftKeyboardShown(imm, cardCVC)) {
                    //close the softkeyboard
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }else{
                    onBackPressed();
                }
            }
        });

        cardNumber.addTextChangedListener(new CreditCardFormattingTextWatcher(cardNumber));
        cardNumber.setOnBackButtonListener(new CreditCardEditText.BackButtonListener() {
            @Override
            public void onBackClick() {
                InputMethodManager imm = (InputMethodManager) AddBankingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(isSoftKeyboardShown(imm, cardNumber)) {
                    //close the softkeyboard
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }else{
                    onBackPressed();
                }
            }
        });
        cardExpiry.addTextChangedListener(new CreditCardExpiryTextWatcher(cardExpiry));
        cardExpiry.setOnBackButtonListener(new CreditCardEditText.BackButtonListener() {
            @Override
            public void onBackClick() {
                InputMethodManager imm = (InputMethodManager) AddBankingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(isSoftKeyboardShown(imm, cardExpiry)) {
                    //close the softkeyboard
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }else{
                    onBackPressed();
                }
            }
        });
    }

    @OnClick(R.id.banking_save_continue_button)
    public void submitAndContinueClicked(){

        startActivity(new Intent(this, MainActivity.class));
    }

    public boolean isSoftKeyboardShown(InputMethodManager imm, View v) {

        IMMResult result = new IMMResult();
        int res;

        imm.showSoftInput(v, 0, result);

        // if keyboard doesn't change, handle the keypress
        res = result.getResult();
        if (res == InputMethodManager.RESULT_UNCHANGED_SHOWN ||
                res == InputMethodManager.RESULT_UNCHANGED_HIDDEN) {

            return true;
        }
        else
            return false;

    }
}
