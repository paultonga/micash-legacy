package ng.com.bitlab.micash.ui.addEmployment;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.ui.addContact.AddContactActivity;

public class AddEmploymentActivity extends AppCompatActivity {

    @BindView(R.id.employment_status_spinner)
    AppCompatSpinner employmentStatus;
    @BindView(R.id.employment_state_spinner)
    Spinner statesSpinner;
    @BindView(R.id.banker_checkbox)
    CheckBox bankerCheckbox;
    @BindView(R.id.employment_bank_branch_layout)
    TextInputLayout branchLayout;
    @BindView(R.id.employment_name_layout) TextInputLayout businessNameLayout;
    @BindView(R.id.input_business_name)
    EditText businessNameInput;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employment);
        ButterKnife.bind(this);

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

        bankerCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!compoundButton.isChecked()){
                    branchLayout.setVisibility(View.GONE);
                    businessNameLayout.setHint("Office or Business Name");

                } else {
                    branchLayout.setVisibility(View.VISIBLE);
                    businessNameLayout.setHint("Bank Name");

                }
            }
        });



    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.employment_status_array,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employmentStatus.setAdapter(adapter);
    }

    private void initStatesSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states_array,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(adapter);
    }

    @OnClick(R.id.employ_save_continue_button)
    public void submitAndContinueClicked(){

        startActivity(new Intent(this, AddContactActivity.class));
    }
}
