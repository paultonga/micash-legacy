package ng.com.bitlab.micash.ui.ledger;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.listeners.LedgerTouchedListener;
import ng.com.bitlab.micash.models.Ledger;
import ng.com.bitlab.micash.ui.common.BaseFragment;
import ng.com.bitlab.micash.utils.LedgerItemTouchHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class LedgerListFragment extends BaseFragment implements LedgerListContract.View,
        LedgerTouchedListener,
        LedgerItemTouchHelper.ItemTouchHelperListener {

    LedgerListContract.Presenter mPresenter;
    private View mRootView;

    @BindView(R.id.content_ledger_list) RelativeLayout ledgerLayout;
    @BindView(R.id.loading_ledger_list) RelativeLayout loadingLayout;
    @BindView(R.id.empty_ledger_list) RelativeLayout emptyLayout;

    @BindView(R.id.unpaid_linear_layout) LinearLayout unpaidLayout;
    @BindView(R.id.paid_linear_layout) LinearLayout paidLayout;

    @BindView(R.id.paid_recyclerview) RecyclerView paidRecyclerview;
    @BindView(R.id.unpaid_recyclerview) RecyclerView unpaidRecyclerview;

    @BindView(R.id.progress_ledger) ProgressBar progressBar;

    private LedgerUnpaidAdapter ledgerUnpaidAdapter;
    private LedgerPaidAdapter ledgerPaidAdapter;

    public LedgerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPresenter = new LedgerListPresenter(this);
        // Inflate the layout for this fragment
        mRootView =  inflater.inflate(R.layout.fragment_ledger_list, container, false);
        ButterKnife.bind(this, mRootView);
        mPresenter = new LedgerListPresenter(this);

        List<Ledger> temp = new ArrayList<>();

        //unpaid recycler setup
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ledgerUnpaidAdapter = new LedgerUnpaidAdapter(temp, this, getContext());
        unpaidRecyclerview.setLayoutManager(layoutManager);
        unpaidRecyclerview.setAdapter(ledgerUnpaidAdapter);

        //paid recycler setup
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        ledgerPaidAdapter = new LedgerPaidAdapter(temp, getContext());
        paidRecyclerview.setLayoutManager(layoutManager2);
        paidRecyclerview.setAdapter(ledgerPaidAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new LedgerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(paidRecyclerview);

        mPresenter.loadData();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadData();
    }

    @Override
    public void onTouchPaidLedger() {

    }

    @Override
    public void onTouchUnpaidLedger() {

    }

    @Override
    public void showEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        ledgerLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingLayout() {
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0288D1, PorterDuff.Mode.MULTIPLY);
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        ledgerLayout.setVisibility(View.GONE);
    }

    @Override
    public void showContentLayout() {
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        ledgerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUnpaidLedgers(List<Ledger> unpaidLedgers) {
        paidLayout.setVisibility(View.VISIBLE);
        ledgerUnpaidAdapter.refresh(unpaidLedgers);
    }

    @Override
    public void showPaidLedgers(List<Ledger> paidLedgers) {
        unpaidLayout.setVisibility(View.VISIBLE);
        ledgerPaidAdapter.refresh(paidLedgers);
    }

    @Override
    public void onLedgerTouched(Ledger ledger, int position) {
        if(position == 0){
            showConfirmDialog(ledger);
        } else {
            showWarningDialog();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof LedgerPaidAdapter.ViewHolder){
            Ledger ledger = ((LedgerPaidAdapter.ViewHolder) viewHolder).getAtPosition(position);
            showToast(ledger.getLoanTitle() + " ledger will be deleted");
        }
    }

    private void showWarningDialog(){
        new MaterialDialog.Builder(getContext())
                .title("Warning")
                .content("Please don't skip payment ledger. You need to repay ledgers in the right order.")
                .negativeText("CLOSE")
                .negativeColor(Color.RED)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showConfirmDialog(final Ledger ledger){
        new MaterialDialog.Builder(getContext())
                .title("Payment Sent")
                .content("This is to confirm that the stated amount has been paid to miCash.")
                .positiveText("CONTINUE")
                .positiveColor(Color.BLUE)
                .negativeText("CANCEL")
                .negativeColor(Color.RED)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.setLedgerAsPaid(ledger);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
