package ng.com.bitlab.micash.ui.addEmployment;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.satsuware.usefulviews.LabelledSpinner;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import faranjit.currency.edittext.CurrencyEditText;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.models.Profile;
import ng.com.bitlab.micash.ui.addBanking.AddBankingActivity;
import ng.com.bitlab.micash.ui.common.BaseView;

public class AddEmploymentActivity extends BaseView implements AddEmploymentContract.View {

    @BindView(R.id.status_spinner) LabelledSpinner statusSpinner;
    @BindView(R.id.state_spinner) LabelledSpinner statesSpinner;

    @BindView(R.id.employment_bank_branch_layout) TextInputLayout branchLayout;
    @BindView(R.id.employment_name_layout) TextInputLayout businessNameLayout;

    @BindView(R.id.input_name) EditText nameInput;
    @BindView(R.id.input_address) EditText addressInput;
    @BindView(R.id.input_salary) CurrencyEditText incomeInput;
    @BindView(R.id.input_residential) EditText residentialInput;



    @BindView(R.id.error_title) TextView errorText;


    @BindView(R.id.root_layout) RelativeLayout rootLayout;
    @BindView(R.id.error_layout) RelativeLayout errorLayout;
    //@BindView(R.id.processing_layout) RelativeLayout processingLayout;
    @BindView(R.id.employment_details) RelativeLayout employmentLayout;

    AddEmploymentContract.Presenter mPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employment);
        ButterKnife.bind(this);

        mPresenter = new AddEmploymentPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Employment Details");


            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        rootLayout.clearFocus();

            initSpinner();
            initStatesSpinner();

            statusSpinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                @Override
                public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                    switch (position) {
                        case 0:
                            //do nothing
                            break;
                        case 1:
                            //Banker
                            //nameInput.setHint("Bank Name");
                            //addressInput.setHint("Bank Branch/Address");
                            businessNameLayout.setHint("Bank Name");
                            branchLayout.setHint("Bank Branch/Address");
                            break;
                        case 2:
                            //nameInput.setHint("Office Name");
                            //addressInput.setHint("Address");
                            businessNameLayout.setHint("Office Name");
                            branchLayout.setHint("Address");
                            break;
                        case 3:
                            //nameInput.setHint("Business Name");
                            //addressInput.setHint("Address");
                            businessNameLayout.setHint("Business Name");
                            branchLayout.setHint("Address");
                            break;
                        case 4:
                            //nameInput.setHint("School Name");
                            //addressInput.setHint("Address");
                            businessNameLayout.setHint("School Name");
                            branchLayout.setHint("Address");
                            break;
                    }
                }

                @Override
                public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

                }
            });

    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.employment_status_array,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //statusSpinner.setAdapter(adapter);
    }

    private void initStatesSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states_array,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //statesSpinner.setAdapter(adapter);
    }


    @Override
    public void showErrorLayout(String error) {
        //processingLayout.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.VISIBLE);
        employmentLayout.setVisibility(View.INVISIBLE);
        errorText.setText(error);
    }

    @Override
    public void startNextActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    @OnClick(R.id.employ_save_continue_button)
    public void onSubmitClicked() {

        Profile profile = new Profile();
        profile.setAddress(addressInput.getText().toString());
        profile.setResidential(residentialInput.getText().toString());
        profile.setStatus(statusSpinner.getSpinner().getSelectedItem().toString());
        profile.setState(statesSpinner.getSpinner().getSelectedItem().toString());
        profile.setOffice(nameInput.getText().toString());
        try {
            profile.setIncome(incomeInput.getCurrencyText());
        } catch (ParseException e) {
            e.printStackTrace();
        }



        mPresenter.saveProfile(profile);

    }

    @Override
    public boolean areInputsValid() {
        return false;
    }
}
