package ng.com.bitlab.micash.ui.addBanking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.com.bitlab.micash.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class addBankingFragmentDialog extends Fragment {


    public addBankingFragmentDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_banking_fragment_dialog, container, false);
    }

}
