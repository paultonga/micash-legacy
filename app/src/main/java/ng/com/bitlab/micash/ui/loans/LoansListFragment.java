package ng.com.bitlab.micash.ui.loans;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.listeners.OnLoanSelectedListener;
import ng.com.bitlab.micash.models.Loan;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoansListFragment extends Fragment implements LoansListContract.View, OnLoanSelectedListener {

    private View mRootView;
    private LoansListAdapter mAdapter;
    LoansListContract.Presenter mPresenter;


    @BindView(R.id.loans_list_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.empty_loans_list) RelativeLayout emptyLoansLayout;

    public LoansListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView =  inflater.inflate(R.layout.fragment_loans_list, container, false);
        ButterKnife.bind(this, mRootView);

        mPresenter = new LoansListPresenter(this);


        //setup Adapter
        List<Loan> tempProducts = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new LoansListAdapter(tempProducts, getActivity(), this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);



        return mRootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadLoans();
    }

    @Override
    public void showLoans(List<Loan> loans) {
        emptyLoansLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.refreshData(loans);
    }

    @Override
    public void showEmptyLayout() {
        emptyLoansLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void OnLoanSelected(Loan loan) {
        Intent i = new Intent(getActivity(), LoanDetailsActivity.class);
        i.putExtra("loan", loan.getId());
        startActivity(i);
    }
}
