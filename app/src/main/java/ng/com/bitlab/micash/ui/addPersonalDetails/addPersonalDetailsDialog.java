package ng.com.bitlab.micash.ui.addPersonalDetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.utils.Constants;


public class addPersonalDetailsDialog extends DialogFragment {

    @BindView(R.id.employment_status_spinner) AppCompatSpinner employmentStatus;
    @BindView(R.id.employment_state_spinner) Spinner statesSpinner;
    @BindView(R.id.banker_checkbox) CheckBox bankerCheckbox;
    @BindView(R.id.employment_bank_branch_layout) TextInputLayout branchLayout;
    @BindView(R.id.employment_name_layout) TextInputLayout businessNameLayout;
    @BindView(R.id.input_business_name) EditText businessNameInput;

    public addPersonalDetailsDialog() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogFragment = new AlertDialog.Builder(getActivity());
        if(savedInstanceState == null){
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View rootView = inflater.inflate(R.layout.fragment_add_personal_details_dialog, null);
            dialogFragment.setView(rootView);
            ButterKnife.bind(this, rootView);
        }

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

        initSpinner();
        initStatesSpinner();

        return dialogFragment.create();
    }

    public static addPersonalDetailsDialog newInstance(int id) {
        addPersonalDetailsDialog fragment = new addPersonalDetailsDialog();
        if(id > 0){
            Bundle args = new Bundle();
            args.putInt(Constants.USER_ID, id);
            fragment.setArguments(args);
        }

        return fragment;
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.employment_status_array,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employmentStatus.setAdapter(adapter);
    }

    private void initStatesSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.states_array,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(adapter);
    }
}
