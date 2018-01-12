package ng.com.bitlab.micash.ui.cards;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.Bank;
import ng.com.bitlab.micash.ui.addBanking.AddBankingActivity;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.utils.RecyclerItemTouchHelper;

public class CardsActivity extends BaseView implements CardsContract.View, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @BindView(R.id.add_banking_button) Button addBankingButton;
    @BindView(R.id.empty_bank_list) RelativeLayout emptyLayout;
    @BindView(R.id.bank_list_recyclerview) RecyclerView mRecycler;
    @BindView(R.id.bank_loading_layout) RelativeLayout loadingLayout;

    CardsListAdapter mAdapter;
    CardsContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Banking Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        mPresenter = new CardsPresenter(this);

        List<Bank> temp = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new CardsListAdapter(temp);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecycler);


        mPresenter.fetchData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add){
            showAddDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onAddButtonClicked(){
        startActivity(new Intent(this, AddBankingActivity.class));
    }

    @Override
    public void showEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
    }



    @Override
    public void showRecords(List<Bank> records) {
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        mAdapter.refreshData(records);
    }

    @Override
    public void showLoadingLayout() {
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
    }

    @Override
    @OnClick(R.id.add_banking_button)
    public void showAddDialog() {
        new MaterialDialog.Builder(this)
                .title("Add Bank Account")
                .customView(R.layout.custom_add_bank_dialog, true)
                .autoDismiss(false)
                .positiveColor(Color.BLUE)
                .positiveText("Save Account")
                .negativeColor(Color.BLACK)
                .negativeText("CANCEL")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        boolean flag = false;
                        EditText etbank = (EditText) dialog.getCustomView().findViewById(R.id.input_bank_name);
                        EditText account = (EditText) dialog.getCustomView().findViewById(R.id.input_bank_account);
                        EditText bvn = (EditText) dialog.getCustomView().findViewById(R.id.input_bank_bvn);

                        if(etbank.getText().length() < 3){
                            flag = true;
                            etbank.setError("Invalid bank name");
                        }
                        if(account.getText().length() < 10){
                            flag = true;
                            account.setError("Invalid account number");
                        }
                        if(bvn.getText().length() < 11){
                            flag = true;
                            bvn.setError("Invalid BVN");
                        }
                        if(!flag){
                            Bank bank = new Bank(etbank.getText().toString(), account.getText().toString(),
                                    bvn.getText().toString());
                            mPresenter.checkAccount(bank);
                            dialog.dismiss();
                        }
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

    @Override
    protected void onResume() {
        super.onResume();
        //mPresenter.loadData();
        mPresenter.fetchData();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CardsListAdapter.ViewHolder){
           Bank bank = ((CardsListAdapter.ViewHolder) viewHolder).getBankAtPosition(position);
           mPresenter.deleteRecord(bank);
        }
    }
}
